package tn.cita.app.business.reservation.employee.worker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationService;
import tn.cita.app.exception.wrapper.EmployeeNotFoundException;
import tn.cita.app.mapper.EmployeeMapper;
import tn.cita.app.mapper.TaskMapper;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.TaskDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.repository.TaskRepository;
import tn.cita.app.util.ClientPageRequestUtils;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class WorkerReservationServiceImpl implements WorkerReservationService {
	
	private final EmployeeRepository employeeRepository;
	private final TaskRepository taskRepository;
	
	@Override
	public Page<TaskDto> fetchAllReservations(final String username, final ClientPageRequest clientPageRequest) {
		log.info("** Fetch all paged reservations by worker.. *");
		final var workerDto = this.retrieveWorkerByUsername(username.strip());
		return this.taskRepository
				.findAllByWorkerId(workerDto.getId(), ClientPageRequestUtils.from(clientPageRequest))
				.map(TaskMapper::toDto);
	}
	
	@Override
	public Page<TaskDto> fetchAllReservations(final String username) {
		log.info("** Fetch all reservations by worker.. *");
		final var workerDto = this.retrieveWorkerByUsername(username.strip());
		return new PageImpl<>(this.taskRepository.findAllByWorkerId(workerDto.getId()))
				.map(TaskMapper::toDto);
	}
	
	@Override
	public Page<TaskDto> searchAllLikeKey(final String username, final String key) {
		log.info("** Search all reservations like key by worker.. *");
		final var workerDto = this.retrieveWorkerByUsername(username.strip());
		return new PageImpl<>(this.taskRepository
				.searchAllByWorkerIdLikeKey(workerDto.getId(), key.strip().toLowerCase()).stream()
					.map(TaskMapper::toDto)
					.toList());
	}
	
	private EmployeeDto retrieveWorkerByUsername(final String username) {
		return this.employeeRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(EmployeeMapper::toDto)
				.orElseThrow(() -> new EmployeeNotFoundException(
						"Employee with username: %s not found".formatted(username)));
	}
	
}



