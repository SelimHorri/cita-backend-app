package tn.cita.app.resource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tn.cita.app.exception.wrapper.BusinessException;
import tn.cita.app.exception.wrapper.ObjectNotFoundException;
import tn.cita.app.model.dto.response.api.ApiResponse;

import java.net.URI;

@RestControllerAdvice
@Slf4j
class ApiExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	<T extends MethodArgumentNotValidException> ResponseEntity<ApiResponse<ProblemDetail>> handleValidationException(
																							T e, HttpServletRequest request) {
		log.info("Handling validation exception..");
		
		record InvalidField(String fieldName, String reason) {}
		
		var invalidFields = e.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> new InvalidField(
						fieldError.getField(),
						fieldError.getDefaultMessage()))
				.toList();
		
		var problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST, 
				"#### %s! ####".formatted("There are validation errors"));
		problemDetail.setType(URI.create(e.getClass().getSimpleName()));
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		problemDetail.setProperty("invalidFields", invalidFields);
		log.error("Processing problemDetail: {}", problemDetail, e);
		
		var apiResponse = new ApiResponse<>(
				-1, false, problemDetail);
		return ResponseEntity.status(problemDetail.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiResponse);
	}
	
	@ExceptionHandler(ObjectNotFoundException.class)
	<T extends ObjectNotFoundException> ResponseEntity<ApiResponse<ProblemDetail>> handleObjectNotFoundException(
																				T e, HttpServletRequest request) {
		log.info("Handling not-found exception..");
		
		var problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.NOT_FOUND, 
				"#### %s! ####".formatted(e.getMessage()));
		problemDetail.setType(URI.create(e.getClass().getSimpleName()));
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		log.error("Processing problemDetail: {}", problemDetail, e);
		
		var apiResponse = new ApiResponse<>(
				-1, false, problemDetail);
		
		return ResponseEntity.status(problemDetail.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiResponse);
	}
	
	@ExceptionHandler(BusinessException.class)
	<T extends BusinessException> ResponseEntity<ApiResponse<ProblemDetail>> handleBusinessException(
																				T e, HttpServletRequest request) {
		log.info("Handling business exception..");
		
		var problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.BAD_REQUEST, 
				"#### %s! ####".formatted(e.getMessage()));
		problemDetail.setType(URI.create(e.getClass().getSimpleName()));
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		log.error("Processing problemDetail: {}", problemDetail, e);
		
		var apiResponse = new ApiResponse<>(
				-1, false, problemDetail);
		
		return ResponseEntity.status(problemDetail.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(apiResponse);
	}
	
	@ExceptionHandler
	ResponseEntity<ApiResponse<ProblemDetail>> handleException(HttpServletRequest request, Exception e) {
		log.info("Handling general exception..");
		
		var problemDetail = ProblemDetail.forStatusAndDetail(
				HttpStatus.INTERNAL_SERVER_ERROR, 
				"#### %s! ####".formatted("Service temporarily unavailable please try later"));
		problemDetail.setType(URI.create(e.getClass().getSimpleName()));
		problemDetail.setInstance(URI.create(request.getRequestURI()));
		problemDetail.setProperty("defaultMessage", e.getMessage());
		log.error("Processing problemDetail: {}", problemDetail, e);
		
		return ResponseEntity.status(problemDetail.getStatus())
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ApiResponse<>(-1, false, problemDetail));
	}
	
}



