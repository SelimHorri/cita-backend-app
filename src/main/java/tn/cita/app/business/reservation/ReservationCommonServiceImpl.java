package tn.cita.app.business.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.reservation.employee.manager.model.ReservationAssignWorkerRequest;
import tn.cita.app.business.reservation.employee.manager.model.ReservationSubWorkerResponse;
import tn.cita.app.exception.wrapper.*;
import tn.cita.app.mapper.EmployeeMapper;
import tn.cita.app.mapper.ReservationMapper;
import tn.cita.app.model.domain.ReservationStatus;
import tn.cita.app.model.domain.entity.Task;
import tn.cita.app.model.domain.id.TaskId;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.repository.ReservationRepository;
import tn.cita.app.repository.TaskRepository;
import tn.cita.app.util.StringWrapperUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class ReservationCommonServiceImpl implements ReservationCommonService {
	
	private final EmployeeRepository employeeRepository;
	private final ReservationRepository reservationRepository;
	private final TaskRepository taskRepository;
	
	@Transactional
	@Override
	public ReservationDto cancelReservation(final Integer reservationId) {
		log.info("Cancelling reservation.. ");
		
		final var reservation = this.reservationRepository.findById(reservationId)
				.orElseThrow(ReservationNotFoundException::new);
		
		// TODO: check if reservation has been completed, should not be cancelled (or changed) anymore
		// Add completeDate of reservation along with COMPLETED status to this check
		switch (reservation.getStatus()) {
			case COMPLETED -> throw new IllegalReservationStatusException("Reservation is already completed");
			case CANCELLED -> throw new IllegalReservationStatusException("Reservation is already cancelled");
			case OUTDATED -> throw new IllegalReservationStatusException("Reservation is already outdated");
			case NOT_CLOSED -> throw new IllegalReservationStatusException("Reservation is already not-closed");
			case NOT_STARTED, IN_PROGRESS -> {
				reservation.setStatus(ReservationStatus.CANCELLED);
				reservation.setCancelDate(LocalDateTime.now());
			}
		};
		
		return ReservationMapper.toDto(this.reservationRepository.save(reservation));
	}
	
	@Override
	public ReservationSubWorkerResponse fetchAllUnassignedSubWorkers(final String username, final Integer reservationId) {
		log.info("Fetch all unassigned sub workers.. ");
		
		final var managerDto = this.employeeRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(EmployeeMapper::toDto)
				.orElseThrow(() -> new EmployeeNotFoundException(
						"Employee with username: %s not found".formatted(username)));
		
		final var assignedWorkersIds = this.taskRepository
				.findAllByReservationId(reservationId).stream()
					.map(Task::getWorkerId)
					.collect(Collectors.toUnmodifiableSet());
		
		final var unassignedWorkerDtos = this.employeeRepository
				.findAllByManagerId(managerDto.getId()).stream()
					.filter(w -> !assignedWorkersIds.contains(w.getId()))
					.map(EmployeeMapper::toDto)
					.toList();
		
		return new ReservationSubWorkerResponse(
				this.reservationRepository.findById(reservationId)
						.map(ReservationMapper::toDto)
						.orElseThrow(ReservationNotFoundException::new), 
				new PageImpl<>(unassignedWorkerDtos));
	}
	
	@Transactional
	@Override
	public ReservationSubWorkerResponse assignReservationWorkers(final String username, 
			final ReservationAssignWorkerRequest reservationAssignWorkerRequest) {
		log.info("Assign workers to a reservation.. ");
		
		final var reservation = this.reservationRepository
				.findById(reservationAssignWorkerRequest.reservationId())
				.orElseThrow(ReservationNotFoundException::new);
		final boolean isAlreadyAssigned =
				reservationAssignWorkerRequest.assignedWorkersIds().stream()
						.distinct()
						.map(workerId -> new TaskId(workerId, reservation.getId()))
						.anyMatch(this.taskRepository::existsById);
		if (isAlreadyAssigned)
			throw new TaskAlreadyAssignedException("Worker is already assigned");
		
		final List<Task> assignedWorkers = new ArrayList<>();
		final var task = new Task();
		task.setReservationId(reservation.getId());
		/*
		task.setReservation(reservation);
		*/
		task.setManagerDescription(StringWrapperUtils
				.trimIfBlank(reservationAssignWorkerRequest.managerDescription()));
		
		for (int workerId: reservationAssignWorkerRequest.assignedWorkersIds()) {
			task.setTaskDate(LocalDateTime.now()); // added! cause object is persisted natively..
			task.setIdentifier(UUID.randomUUID().toString());
			task.setWorkerId(workerId);
			/*
			task.setWorker(this.employeeRepository.findById(workerId)
					.orElseThrow(EmployeeNotFoundException::new));
			*/
			this.taskRepository.saveTask(task);
			assignedWorkers.add(this.taskRepository
					.findById(new TaskId(task.getWorkerId(), task.getReservationId()))
					.orElseThrow(TaskNotFoundException::new));
		}
		
		final var savedAssignedWorkers = assignedWorkers.stream()
				.filter(Objects::nonNull)
				.map(Task::getWorkerId)
				.map(workerId -> this.employeeRepository.findById(workerId)
						.map(EmployeeMapper::toDto)
						.orElseThrow(EmployeeNotFoundException::new))
				.toList();
		
		return new ReservationSubWorkerResponse(ReservationMapper.toDto(reservation), new PageImpl<>(savedAssignedWorkers));
	}
	
}



