package tn.cita.app.business.reservation.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequiredArgsConstructor
public class ManagerReservationResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerReservationService managerReservationService;
	
	@GetMapping("/all")
	public ResponseEntity<ApiResponse<ManagerReservationResponse>> fetchAllReservations(final WebRequest webRequest) {
		log.info("** Fetch all reservations by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerReservationService.fetchAllReservations(
						this.userRequestExtractorUtil.extractUsername(webRequest))));
	}
	
	@GetMapping("/paged")
	public ResponseEntity<ApiResponse<ManagerReservationResponse>> fetchAllReservations(
					final WebRequest webRequest, @RequestParam final Map<String, String> params) {
		log.info("** Fetch all paged reservations by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.managerReservationService.fetchAllReservations(
						this.userRequestExtractorUtil.extractUsername(webRequest), 
						ClientPageRequest.from(params))));
	}
	
	@PutMapping("/cancel/{reservationId}")
	public ResponseEntity<ApiResponse<ReservationDto>> cancelReservation(
					final WebRequest request, @PathVariable final String reservationId) {
		log.info("** Cancel reservation by manager.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.managerReservationService.cancelReservation(Integer.parseInt(reservationId))));
	}
	
	@GetMapping("/search/{key}")
	public ResponseEntity<ApiResponse<ManagerReservationResponse>> searchAllBySaloonIdLikeKey(
					final WebRequest webRequest, @PathVariable final String key) {
		log.info("** Search all reservations by saloonId by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerReservationService.searchAllBySaloonIdLikeKey(
						this.userRequestExtractorUtil.extractUsername(webRequest), key)));
	}
	
	@GetMapping("/{reservationId}/unassigned")
	public ResponseEntity<ApiResponse<ReservationSubWorkerResponse>> fetchAllUnassignedSubWorkers(
					final WebRequest webRequest, @PathVariable final String reservationId) {
		log.info("** Fetch all unassigned sub workers by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerReservationService.fetchAllUnassignedSubWorkers(
						this.userRequestExtractorUtil.extractUsername(webRequest), 
						Integer.parseInt(reservationId))));
	}
	
	@PostMapping("/assign")
	public ResponseEntity<ApiResponse<ReservationSubWorkerResponse>> assignReservationWorkers(
					final WebRequest webRequest, 
					@RequestBody @Valid final ReservationAssignWorkerRequest reservationAssignWorkerRequest) {
		log.info("** Assign workers to a reservation by manager.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.managerReservationService.assignReservationWorkers(
						this.userRequestExtractorUtil.extractUsername(webRequest),
						reservationAssignWorkerRequest)));
	}
	
}




