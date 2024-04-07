package tn.cita.app.business.reservation.customer.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
class CustomerReservationDetailResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerReservationDetailService customerReservationDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}")
	ApiResponse<ReservationDetailResponse> fetchReservationDetails(WebRequest request, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerReservationDetailService.fetchReservationDetails(Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{reservationIdentifier}")
	ApiResponse<ReservationDetailResponse> fetchReservationDetailsWithIdentifier(WebRequest request,
																				 @PathVariable String reservationIdentifier) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerReservationDetailService.fetchReservationDetails(reservationIdentifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<ReservationDto> updateReservationDetails(WebRequest request,
														 @RequestBody @Valid ReservationDetailRequest reservationDetailRequest) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerReservationDetailService.updateReservationDetails(reservationDetailRequest));
	}
	
}




