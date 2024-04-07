package tn.cita.app.resource.v0;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.domain.id.OrderedDetailId;
import tn.cita.app.model.dto.OrderedDetailDto;
import tn.cita.app.model.dto.request.OrderedDetailRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.OrderedDetailService;

@RestController
@RequestMapping("${app.api-version}" + "/ordered-details")
@RequiredArgsConstructor
class OrderedDetailResource {
	
	private final OrderedDetailService orderedDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<OrderedDetailDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.orderedDetailService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/reservationId/{reservationId}")
	ApiResponse<Page<OrderedDetailDto>> findAllByReservationId(@PathVariable String reservationId) {
		var orderedDetails = new PageImpl<>(this.orderedDetailService.findAllByReservationId(Integer.parseInt(reservationId)));
		return ApiResponse.of(orderedDetails.getSize(), orderedDetails);
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping
	ApiResponse<Boolean> deleteById(@RequestBody @Valid OrderedDetailId orderedDetailId) {
		return ApiResponse.of(
				this.orderedDetailService.deleteById(orderedDetailId));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<OrderedDetailDto> save(@RequestBody @Valid OrderedDetailRequest orderedDetailRequest) {
		return ApiResponse.of(this.orderedDetailService.save(orderedDetailRequest));
	}
	
}



