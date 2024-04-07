package tn.cita.app.business.servicedetail;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.ServiceDetailDto;
import tn.cita.app.model.dto.response.ServiceDetailsReservationContainerResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/service-details")
@RequiredArgsConstructor
class ServiceDetailResource {
	
	private final ServiceDetailService serviceDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<ServiceDetailDto> findById(@PathVariable final String id) {
		return ApiResponse.of(this.serviceDetailService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<ServiceDetailDto> findByIdentifier(@PathVariable final String identifier) {
		return ApiResponse.of(this.serviceDetailService.findByIdentifier(identifier.strip()));
	}
	
	/**
	 * Secured Resource
	 * Added WHITE_BLACKLISTED_URLS_GET
	 * @param reservationId
	 * @return related services by a reservation
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/reservationId/{reservationId}")
	ApiResponse<ServiceDetailsReservationContainerResponse> fetchOrderedServiceDetails(
			@PathVariable final String reservationId) {
		return ApiResponse.of( 
				this.serviceDetailService.fetchOrderedServiceDetails(Integer.parseInt(reservationId)));
	}
	
	/**
	 * Secured Resource
	 * Added WHITE_BLACKLISTED_URLS_GET
	 * @param reservationIdentifier
	 * @return related services by a reservation
	 */
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/reservationIdentifier/{reservationIdentifier}")
	ApiResponse<ServiceDetailsReservationContainerResponse> fetchOrderedServiceDetailsWithIdentifier(
					@PathVariable final String reservationIdentifier) {
		return ApiResponse.of( 
				this.serviceDetailService.fetchOrderedServiceDetails(reservationIdentifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/saloonId/{saloonId}")
	ApiResponse<Page<ServiceDetailDto>> findAllByCategorySaloonId(@PathVariable final String saloonId) {
		final var serviceDetails = this.serviceDetailService.findAllByCategorySaloonId(Integer.parseInt(saloonId));
		return ApiResponse.of(serviceDetails.size(), new PageImpl<>(serviceDetails));
	}
	
}




