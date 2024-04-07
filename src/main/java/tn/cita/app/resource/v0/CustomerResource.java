package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.CustomerDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.CustomerService;

@RestController
@RequestMapping("${app.api-version}" + "/customers")
@RequiredArgsConstructor
class CustomerResource {
	
	private final CustomerService customerService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<CustomerDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.customerService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<CustomerDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.customerService.findByIdentifier(identifier));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/username/{username}")
	ApiResponse<CustomerDto> findByCredentialUsername(@PathVariable String username) {
		return ApiResponse.of(this.customerService.findByCredentialUsername(username));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/ssn/{ssn}")
	ApiResponse<Page<CustomerDto>> findAllBySsn(@PathVariable String ssn) {
		var customers = new PageImpl<>(this.customerService.findAllBySsn(ssn));
		return ApiResponse.of(customers.getSize(), customers);
	}
	
}



