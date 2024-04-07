package tn.cita.app.business.auth.authentication.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.business.auth.authentication.model.LoginRequest;
import tn.cita.app.business.auth.authentication.model.LoginResponse;
import tn.cita.app.business.auth.authentication.service.AuthenticationService;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/authenticate")
@RequiredArgsConstructor
class AuthenticationResource {
	
	private final AuthenticationService authenticationService;
	
	@ResponseStatus(HttpStatus.OK)
	@PostMapping
	ApiResponse<LoginResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
		return ApiResponse.of(this.authenticationService.authenticate(loginRequest));
	}
	
}




