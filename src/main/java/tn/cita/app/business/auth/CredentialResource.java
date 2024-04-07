package tn.cita.app.business.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.CredentialDto;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/credentials")
@RequiredArgsConstructor
class CredentialResource {
	
	private final CredentialService credentialService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<CredentialDto> findByIdentifier(@PathVariable final String identifier) {
		return ApiResponse.of(this.credentialService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/username/{username}")
	ApiResponse<CredentialDto> findByUsername(@PathVariable final String username) {
		return ApiResponse.of(this.credentialService.findByUsername(username));
	}
	
}







