package tn.cita.app.business.reservation.employee.manager.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequiredArgsConstructor
public class ManagerWorkerAssignmentResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerWorkerAssignmentService managerWorkerAssignmentService;
	
	@GetMapping("/{workerId}")
	public ResponseEntity<ApiResponse<ManagerWorkerAssignmentResponse>> fetchAllWorkerTasks(
					final WebRequest webRequest, 
					@PathVariable final String workerId, 
					@RequestParam final Map<String, String> params) {
		log.info("** Fetch all worker tasks by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerWorkerAssignmentService.fetchAllWorkerTasks(
						this.userRequestExtractorUtil.extractUsername(webRequest),
						Integer.parseInt(workerId),
						ClientPageRequest.from(params))));
	}
	
	@GetMapping("/{workerId}/search/{key}")
	public ResponseEntity<ApiResponse<ManagerWorkerAssignmentResponse>> searchAllReservationsLikeKey(
					final WebRequest webRequest, 
					@PathVariable final String workerId, 
					@PathVariable final String key) {
		log.info("** Search all reservations like key by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerWorkerAssignmentService.searchAllLikeKey(
						this.userRequestExtractorUtil.extractUsername(webRequest),
						Integer.parseInt(workerId),
						key)));
	}
	
}




