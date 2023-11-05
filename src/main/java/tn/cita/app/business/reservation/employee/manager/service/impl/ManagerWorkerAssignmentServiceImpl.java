package tn.cita.app.business.reservation.employee.manager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.reservation.employee.manager.model.ManagerWorkerAssignmentResponse;
import tn.cita.app.business.reservation.employee.manager.service.ManagerWorkerAssignmentService;
import tn.cita.app.exception.wrapper.EmployeeNotFoundException;
import tn.cita.app.mapper.EmployeeMapper;
import tn.cita.app.mapper.TaskMapper;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.repository.TaskRepository;
import tn.cita.app.util.ClientPageRequestUtils;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class ManagerWorkerAssignmentServiceImpl implements ManagerWorkerAssignmentService {
	
	private final EmployeeRepository employeeRepository;
	private final TaskRepository taskRepository;
	
	@Override
	public ManagerWorkerAssignmentResponse fetchAllWorkerTasks(
			final String username, final Integer workerId, final ClientPageRequest clientPageRequest) {
		log.info("** Fetch all worker tasks by manager.. *");
		final var managerDto = this.retrieveManagerByUsername(username);
		return new ManagerWorkerAssignmentResponse(
				managerDto, 
				this.taskRepository.findAllByWorkerId(workerId, 
						ClientPageRequestUtils.from(clientPageRequest))
					.map(TaskMapper::toDto));
	}
	
	@Override
	public ManagerWorkerAssignmentResponse searchAllLikeKey(final String username, final Integer workerId, final String key) {
		log.info("** Search all worker tasks like key by manager.. *");
		return new ManagerWorkerAssignmentResponse(
				this.retrieveManagerByUsername(username),
				new PageImpl<>(this.taskRepository
						.searchAllByWorkerIdLikeKey(workerId, key.strip().toLowerCase()).stream()
							.map(TaskMapper::toDto)
							.toList()));
	}
	
	private EmployeeDto retrieveManagerByUsername(String username) {
		return this.employeeRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(EmployeeMapper::toDto)
				.orElseThrow(() -> new EmployeeNotFoundException(
						"Employee with username: %s not found".formatted(username)));
	}
	
}




