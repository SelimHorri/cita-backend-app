package tn.cita.app.business.reservation.employee.worker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationDetailService;
import tn.cita.app.exception.wrapper.ReservationNotFoundException;
import tn.cita.app.mapper.OrderedDetailMapper;
import tn.cita.app.mapper.ReservationMapper;
import tn.cita.app.mapper.TaskMapper;
import tn.cita.app.model.dto.response.ReservationDetailResponse;
import tn.cita.app.repository.OrderedDetailRepository;
import tn.cita.app.repository.ReservationRepository;
import tn.cita.app.repository.TaskRepository;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class WorkerReservationDetailServiceImpl implements WorkerReservationDetailService {
	
	private final TaskRepository taskRepository;
	private final ReservationRepository reservationRepository;
	private final OrderedDetailRepository orderedDetailRepository;
	
	@Override
	public ReservationDetailResponse fetchReservationDetails(final Integer reservationId) {
		log.info("** Fetch reservation details by reservationId by worker.. *");
		final var reservationDto = this.reservationRepository.findById(reservationId)
				.map(ReservationMapper::toDto)
				.orElseThrow(ReservationNotFoundException::new);
		return ReservationDetailResponse.builder()
				.reservationDto(reservationDto)
				.orderedDetailDtos(new PageImpl<>(this.orderedDetailRepository
						.findAllByReservationId(reservationDto.getId()).stream()
						.map(OrderedDetailMapper::toDto)
						.toList()))
				.taskDtos(new PageImpl<>(this.taskRepository
						.findAllByReservationId(reservationDto.getId()).stream()
						.map(TaskMapper::toDto)
						.toList()))
				.build();
	}
	
}




