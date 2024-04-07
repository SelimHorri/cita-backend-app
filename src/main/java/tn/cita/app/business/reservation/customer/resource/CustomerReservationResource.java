package tn.cita.app.business.reservation.customer.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
class CustomerReservationResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerReservationService customerReservationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<CustomerReservationResponse> fetchAllReservations(WebRequest request, @RequestParam Map<String, String> params) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(
				this.customerReservationService.fetchAllReservations(extractUsername, ClientPageRequest.from(params)));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/cancel/{reservationId}")
	ApiResponse<ReservationDto> cancelReservation(WebRequest request, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerReservationService.cancelReservation(Integer.parseInt(reservationId)));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<ReservationDto> createReservation(WebRequest request,
												  @RequestBody @Valid ReservationRequest reservationRequest) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.customerReservationService.createReservation(reservationRequest));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/search/{key}")
	ApiResponse<CustomerReservationResponse> searchAllBySaloonIdLikeKey(WebRequest webRequest, @PathVariable String key) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.customerReservationService.searchAllByCustomerIdLikeKey(extractUsername, key));
	}
	
}




