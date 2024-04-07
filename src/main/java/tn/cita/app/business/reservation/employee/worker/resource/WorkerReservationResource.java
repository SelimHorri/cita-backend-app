package tn.cita.app.business.reservation.employee.worker.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationService;
import tn.cita.app.model.dto.TaskDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/employees/workers/reservations")
@RequiredArgsConstructor
class WorkerReservationResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerReservationService workerReservationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/paged")
	ApiResponse<Page<TaskDto>> fetchAllReservations(WebRequest webRequest, @RequestParam Map<String, String> params) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		var tasks = this.workerReservationService.fetchAllReservations(extractUsername, ClientPageRequest.from(params));
		return ApiResponse.of(tasks.getSize(), tasks);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping({"", "/all"})
	ApiResponse<Page<TaskDto>> fetchAllReservations(WebRequest webRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		var tasks = this.workerReservationService.fetchAllReservations(extractUsername);
		return ApiResponse.of(tasks.getSize(), tasks);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/search/{key}")
	ApiResponse<Page<TaskDto>> searchAllReservationsLikeKey(WebRequest webRequest, @PathVariable String key) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		var tasks = this.workerReservationService.searchAllLikeKey(extractUsername, key);
		return ApiResponse.of(tasks.getSize(), tasks);
	}
	
}




