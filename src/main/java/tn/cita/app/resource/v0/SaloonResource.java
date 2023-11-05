package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.SaloonDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.SaloonService;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/saloons")
@Slf4j
@RequiredArgsConstructor
public class SaloonResource {
	
	private final SaloonService saloonService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<SaloonDto>>> findAll(@RequestParam final Map<String, String> params) {
		log.info("** Find all saloons.. *");
		final var saloons = this.saloonService.findAll(ClientPageRequest.from(params));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(saloons.getSize(), saloons));
	}
	
	@GetMapping("/locations/state/{state}")
	public ResponseEntity<ApiResponse<Page<SaloonDto>>> findAllByLocationState(
					@PathVariable final String state, @RequestParam final Map<String, String> params) {
		log.info("** Find all saloons by location state.. *");
		final var saloons = this.saloonService.findAllByLocationState(state, ClientPageRequest.from(params));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(saloons.getSize(), saloons));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<SaloonDto>> findById(@PathVariable final String id) {
		log.info("** Find by id.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.saloonService.findById(Integer.parseInt(id))));
	}
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<SaloonDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find by identifier.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.saloonService.findByIdentifier(identifier.strip())));
	}
	
	@GetMapping("/code/{code}")
	public ResponseEntity<ApiResponse<Page<SaloonDto>>> findAllByCode(@PathVariable final String code) {
		log.info("** Find all salonns by code.. *");
		final var saloons = this.saloonService.findAllByCode(code);
		return ResponseEntity.ok(ApiResponse.of2xxPoly(saloons.getSize(), saloons));
	}
	
}




