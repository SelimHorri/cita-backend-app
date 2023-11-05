package tn.cita.app.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.cita.app.exception.wrapper.ActuatorHealthException;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/actuator")
@Slf4j
@RequiredArgsConstructor
public class ActuatorLiveness {
	
	private final RestTemplate restTemplate;
	
	@Value("${management.endpoints.web.base-path:actuator}")
	private String actuatorEndpoint;
	
	@GetMapping("/health")
	public ResponseEntity<ApiResponse<HealthActuatorResponse>> health() {
		log.info("** App health.. *");
		
		final var healthException = new ActuatorHealthException(
				"We're running into an issue 😬 Will be FIXED very soon, stay tuned..🤗");
		
		final HealthActuatorResponse health;
		
		try {
			var url = "%s/%s/health".formatted(
					ServletUriComponentsBuilder.fromCurrentContextPath().toUriString(), actuatorEndpoint);
			health = this.restTemplate.getForObject(url, HealthActuatorResponse.class);
		}
		catch (RestClientException e) {
			log.warn(e.getMessage());
			throw healthException;
		}
		
		if (health == null || !health.status().equalsIgnoreCase("UP"))
			throw healthException;
		
		return ResponseEntity.ok(ApiResponse.of2xxMono(health));
	}
	
	record HealthActuatorResponse(String status, String[] groups) {}
	
}



