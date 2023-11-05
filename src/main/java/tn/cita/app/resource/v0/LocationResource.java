package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.LocationDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.LocationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/locations")
@Slf4j
@RequiredArgsConstructor
public class LocationResource {
	
	private final LocationService locationService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<LocationDto>>> findAll(@RequestParam final Map<String, String> params) {
		log.info("** Find all paged locations.. *");
		final var locations = this.locationService.findAll(ClientPageRequest.from(params));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(locations.getSize(), locations));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<LocationDto>> findById(@PathVariable final String id) {
		log.info("** Find location by id.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.locationService.findById(Integer.parseInt(id))));
	}
	
	@GetMapping("/cities")
	public ResponseEntity<ApiResponse<List<String>>> fetchAllCities() {
		log.info("** Fetch all cities.. *");
		final var cities = this.locationService.fetchAllCities();
		return ResponseEntity.ok(ApiResponse.of2xxPoly(cities.size(), cities));
	}
	
	@GetMapping("/states")
	public ResponseEntity<ApiResponse<List<String>>> fetchAllStates() {
		log.info("** Fetch all states.. *");
		final var states = this.locationService.fetchAllStates();
		return ResponseEntity.ok(ApiResponse.of2xxPoly(states.size(), states));
	}
	
}




