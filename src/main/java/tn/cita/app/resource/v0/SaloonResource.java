package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.SaloonDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.SaloonService;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/saloons")
@RequiredArgsConstructor
class SaloonResource {
	
	private final SaloonService saloonService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<Page<SaloonDto>> findAll(@RequestParam Map<String, String> params) {
		var saloons = this.saloonService.findAll(ClientPageRequest.from(params));
		return ApiResponse.of(saloons.getSize(), saloons);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/locations/state/{state}")
	ApiResponse<Page<SaloonDto>> findAllByLocationState(
					@PathVariable String state, @RequestParam Map<String, String> params) {
		var saloons = this.saloonService.findAllByLocationState(state, ClientPageRequest.from(params));
		return ApiResponse.of(saloons.getSize(), saloons);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<SaloonDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.saloonService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<SaloonDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.saloonService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/code/{code}")
	ApiResponse<Page<SaloonDto>> findAllByCode(@PathVariable String code) {
		var saloons = this.saloonService.findAllByCode(code);
		return ApiResponse.of(saloons.getSize(), saloons);
	}
	
}



