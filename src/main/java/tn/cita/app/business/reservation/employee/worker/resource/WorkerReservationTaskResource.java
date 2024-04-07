package tn.cita.app.business.reservation.employee.worker.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.worker.model.TaskBeginEndRequest;
import tn.cita.app.business.reservation.employee.worker.model.TaskUpdateDescriptionRequest;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationTaskService;
import tn.cita.app.model.dto.TaskDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/workers/reservations/tasks")
@RequiredArgsConstructor
class WorkerReservationTaskResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerReservationTaskService workerReservationTaskService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}")
	ApiResponse<TaskDto> fetchAssignedTask(WebRequest webRequest, @PathVariable String reservationId) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of( 
				this.workerReservationTaskService.fetchAssignedTask(
						extractUsername, 
						Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/describe")
	ApiResponse<TaskDto> updateDescription(WebRequest webRequest,
										   @RequestBody @Valid TaskUpdateDescriptionRequest taskUpdateDescriptionRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of( 
				this.workerReservationTaskService.updateDescription(taskUpdateDescriptionRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/begin")
	ApiResponse<TaskDto> beginTask(WebRequest webRequest,
								   @RequestBody @Valid TaskBeginEndRequest taskBeginRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.workerReservationTaskService.beginTask(taskBeginRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/end")
	ApiResponse<TaskDto> endTask(WebRequest webRequest,
								 @RequestBody @Valid TaskBeginEndRequest taskEndRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.workerReservationTaskService.endTask(taskEndRequest));
	}
	
}




