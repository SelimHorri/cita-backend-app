package tn.cita.app.business.reservation.employee.manager.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.manager.model.ManagerWorkerAssignmentResponse;
import tn.cita.app.business.reservation.employee.manager.service.ManagerWorkerAssignmentService;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/workers/assignments")
@RequiredArgsConstructor
class ManagerWorkerAssignmentResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerWorkerAssignmentService managerWorkerAssignmentService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{workerId}")
	ApiResponse<ManagerWorkerAssignmentResponse> fetchAllWorkerTasks(WebRequest webRequest,
																	 @PathVariable String workerId,
																	 @RequestParam Map<String, String> params) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerWorkerAssignmentService.fetchAllWorkerTasks(
						extractUsername,
						Integer.parseInt(workerId),
						ClientPageRequest.from(params)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{workerId}/search/{key}")
	ApiResponse<ManagerWorkerAssignmentResponse> searchAllReservationsLikeKey(WebRequest webRequest,
																			  @PathVariable String workerId,
																			  @PathVariable String key) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerWorkerAssignmentService.searchAllLikeKey(extractUsername, Integer.parseInt(workerId), key));
	}
	
}




