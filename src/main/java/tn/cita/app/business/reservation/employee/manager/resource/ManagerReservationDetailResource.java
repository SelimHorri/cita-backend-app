package tn.cita.app.business.reservation.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.manager.model.ReservationAssignWorkerRequest;
import tn.cita.app.business.reservation.employee.manager.model.ReservationSubWorkerResponse;
import tn.cita.app.business.reservation.employee.manager.service.ManagerReservationDetailService;
import tn.cita.app.model.dto.response.ReservationBeginEndTaskResponse;
import tn.cita.app.model.dto.response.ReservationDetailResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/reservations/details")
@RequiredArgsConstructor
class ManagerReservationDetailResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerReservationDetailService managerReservationDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}")
	ApiResponse<ReservationDetailResponse> fetchReservationDetails(WebRequest request, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.managerReservationDetailService.fetchReservationDetails(Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}/tasks/info/beginEnd")
	ApiResponse<ReservationBeginEndTaskResponse> fetchBeginEndTask(WebRequest webRequest, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerReservationDetailService.fetchBeginEndTask(Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}/tasks/unassigned")
	ApiResponse<ReservationSubWorkerResponse> fetchAllUnassignedSubWorkers(WebRequest webRequest, @PathVariable String reservationId) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerReservationDetailService.fetchAllUnassignedSubWorkers(
						extractUsername,
						Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/tasks/assign")
	ApiResponse<ReservationSubWorkerResponse> assignReservationWorkers(WebRequest webRequest,
																	   @RequestBody @Valid ReservationAssignWorkerRequest assignWorkerRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerReservationDetailService.assignReservationWorkers(extractUsername, assignWorkerRequest));
	}
	
}




