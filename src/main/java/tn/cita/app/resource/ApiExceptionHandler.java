package tn.cita.app.resource;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.exception.wrapper.ActuatorHealthException;
import tn.cita.app.exception.wrapper.CustomRuntimeException;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
	
	@ExceptionHandler(value = {
		MethodArgumentNotValidException.class,
		HttpMessageNotReadableException.class,
		ConstraintViolationException.class,
	})
	public <T extends BindException> ResponseEntity<ApiResponse<ExceptionMsg>> handleValidationException(final T e, 
			final WebRequest webRequest) {
		
		log.info("** Handle validation exception.. *\n");
		
		final var fieldError = Optional
				.ofNullable(e.getBindingResult().getFieldError())
				.orElseGet(() -> new FieldError(null, null, "Validation error happened, check again"));
		
		final var httpStatus = HttpStatus.BAD_REQUEST;
		final var exceptionMsg = new ExceptionMsg("*%s!**".formatted(fieldError.getDefaultMessage()));
		final var apiPayloadResponse = new ApiResponse<>(1, httpStatus, false, exceptionMsg);
		
		return ResponseEntity.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiPayloadResponse);
	}
	
	@ExceptionHandler(value = {
		CustomRuntimeException.class,
		NoSuchElementException.class,
		BadCredentialsException.class,
		IllegalStateException.class,
		DisabledException.class,
		NumberFormatException.class,
		// UnauthorizedUserException.class, // already works for filter using resolver
		ActuatorHealthException.class,
	})
	public <T extends RuntimeException> ResponseEntity<ApiResponse<ExceptionMsg>> handleApiRequestException(final T e, 
			final WebRequest webRequest) {
		log.info("** Handle API request custom exception.. *\n");
		
		final var httpStatus = HttpStatus.BAD_REQUEST;
		final var exceptionMsg = new ExceptionMsg("#### %s! ####".formatted(e.getMessage()));
		final var apiPayloadResponse = new ApiResponse<>(1, httpStatus, false, exceptionMsg);
		
		return ResponseEntity.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiPayloadResponse);
	}
	
}









