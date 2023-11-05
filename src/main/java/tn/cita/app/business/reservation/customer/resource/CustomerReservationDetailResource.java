package tn.cita.app.business.reservation.customer.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.customer.service.CustomerReservationDetailService;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.request.ReservationDetailRequest;
import tn.cita.app.model.dto.response.ReservationDetailResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/customers/reservations/details")
@Slf4j
@RequiredArgsConstructor
public class CustomerReservationDetailResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerReservationDetailService customerReservationDetailService;
	
	@GetMapping("/{reservationId}")
	public ResponseEntity<ApiResponse<ReservationDetailResponse>> fetchReservationDetails(
					final WebRequest request, @PathVariable final String reservationId) {
		log.info("** Fetch customer reservation details.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerReservationDetailService.fetchReservationDetails(Integer.parseInt(reservationId))));
	}
	
	@GetMapping("/identifier/{reservationIdentifier}")
	public ResponseEntity<ApiResponse<ReservationDetailResponse>> fetchReservationDetailsWithIdentifier(
					final WebRequest request, @PathVariable final String reservationIdentifier) {
		log.info("** Fetch customer reservation details.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerReservationDetailService.fetchReservationDetails(reservationIdentifier.strip())));
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse<ReservationDto>> updateReservationDetails(
					final WebRequest request, @RequestBody @Valid final ReservationDetailRequest reservationDetailRequest) {
		log.info("** Update customer reservation details.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerReservationDetailService.updateReservationDetails(reservationDetailRequest)));
	}
	
}




