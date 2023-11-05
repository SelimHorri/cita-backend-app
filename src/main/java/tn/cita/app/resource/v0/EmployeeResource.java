package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.EmployeeService;

@RestController
@RequestMapping("${app.api-version}" + "/employees")
@Slf4j
@RequiredArgsConstructor
public class EmployeeResource {
	
	private final EmployeeService employeeService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<EmployeeDto>> findById(@PathVariable final String id) {
		log.info("** Find employee by id.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.employeeService.findById(Integer.parseInt(id))));
	}
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<EmployeeDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find employee by identifier.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.employeeService.findByIdentifier(identifier)));
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<ApiResponse<EmployeeDto>> findByCredentialUsername(@PathVariable final String username) {
		log.info("** Find employee by credential username.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.employeeService.findByCredentialUsername(username)));
	}
	
	@GetMapping("/ssn/{ssn}")
	public ResponseEntity<ApiResponse<Page<EmployeeDto>>> findAllBySsn(@PathVariable final String ssn) {
		log.info("** Find employee(s) by ssn.. *");
		final var employeesDtos = new PageImpl<>(this.employeeService.findAllBySsn(ssn));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(employeesDtos.getSize(), employeesDtos));
	}
	
}




