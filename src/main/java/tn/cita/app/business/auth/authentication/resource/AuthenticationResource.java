package tn.cita.app.business.auth.authentication.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.cita.app.business.auth.authentication.model.LoginRequest;
import tn.cita.app.business.auth.authentication.model.LoginResponse;
import tn.cita.app.business.auth.authentication.service.AuthenticationService;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/authenticate")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationResource {
	
	private final AuthenticationService authenticationService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<LoginResponse>> authenticate(@RequestBody @Valid final LoginRequest loginRequest) {
		log.info("** Authenticate user...*");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.authenticationService.authenticate(loginRequest)));
	}
	
}




