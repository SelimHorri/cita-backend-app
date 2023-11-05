package tn.cita.app.business.reservation.customer.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.customer.model.CustomerReservationResponse;
import tn.cita.app.business.reservation.customer.service.CustomerReservationService;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.request.ReservationRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/customers/reservations")
@Slf4j
@RequiredArgsConstructor
public class CustomerReservationResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerReservationService customerReservationService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<CustomerReservationResponse>> fetchAllReservations(
					final WebRequest request, @RequestParam final Map<String, String> params) {
		log.info("** Fetch all customer reservations.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerReservationService.fetchAllReservations(
						this.userRequestExtractorUtil.extractUsername(request), ClientPageRequest.from(params))));
	}
	
	@PutMapping("/cancel/{reservationId}")
	public ResponseEntity<ApiResponse<ReservationDto>> cancelReservation(
					final WebRequest request, @PathVariable final String reservationId) {
		log.info("** Cancel customer reservation.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.customerReservationService.cancelReservation(Integer.parseInt(reservationId))));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ReservationDto>> createReservation(
					final WebRequest request, @RequestBody @NotNull @Valid final ReservationRequest reservationRequest) {
		log.info("** Create customer reservation.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.customerReservationService.createReservation(reservationRequest)));
	}
	
	@GetMapping("/search/{key}")
	public ResponseEntity<ApiResponse<CustomerReservationResponse>> searchAllBySaloonIdLikeKey(
					final WebRequest webRequest, @PathVariable final String key) {
		log.info("** Search all customer reservations by saloonId like key.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerReservationService.searchAllByCustomerIdLikeKey(
						this.userRequestExtractorUtil.extractUsername(webRequest), key)));
	}
	
}




