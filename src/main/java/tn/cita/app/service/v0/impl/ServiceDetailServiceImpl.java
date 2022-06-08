package tn.cita.app.service.v0.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.cita.app.constant.AppConstant;
import tn.cita.app.dto.OrderedDetailDto;
import tn.cita.app.dto.ServiceDetailDto;
import tn.cita.app.dto.response.ServiceDetailsReservationContainerResponse;
import tn.cita.app.exception.wrapper.ServiceDetailNotFoundException;
import tn.cita.app.mapper.ServiceDetailMapper;
import tn.cita.app.repository.ServiceDetailRepository;
import tn.cita.app.service.v0.OrderedDetailService;
import tn.cita.app.service.v0.ServiceDetailService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ServiceDetailServiceImpl implements ServiceDetailService {
	
	private final ServiceDetailRepository serviceDetailRepository;
	private final OrderedDetailService orderedDetailService;
	
	@Override
	public ServiceDetailRepository getServiceDetailRepository() {
		return this.serviceDetailRepository;
	}
	
	@Override
	public ServiceDetailDto findById(final Integer id) {
		return this.serviceDetailRepository.findById(id)
				.map(ServiceDetailMapper::map)
				.orElseThrow(() -> new ServiceDetailNotFoundException(String
						.format("ServiceDetail with id: %d not found", id)));
	}
	
	@Override
	public Page<ServiceDetailDto> findAllByIds(final Set<Integer> ids) {
		final var list = this.serviceDetailRepository.findAllById(ids)
				.stream()
					.map(ServiceDetailMapper::map)
					.distinct()
					.collect(Collectors.toUnmodifiableList());
		return new PageImpl<>(list, PageRequest.of(1 - 1, AppConstant.PAGE_SIZE), list.size());
	}
	
	@Override
	public ServiceDetailsReservationContainerResponse getOrderedServiceDetailsByReservationId(final Integer reservationId) {
		
		final var orderedDetailDtos = this.orderedDetailService.findAllByReservationId(reservationId);
		final var ids = orderedDetailDtos
				.stream()
					.map(OrderedDetailDto::getServiceDetailId)
					.collect(Collectors.toUnmodifiableSet());
		
		return ServiceDetailsReservationContainerResponse.builder()
				.serviceDetailDtos(this.findAllByIds(ids))
				.orderedDetailDtos(orderedDetailDtos)
				.build();
	}
	
	@Override
	public Page<ServiceDetailDto> findAllByCategoryId(final Integer categoryId) {
		return this.serviceDetailRepository.findAllByCategoryId(categoryId, PageRequest.of(1 - 1, AppConstant.PAGE_SIZE))
				.map(ServiceDetailMapper::map);
	}
	
	@Override
	public Page<ServiceDetailDto> findAllByCategorySaloonId(final Integer saloonId) {
		return this.serviceDetailRepository.findAllByCategorySaloonId(saloonId, PageRequest.of(1 - 1, AppConstant.PAGE_SIZE))
				.map(ServiceDetailMapper::map);
	}
	
	
	
}
















