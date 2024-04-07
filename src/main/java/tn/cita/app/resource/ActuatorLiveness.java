package tn.cita.app.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.cita.app.exception.wrapper.ActuatorHealthException;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/actuator")
@Slf4j
@RequiredArgsConstructor
class ActuatorLiveness {
	
	@Value("${management.endpoints.web.base-path:actuator}")
	private final String actuatorEndpoint;
	private final RestClient http;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/health")
	ApiResponse<HealthActuatorResponse> health() {
		log.info("App health..");
		
		var url = "%s/%s/health".formatted(
				ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.toUriString(),
				actuatorEndpoint);
		
		var health = this.http
				.get()
				.uri(url)
				.retrieve()
				.body(HealthActuatorResponse.class);
		
		if (health == null || !health.status().equalsIgnoreCase("UP"))
			throw new ActuatorHealthException("We're running into an issue 😬 Will be FIXED very soon, stay tuned..🤗🤗");
		log.info("Health endpoint status indicates: {}", health.status());
		
		return ApiResponse.of(health);
	}
	
	private record HealthActuatorResponse(String status, String[] groups) {}
	
}



