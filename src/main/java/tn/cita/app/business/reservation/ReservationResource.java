package tn.cita.app.business.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/reservations")
@Slf4j
@RequiredArgsConstructor
public class ReservationResource {
	
	private final ReservationService reservationService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ReservationDto>> findById(@PathVariable final String id) {
		log.info("** Find a reservation by id.. *\n");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.reservationService.findById(Integer.parseInt(id))));
	}
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<ReservationDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find by identifier.. *\n");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.reservationService.findByIdentifier(identifier.strip())));
	}
	
	@GetMapping("/code/{code}")
	public ResponseEntity<ApiResponse<ReservationDto>> findByCode(@PathVariable final String code) {
		log.info("** Find a reservation by code.. *\n");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.reservationService.findByCode(code)));
	}
	
	@GetMapping("/saloonId/{saloonId}")
	public ResponseEntity<ApiResponse<Page<ReservationDto>>> findAllBySaloonId(@PathVariable final String saloonId) {
		log.info("** Find all reservations by saloonId.. *\n");
		final var reservations = new PageImpl<>(this.reservationService.findAllBySaloonId(Integer.parseInt(saloonId)));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(reservations.getSize(), reservations));
	}
	
}




