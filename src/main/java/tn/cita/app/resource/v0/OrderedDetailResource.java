package tn.cita.app.resource.v0;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.domain.id.OrderedDetailId;
import tn.cita.app.model.dto.OrderedDetailDto;
import tn.cita.app.model.dto.request.OrderedDetailRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.OrderedDetailService;

@RestController
@RequestMapping("${app.api-version}" + "/ordered-details")
@Slf4j
@RequiredArgsConstructor
public class OrderedDetailResource {
	
	private final OrderedDetailService orderedDetailService;
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<OrderedDetailDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find by identifier.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.orderedDetailService.findByIdentifier(identifier.strip())));
	}
	
	@GetMapping("/reservationId/{reservationId}")
	public ResponseEntity<ApiResponse<Page<OrderedDetailDto>>> findAllByReservationId(@PathVariable final String reservationId) {
		log.info("** Find all ordered details by reservationId.. *");
		final var orderedDetailDtos = new PageImpl<>(this.orderedDetailService.findAllByReservationId(Integer.parseInt(reservationId)));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(orderedDetailDtos.getSize(), orderedDetailDtos));
	}
	
	@DeleteMapping
	public ResponseEntity<ApiResponse<Boolean>> deleteById(@RequestBody @Valid final OrderedDetailId orderedDetailId) {
		log.info("** Delete an ordered detail by id.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.orderedDetailService.deleteById(orderedDetailId)));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<OrderedDetailDto>> save(@RequestBody @Valid final OrderedDetailRequest orderedDetailRequest) {
		log.info("** Save an ordered detail.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.orderedDetailService.save(orderedDetailRequest)));
	}
	
}




