package tn.cita.app.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.container.AbstractSharedMySQLTestContainer;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.model.dto.TagDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TagResourceIntegrationTest extends AbstractSharedMySQLTestContainer {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Disabled
	@Test
	void givenValidPageOffset_whenFindAll_thenAllTagsBasedOnPageOffsetShouldBeReturned() {
		
		final var clientPageRequest = new ClientPageRequest(0, 0, null, null);
		
		final var list = List.of(
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("barber")
				.build(),
				TagDto.builder()
				.id(null)
				.name("manicure")
				.build()
		);
		
		final var expectedPayload = new ApiResponse<>(2, true, list);
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/tags?offset={offset}", clientPageRequest.getOffset())
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedPayload.total()))
					.jsonPath("$.acknowledge").value(is(expectedPayload.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.size()").value(is(expectedPayload.responseBody().size()));
	}
	
	@Test
	void givenValidId_whenFindById_thenTagDtoShouldBeFound() {
		
		final int id = 1;
		final var tagDto = TagDto.builder()
				.id(id)
				.name("barber")
				.build();
		
		final var expectedPayload = new ApiResponse<>(-1, true, tagDto);
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/tags/{id}", id)
				.exchange()
				.expectStatus()
					.is2xxSuccessful()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedPayload.total()))
					.jsonPath("$.acknowledge").value(is(expectedPayload.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.id").value(notNullValue())
					.jsonPath("$.responseBody.name").value(is(expectedPayload.responseBody().getName()));
	}
	
	@Test
	void givenInvalidId_whenFindById_thenTagNotFoundExceptionShouldBeThrown() {
		
		final int id = 0;
		final var expectedPayload = new ApiResponse<>(-1, false, 
				new ExceptionMsg(String.format("%sTag not found%s", "#### ", "! ####")));
		
		this.webTestClient
				.get()
				.uri(AppConstants.API_CONTEXT_V0 + "/tags/{id}", id)
				.exchange()
				.expectStatus()
					.isBadRequest()
				.expectBody()
					.jsonPath("$").value(notNullValue())
					.jsonPath("$.totalResult").value(is(expectedPayload.total()))
					.jsonPath("$.acknowledge").value(is(expectedPayload.acknowledge()))
					.jsonPath("$.responseBody").value(notNullValue())
					.jsonPath("$.responseBody.errorMsg").value(startsWith("#### "))
					.jsonPath("$.responseBody.errorMsg").value(endsWith("! ####"))
					.jsonPath("$.responseBody.errorMsg").value(is(expectedPayload.responseBody().errorMsg()));
	}
	
}






