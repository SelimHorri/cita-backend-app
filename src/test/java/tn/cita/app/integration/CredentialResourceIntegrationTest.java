package tn.cita.app.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.container.AbstractSharedMySQLTestContainer;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.model.dto.CredentialDto;
import tn.cita.app.model.dto.response.api.ApiResponse;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureMockMvc // required for UserDetailsService
@WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "selimhorri")
class CredentialResourceIntegrationTest extends AbstractSharedMySQLTestContainer {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	void givenValidUsername_whenFindByUsername_thenCredentialDtoShouldBeFound() {
		
		final var username = "selimhorri";
		final var credentialDto = CredentialDto.builder()
				.id(null)
				.username(username)
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(true)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.build();
		
		final var expectedPayload = new ApiResponse<>(-1, true, credentialDto);
		
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/credentials/username/{username}", credentialDto.getUsername())
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedPayload.total()))
					.jsonPath("$.acknowledge").value(is(expectedPayload.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.username").value(is(expectedPayload.responseBody().getUsername()))
					.jsonPath("$.responseBody.role").value(is(expectedPayload.responseBody().getUserRoleBasedAuthority().name()))
					.jsonPath("$.responseBody.isEnabled").value(is(expectedPayload.responseBody().getIsEnabled()))
					.jsonPath("$.responseBody.isAccountNonExpired").value(is(expectedPayload.responseBody().getIsAccountNonExpired()))
					.jsonPath("$.responseBody.isAccountNonLocked").value(is(expectedPayload.responseBody().getIsAccountNonLocked()))
					.jsonPath("$.responseBody.isCredentialsNonExpired").value(is(expectedPayload.responseBody().getIsCredentialsNonExpired()));
	}
	
	@Disabled
	@Test
	void givenInvalidUsername_whenFindByUsername_thenCredentialNotFoundExceptionShouldBeThrown() {
		
		final var username = "selimhorri" + UUID.randomUUID().toString();
		
		final var exceptionMsg = new ExceptionMsg("#### Credential with username %s not found! ####".formatted(username));
		
		final var expectedPayload = new ApiResponse<>(-1, false, exceptionMsg);
		
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/credentials/username/{username}", username)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedPayload.total()))
					.jsonPath("$.acknowledge").value(is(expectedPayload.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(is(expectedPayload.responseBody().errorMsg()));
					
	}
	
}




