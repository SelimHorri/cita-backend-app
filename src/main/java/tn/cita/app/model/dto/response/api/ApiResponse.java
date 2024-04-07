package tn.cita.app.model.dto.response.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.Instant;
import java.util.Objects;

public record ApiResponse<T>(@JsonFormat(shape = Shape.STRING)
							 @JsonSerialize(using = InstantSerializer.class)
							 @JsonDeserialize(using = InstantDeserializer.class)
							 Instant timestamp,
							 int total,
							 boolean acknowledge,
							 T responseBody) {
	
	public ApiResponse {
		timestamp = Objects.requireNonNullElseGet(timestamp, Instant::now);
	}
	
	/**
	 * CAREFUL: <strong>"responseBody"</strong> generic type parameter MUST be an immutable object! <br>
	 * for List: we may use Java9+ factory methods of List interface <br>
	 * for example:<br>
	 * use: <code>List.copyOf(responseBody)</code> to create an immutable copy of the passed list in generic type.
	 */
	public ApiResponse(final Integer total, final Boolean acknowledge, final T responseBody) {
		this(Instant.now(), total, acknowledge, responseBody);
	}
	
	/**
	 * If <code>total == -1</code> then it means that <code>responseBody</code> is not a collection
	 * Otherwise, it is a collection (total >= 0)
	 * @param responseBody
	 * @return
	 * @param <T>
	 */
	public static <T> ApiResponse<T> of(final T responseBody) {
		return new ApiResponse<>(-1, true, responseBody);
	}
	
	/**
	 * * If <code>total == -1</code> then it means that <code>responseBody</code> is not a collection
	 * 	 * Otherwise, it is a collection (total >= 0)
	 * @param total
	 * @param responseBody
	 * @return
	 * @param <T>
	 */
	public static <T> ApiResponse<T> of(final int total, final T responseBody) {
		return new ApiResponse<>(total, true, responseBody);
	}
	
}



