package tn.cita.app.business.reservation.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.manager.model.ManagerReservationResponse;
import tn.cita.app.business.reservation.employee.manager.model.ReservationAssignWorkerRequest;
import tn.cita.app.business.reservation.employee.manager.model.ReservationSubWorkerResponse;
import tn.cita.app.business.reservation.employee.manager.service.ManagerReservationService;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/reservations")
@RequiredArgsConstructor
class ManagerReservationResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerReservationService managerReservationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/all")
	ApiResponse<ManagerReservationResponse> fetchAllReservations(WebRequest webRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerReservationService.fetchAllReservations(extractUsername));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/paged")
	ApiResponse<ManagerReservationResponse> fetchAllReservations(WebRequest webRequest, @RequestParam Map<String, String> params) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of( 
				this.managerReservationService.fetchAllReservations(
						extractUsername,
						ClientPageRequest.from(params)));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/cancel/{reservationId}")
	ApiResponse<ReservationDto> cancelReservation(WebRequest request, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.managerReservationService.cancelReservation(Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/search/{key}")
	ApiResponse<ManagerReservationResponse> searchAllBySaloonIdLikeKey(WebRequest webRequest, @PathVariable String key) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerReservationService.searchAllBySaloonIdLikeKey(extractUsername, key));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}/unassigned")
	ApiResponse<ReservationSubWorkerResponse> fetchAllUnassignedSubWorkers(WebRequest webRequest, @PathVariable String reservationId) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerReservationService.fetchAllUnassignedSubWorkers(
						extractUsername,
						Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping("/assign")
	ApiResponse<ReservationSubWorkerResponse> assignReservationWorkers(WebRequest webRequest,
																	   @RequestBody @Valid ReservationAssignWorkerRequest assignWorkerRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerReservationService.assignReservationWorkers(extractUsername,assignWorkerRequest));
	}
	
}




