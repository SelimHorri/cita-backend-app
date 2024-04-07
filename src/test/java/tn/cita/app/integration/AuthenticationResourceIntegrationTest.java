package tn.cita.app.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.reactive.server.WebTestClient;
import tn.cita.app.business.auth.authentication.model.LoginRequest;
import tn.cita.app.business.auth.authentication.model.LoginResponse;
import tn.cita.app.business.auth.authentication.service.AuthenticationService;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.container.AbstractSharedMySQLTestContainer;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.JwtUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthenticationResourceIntegrationTest extends AbstractSharedMySQLTestContainer {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private WebTestClient webTestClient;
	private LoginRequest loginRequest;
	private LoginResponse loginResponse;
	
	@BeforeEach
	void setUp() {
		loginRequest = new LoginRequest("selimhorri", "0000");
	}
	
	@Test
	void givenLoginApiUrl_whenRequestIsValid_thenLoginResponseShouldBeReturned() {
		
		this.loginResponse = this.authenticationService.authenticate(loginRequest);
		final var apiPayloadResponse = new ApiResponse<>(-1, true, loginResponse);
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/authenticate")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(loginRequest)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectHeader()
					.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(apiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(apiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.username").value(is(apiPayloadResponse.responseBody().username()));
		
		final boolean validateToken = this.jwtUtils.validateToken(this.loginResponse.jwtToken(), 
				this.userDetailsService.loadUserByUsername(this.loginResponse.username()));
		assertThat(validateToken).isTrue();
	}
	
	@Test
	void givenLoginApiUrl_whenRequestWithInvalidUsername_thenExceptionMsgShouldBeReturned() {
		
		final var wrongUsernameLoginRequest = new LoginRequest(this.loginRequest + "iiii", this.loginRequest.password());
		final var apiPayloadResponse = new ApiResponse<ExceptionMsg>(-1, false, 
				new ExceptionMsg("#### Username is not registered! ####"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/authenticate")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(wrongUsernameLoginRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectHeader()
					.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(apiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(apiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg")
						.value(containsStringIgnoringCase("Username is not registered"))
					.jsonPath("$.responseBody.errorMsg").value(is(apiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenLoginApiUrl_whenRequestWithInvalidPasswordOrElse_thenExceptionMsgShouldBeReturned() {
		
		final var wrongCredentialsLoginRequest = new LoginRequest(this.loginRequest.username(), this.loginRequest.password() + "012545");
		final var apiPayloadResponse = new ApiResponse<ExceptionMsg>(-1, false, 
				new ExceptionMsg("#### Incorrect password! ####"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/authenticate")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(wrongCredentialsLoginRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectHeader()
					.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(apiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(apiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg").value(containsStringIgnoringCase("Incorrect password"))
					.jsonPath("$.responseBody.errorMsg").value(is(apiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenLoginApiUrl_whenRequestUsernameIsBlank_thenCustomValidationExceptionMsgShouldBeReturned() {
		
		final var wrongCredentialsLoginRequest = new LoginRequest(null, this.loginRequest.password());
		final var apiPayloadResponse = new ApiResponse<ExceptionMsg>(-1, false, 
				new ExceptionMsg("*Input username should not be blank!*"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/authenticate")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(wrongCredentialsLoginRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectHeader()
					.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(apiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(apiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith(""))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("!*"))
					.jsonPath("$.responseBody.errorMsg").value(containsStringIgnoringCase("Input username should not be blank"))
					.jsonPath("$.responseBody.errorMsg").value(is(apiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenLoginApiUrl_whenRequestPasswordIsEmpty_thenCustomValidationExceptionMsgShouldBeReturned() {
		
		final var wrongCredentialsLoginRequest = new LoginRequest(this.loginRequest.username(), null);
		final var apiPayloadResponse = new ApiResponse<ExceptionMsg>(-1, false, 
				new ExceptionMsg("*Input password should not be blank!*"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/authenticate")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(wrongCredentialsLoginRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectHeader()
					.contentType(MediaType.APPLICATION_JSON)
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(apiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(apiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith(""))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("!*"))
					.jsonPath("$.responseBody.errorMsg").value(containsStringIgnoringCase("Input password should not be blank"))
					.jsonPath("$.responseBody.errorMsg").value(is(apiPayloadResponse.responseBody().errorMsg()));
	}
	
}






