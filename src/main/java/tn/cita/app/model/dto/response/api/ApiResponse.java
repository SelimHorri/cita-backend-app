package tn.cita.app.model.dto.response.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public record ApiResponse<T>(
		@JsonFormat(shape = Shape.STRING)
		@JsonSerialize(using = InstantSerializer.class)
		@JsonDeserialize(using = InstantDeserializer.class)
		Instant timestamp,
		int totalResult,
		HttpStatus httpStatus,
		boolean acknowledge,
		T responseBody) implements Serializable {
	
	public ApiResponse {
		timestamp = Objects.requireNonNullElseGet(timestamp, Instant::now);
	}
	
	/**
	 * CAREFUL: <strong>"responseBody"</strong> generic type parameter MUST be an immutable object! <br>
	 * for List: we may use Java9+ factory methods of List interface <br>
	 * for example:<br>
	 * use: <code>List.copyOf(responseBody)</code> to create an immutable copy of the passed list in generic type.
	 */
	public ApiResponse(final Integer totalResult, final HttpStatus httpStatus, final Boolean acknowledge, final T responseBody) {
		this(Instant.now(), totalResult, httpStatus, acknowledge, responseBody);
	}
	
	public static <T> ApiResponse<T> of2xxMono(final T responseBody) {
		return new ApiResponse<>(1, HttpStatus.OK, true, responseBody);
	}
	
	public static <T> ApiResponse<T> of2xxPoly(final int totalResult, final T responseBody) {
		return new ApiResponse<>(totalResult, HttpStatus.OK, true, responseBody);
	}
	
	public static <T> ApiResponse<T> of4xxMono(final T responseBody) {
		return new ApiResponse<>(1, HttpStatus.BAD_REQUEST, false, responseBody);
	}
	
	public static <T> ApiResponse<T> of4xxPoly(final int totalResult, final T responseBody) {
		return new ApiResponse<>(totalResult, HttpStatus.BAD_REQUEST, false, responseBody);
	}
	
	public static <T> ApiResponse<T> of5xxMono(final T responseBody) {
		return new ApiResponse<>(1, HttpStatus.INTERNAL_SERVER_ERROR, false, responseBody);
	}
	
	public static <T> ApiResponse<T> of5xxPoly(final int totalResult, final T responseBody) {
		return new ApiResponse<>(totalResult, HttpStatus.INTERNAL_SERVER_ERROR, false, responseBody);
	}
	
}



