package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.OrderedDetailAlreadyExistsException;
import tn.cita.app.exception.wrapper.OrderedDetailNotFoundException;
import tn.cita.app.mapper.OrderedDetailMapper;
import tn.cita.app.model.domain.id.OrderedDetailId;
import tn.cita.app.model.dto.OrderedDetailDto;
import tn.cita.app.model.dto.request.OrderedDetailRequest;
import tn.cita.app.repository.OrderedDetailRepository;
import tn.cita.app.service.OrderedDetailService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class OrderedDetailServiceImpl implements OrderedDetailService {
	
	private final OrderedDetailRepository orderedDetailRepository;
	
	@Override
	public OrderedDetailDto findByIdentifier(final String identifier) {
		return this.orderedDetailRepository.findByIdentifier(identifier)
				.map(OrderedDetailMapper::toDto)
				.orElseThrow(OrderedDetailNotFoundException::new);
	}
	
	@Override
	public List<OrderedDetailDto> findAllByReservationId(final Integer reservationId) {
		log.info("Find all ordered detail by reservationId.. ");
		return this.orderedDetailRepository
				.findAllByReservationId(reservationId).stream()
					.map(OrderedDetailMapper::toDto)
					.toList();
	}
	
	@Transactional
	@Override
	public Boolean deleteById(final OrderedDetailId orderedDetailId) {
		log.info("Delete ordered detail by id.. ");
		this.orderedDetailRepository.deleteById(orderedDetailId);
		return !this.orderedDetailRepository.existsById(orderedDetailId);
	}
	
	@Transactional
	@Override
	public OrderedDetailDto save(OrderedDetailRequest orderedDetailRequest) {
		log.info("Save ordered detail.. ");
		
		final var orderedDetailId = new OrderedDetailId(orderedDetailRequest.reservationId(), 
				orderedDetailRequest.serviceDetailId());
		
		if (this.orderedDetailRepository.existsById(orderedDetailId))
			throw new OrderedDetailAlreadyExistsException("Service is already ordered");
		
		// Smell..
		if (orderedDetailRequest.orderedDate() == null)
			orderedDetailRequest = new OrderedDetailRequest(
					orderedDetailId.getReservationId(), 
					orderedDetailId.getServiceDetailId(), 
					LocalDateTime.now());
		
		this.orderedDetailRepository.saveOrderedDetail(orderedDetailRequest);
		return OrderedDetailMapper.toDto(this.orderedDetailRepository.findById(orderedDetailId).orElseThrow());
	}
	
}




