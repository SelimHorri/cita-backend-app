package tn.cita.app.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tn.cita.app.exception.payload.ExceptionMsg;
import tn.cita.app.exception.wrapper.ActuatorHealthException;
import tn.cita.app.exception.wrapper.BusinessException;
import tn.cita.app.model.dto.response.api.ApiResponse;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public <T extends MethodArgumentNotValidException> ResponseEntity<ApiResponse<ExceptionMsg>> handleValidationException(final T e) {
		log.info("** Handle validation exception.. *");
		
		final var fieldError = Objects.requireNonNullElseGet(e.getBindingResult().getFieldError(), 
				() -> new FieldError(null, null, "Validation error happened, check again"));
		
		final var httpStatus = HttpStatus.BAD_REQUEST;
		final var exceptionMsg = new ExceptionMsg("*%s!**".formatted(fieldError.getDefaultMessage()));
		final var apiResponse = new ApiResponse<>(1, httpStatus, false, exceptionMsg);
		
		return ResponseEntity.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiResponse);
	}
	
	@ExceptionHandler(value = {
		BusinessException.class,
		BadCredentialsException.class,
		IllegalStateException.class,
		DisabledException.class,
		NumberFormatException.class,
		// UnauthorizedUserException.class, // already works for filter using resolver
		ActuatorHealthException.class,
	})
	public <T extends RuntimeException> ResponseEntity<ApiResponse<ExceptionMsg>> handleApiRequestException(final T e) {
		log.info("** Handle API request custom exception.. *");
		
		final var httpStatus = HttpStatus.BAD_REQUEST;
		final var exceptionMsg = new ExceptionMsg("#### %s! ####".formatted(e.getMessage()));
		final var apiResponse = new ApiResponse<>(1, httpStatus, false, exceptionMsg);
		
		return ResponseEntity.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiResponse);
	}
	
	@ExceptionHandler
	public ResponseEntity<ApiResponse<ProblemDetail>> handleException(final Exception e) {
		log.info("** Handle API request custom exception.. *");
		
		final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		problemDetail.setProperty("errorMsg", problemDetail.getDetail());
		
		return ResponseEntity.status(problemDetail.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(ApiResponse.of5xxMono(problemDetail));
	}
	
}




