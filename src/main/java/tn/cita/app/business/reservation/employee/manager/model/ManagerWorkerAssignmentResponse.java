package tn.cita.app.business.reservation.employee.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.TaskDto;

public record ManagerWorkerAssignmentResponse(
		
		@JsonProperty("manager")
		@JsonInclude(Include.NON_NULL)
		EmployeeDto managerDto,
		
		@JsonProperty("tasks")
		@JsonInclude(Include.NON_NULL)
		Page<TaskDto> tasksDtos) {}



