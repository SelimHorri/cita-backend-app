package tn.cita.app.business.reservation.employee.worker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.reservation.employee.worker.model.TaskBeginEndRequest;
import tn.cita.app.business.reservation.employee.worker.model.TaskUpdateDescriptionRequest;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationTaskService;
import tn.cita.app.exception.wrapper.*;
import tn.cita.app.mapper.EmployeeMapper;
import tn.cita.app.mapper.TaskMapper;
import tn.cita.app.model.domain.ReservationStatus;
import tn.cita.app.model.domain.entity.Task;
import tn.cita.app.model.domain.id.TaskId;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.TaskDto;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.repository.ReservationRepository;
import tn.cita.app.repository.TaskRepository;
import tn.cita.app.util.StringWrapperUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class WorkerReservationTaskServiceImpl implements WorkerReservationTaskService {
	
	private final EmployeeRepository employeeRepository;
	private final TaskRepository taskRepository;
	private final ReservationRepository reservationRepository;
	
	@Override
	public TaskDto fetchAssignedTask(final String username, final Integer reservationId) {
		log.info("** Fetch assigned task by worker.. *");
		final var workerDto = this.retrieveWorkerByUsername(username);
		return this.taskRepository
				.findById(new TaskId(workerDto.getId(), reservationId))
				.map(TaskMapper::toDto)
				.orElseThrow(TaskNotFoundException::new);
	}
	
	@Transactional
	@Override
	public TaskDto updateDescription(final TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
		log.info("** Update description by worker.. *");
		
		final var workerDto = this.retrieveWorkerByUsername(taskUpdateDescriptionRequest.username());
		
		final var reservation = this.reservationRepository
				.findById(taskUpdateDescriptionRequest.reservationId())
				.orElseThrow(ReservationNotFoundException::new);
		
		final var task = this.taskRepository
				.findById(new TaskId(workerDto.getId(), reservation.getId()))
				.orElseThrow(TaskNotFoundException::new);
		
		if (Objects.nonNull(task.getEndDate()))
			throw new TaskAlreadyEndedException("Task is already ended. "
					+ "Unfortunately, you cannot comment it anymore");
		
		// update worker description..
		task.setWorkerDescription(StringWrapperUtils
				.trimIfBlank(taskUpdateDescriptionRequest.workerDescription()));
		return TaskMapper.toDto(this.taskRepository.save(task));
	}
	
	@Transactional
	@Override
	public TaskDto beginTask(final TaskBeginEndRequest taskBeginRequest) {
		log.info("** Begin task by worker.. *");
		
		final var workerDto = this.retrieveWorkerByUsername(taskBeginRequest.username());
		
		final var reservation = this.reservationRepository
				.findById(taskBeginRequest.reservationId())
				.orElseThrow(ReservationNotFoundException::new);
		
		final var task = this.taskRepository
				.findById(new TaskId(workerDto.getId(), reservation.getId()))
				.orElseThrow(TaskNotFoundException::new);
		
		if (Objects.nonNull(task.getEndDate()))
			throw new TaskAlreadyEndedException("Task already ended");
		if (Objects.nonNull(task.getStartDate()))
			throw new TaskAlreadyBeganException("Task already began");
		
		// begin task..
		task.setStartDate(LocalDateTime.now());
		task.setWorkerDescription(StringWrapperUtils
				.trimIfBlank(taskBeginRequest.workerDescription()));
		
		// make reservation as in_progress..
		if (!reservation.getStatus().equals(ReservationStatus.NOT_STARTED)
				&& !reservation.getStatus().equals(ReservationStatus.IN_PROGRESS))
			throw new IllegalArgumentException("Illegal reservation status when beginning task");
		
		reservation.setStatus(ReservationStatus.IN_PROGRESS);
		
		// update reservation status..
		final var updatedReservation = this.reservationRepository.save(reservation);
		task.setReservationId(updatedReservation.getId());
		task.setReservation(updatedReservation);
		
		return TaskMapper.toDto(this.taskRepository.save(task));
	}
	
	@Transactional
	@Override
	public TaskDto endTask(final TaskBeginEndRequest taskEndRequest) {
		log.info("** End task by worker.. *");
		
		final var workerDto = this.retrieveWorkerByUsername(taskEndRequest.username());
		
		final var reservation = this.reservationRepository
				.findById(taskEndRequest.reservationId())
				.orElseThrow(ReservationNotFoundException::new);
		
		final var task = this.taskRepository
				.findById(new TaskId(workerDto.getId(), reservation.getId()))
				.orElseThrow(TaskNotFoundException::new);
		
		if (task.getStartDate() == null)
			throw new TaskNotBeganException("Task have not began yet");
		if (task.getEndDate() != null)
			throw new TaskAlreadyEndedException("Task is already ended");
		
		// update task to be ended..
		task.setEndDate(LocalDateTime.now());
		task.setWorkerDescription(StringWrapperUtils
				.trimIfBlank(taskEndRequest.workerDescription()));
		
		// fetch all assigned workers to this reservation..
		final var assignedOtherTasks = this.taskRepository
				.findAllByReservationId(taskEndRequest.reservationId()).stream()
					.filter(t -> !t.getWorkerId().equals(workerDto.getId()))
					.toList();
		
		final boolean areAllTasksEnded = assignedOtherTasks.stream()
					.map(Task::getEndDate)
					.allMatch(Objects::nonNull);
		
		// update reservation as COMPLETED if match..
		if (areAllTasksEnded) {
			reservation.setStatus(ReservationStatus.COMPLETED);
			reservation.setCompleteDate(LocalDateTime.now());
			final var completedReservation = this.reservationRepository.save(reservation);
			task.setReservationId(completedReservation.getId());
			task.setReservation(completedReservation);
		}
		
		return TaskMapper.toDto(this.taskRepository.save(task));
	}
	
	private EmployeeDto retrieveWorkerByUsername(final String username) {
		return this.employeeRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(EmployeeMapper::toDto)
				.orElseThrow(() -> new EmployeeNotFoundException(
						"Employee with username: %s not found".formatted(username)));
	}
	
}




