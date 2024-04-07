package tn.cita.app.business.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/reservations")
@RequiredArgsConstructor
class ReservationResource {
	
	private final ReservationService reservationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<ReservationDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.reservationService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<ReservationDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.reservationService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/code/{code}")
	ApiResponse<ReservationDto> findByCode(@PathVariable String code) {
		return ApiResponse.of(this.reservationService.findByCode(code));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/saloonId/{saloonId}")
	ApiResponse<Page<ReservationDto>> findAllBySaloonId(@PathVariable String saloonId) {
		var reservations = new PageImpl<>(this.reservationService.findAllBySaloonId(Integer.parseInt(saloonId)));
		return ApiResponse.of(reservations.getSize(), reservations);
	}
	
}




