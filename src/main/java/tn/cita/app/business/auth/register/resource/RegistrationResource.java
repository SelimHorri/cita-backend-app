package tn.cita.app.business.auth.register.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.business.auth.register.model.RegisterRequest;
import tn.cita.app.business.auth.register.model.RegisterResponse;
import tn.cita.app.business.auth.register.service.RegistrationService;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/register")
@RequiredArgsConstructor
class RegistrationResource {
	
	private final RegistrationService registrationService;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
		return ApiResponse.of(this.registrationService.register(registerRequest));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{token}")
	ApiResponse<String> validateToken(@PathVariable String token) {
		return ApiResponse.of(this.registrationService.validateToken(token));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/resend")
	ApiResponse<RegisterResponse> resendToken(@RequestParam String username) {
		return ApiResponse.of(this.registrationService.resendToken(username));
	}
	
}



