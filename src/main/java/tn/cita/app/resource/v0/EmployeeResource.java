package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.EmployeeService;

@RestController
@RequestMapping("${app.api-version}" + "/employees")
@RequiredArgsConstructor
class EmployeeResource {
	
	private final EmployeeService employeeService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<EmployeeDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.employeeService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<EmployeeDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.employeeService.findByIdentifier(identifier));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/username/{username}")
	ApiResponse<EmployeeDto> findByCredentialUsername(@PathVariable String username) {
		return ApiResponse.of(this.employeeService.findByCredentialUsername(username));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/ssn/{ssn}")
	ApiResponse<Page<EmployeeDto>> findAllBySsn(@PathVariable String ssn) {
		var employeesDtos = new PageImpl<>(this.employeeService.findAllBySsn(ssn));
		return ApiResponse.of(employeesDtos.getSize(), employeesDtos);
	}
	
}



