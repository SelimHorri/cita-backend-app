package tn.cita.app.service.v0.business.employee.manager;

import tn.cita.app.dto.EmployeeDto;
import tn.cita.app.dto.response.ManagerWorkerInfoResponse;

public interface ManagerWorkerInfoService {
	
	ManagerWorkerInfoResponse getAllSubWorkers(final String username);
	EmployeeDto getWorkerInfo(final Integer workerId);
	
}












