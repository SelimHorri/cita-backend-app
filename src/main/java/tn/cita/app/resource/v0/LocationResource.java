package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.LocationDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.LocationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/locations")
@RequiredArgsConstructor
class LocationResource {
	
	private final LocationService locationService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<Page<LocationDto>> findAll(@RequestParam Map<String, String> params) {
		var locations = this.locationService.findAll(ClientPageRequest.from(params));
		return ApiResponse.of(locations.getSize(), locations);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<LocationDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.locationService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/cities")
	ApiResponse<List<String>> fetchAllCities() {
		var cities = this.locationService.fetchAllCities();
		return ApiResponse.of(cities.size(), cities);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/states")
	ApiResponse<List<String>> fetchAllStates() {
		var states = this.locationService.fetchAllStates();
		return ApiResponse.of(states.size(), states);
	}
	
}



