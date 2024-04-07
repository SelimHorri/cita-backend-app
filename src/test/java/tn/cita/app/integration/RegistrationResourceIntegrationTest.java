package tn.cita.app.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tn.cita.app.business.auth.register.model.RegisterRequest;
import tn.cita.app.business.auth.register.model.RegisterResponse;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.container.AbstractSharedMySQLTestContainer;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.model.dto.response.api.ApiResponse;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class RegistrationResourceIntegrationTest extends AbstractSharedMySQLTestContainer {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@BeforeEach
	void setUp() throws Exception {
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
	}
	
	@Test
	void givenValidRegisterRequestForCustomer_whenRegister_thenRegisterResponseShouldBeFound() {
		
		final var username = "seliimhorri vbsjhi000000skdfjbrjusrepikfjregijo";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.CUSTOMER.name().toUpperCase())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, true,
				new RegisterResponse("""
					User with username %s has been saved successfully.
					Check your email to enable your account.
					Please consider that link will expire after %dmin from registration.
					""".formatted(registerRequest.username(), AppConstants.USER_EXPIRES_AFTER_MINUTES)));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.isSuccess").value(is(expectedApiPayloadResponse.responseBody().isSuccess()))
					.jsonPath("$.responseBody.msg").value(is(expectedApiPayloadResponse.responseBody().msg()));
	}
	
	@Test
	void givenValidRegisterRequestForWorker_whenRegister_thenRegisterResponseShouldBeFound() {
		
		final var username = "seliimhoc vskjzerrii000000skdfjbrjusrepikfregi";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.WORKER.name().toUpperCase())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, true,
				new RegisterResponse("""
					User with username %s has been saved successfully.
					Check your email to enable your account.
					Please consider that link will expire after %dmin from registration.
					""".formatted(registerRequest.username(), AppConstants.USER_EXPIRES_AFTER_MINUTES)));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.isSuccess").value(is(expectedApiPayloadResponse.responseBody().isSuccess()))
					.jsonPath("$.responseBody.msg").value(is(expectedApiPayloadResponse.responseBody().msg()));
	}
	
	@Test
	void givenValidRegisterRequestForManager_whenRegister_thenRegisterResponseShouldBeFound() {
		
		final var username = "seliimhzertgorrii000000skdfjbrjrefijerijfusrepikfregi";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.MANAGER.name().toUpperCase())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, true,
				new RegisterResponse("""
					User with username %s has been saved successfully.
					Check your email to enable your account.
					Please consider that link will expire after %dmin from registration.
					""".formatted(registerRequest.username(), AppConstants.USER_EXPIRES_AFTER_MINUTES)));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.isSuccess").value(is(expectedApiPayloadResponse.responseBody().isSuccess()))
					.jsonPath("$.responseBody.msg").value(is(expectedApiPayloadResponse.responseBody().msg()));
	}
	
	@Test
	void givenValidRegisterRequestForOwner_whenRegister_thenRegisterResponseShouldBeFound() {
		
		final var username = "seliimhorg(-tymrii000000skdfjbrjusrepikfrsrfleknrlfnkegi";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.OWNER.name().toUpperCase())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, true,
				new RegisterResponse("""
					User with username %s has been saved successfully.
					Check your email to enable your account.
					Please consider that link will expire after %dmin from registration.
					""".formatted(registerRequest.username(), AppConstants.USER_EXPIRES_AFTER_MINUTES)));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.isSuccess").value(is(expectedApiPayloadResponse.responseBody().isSuccess()))
					.jsonPath("$.responseBody.msg").value(is(expectedApiPayloadResponse.responseBody().msg()));
	}
	
	@Test
	void givenInvalidRoleInRegisterRequest_whenRegister_thenIllegalRegistrationRoleTypeExceptionShouldBeThrown() {
		
		final var username = "seliimhorg(-tymrii000000skdfjbrjusrepfggikfrsrfleknrlfnkegi";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role("XXX")
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, false,
				new ExceptionMsg("#### Wrong role type for registration, it should be Customer/Worker/Manager/Owner role! ####"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg").value(is(expectedApiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenAlreadyExistingAccountUsernameInRegisterRequest_whenRegister_thenUsernameAlreadyExistsExceptionShouldBeThrown() {
		
		final var username = "selimhorri";
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.WORKER.name())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, false,
				new ExceptionMsg("#### Account with username: " + registerRequest.username() + " already exists! ####"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg").value(is(expectedApiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenInvalidPasswordConfirmationInRegisterRequest_whenRegister_thenPasswordNotMatchExceptionShouldBeThrown() {
		
		final var username = "seliimhi" + UUID.randomUUID().toString();
		final var registerRequest = RegisterRequest.builder()
				.firstname("selliiiiiim")
				.lastname("hoooooooooorrii")
				.email("cita.team.mail@gmail.com")
				.phone("22125144")
				.birthdate(LocalDate.of(1995, 1, 9))
				.username(username)
				.password("0000rgretg")
				.confirmPassword("0000")
				.role(UserRoleBasedAuthority.CUSTOMER.name())
				.build();
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, false,
				new ExceptionMsg("#### Passwords do not match! please check again! ####"));
		
		this.webTestClient
				.post()
				.uri(AppConstants.API_CONTEXT_V0 + "/register")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(registerRequest)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectBody()
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg").value(is(expectedApiPayloadResponse.responseBody().errorMsg()));
	}
	
	@Test
	void givenValidToken_whenValidateToken_thenConfirmationMsgStringShouldBeReturned() {
		
		final var token = "c856b457-ed66-4dd4-bc1a-f0be552a28e5";
		
		final var expectedApiPayloadResponse = new ApiResponse<>(-1, true, 
				"User has been activated successfully, go and login!");
		
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/register/" + token)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedApiPayloadResponse.total()))
					.jsonPath("$.acknowledge").value(is(expectedApiPayloadResponse.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody").value(is(expectedApiPayloadResponse.responseBody()));
	}
	
}






