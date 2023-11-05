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
import tn.cita.app.model.dto.CustomerDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.CustomerService;

@RestController
@RequestMapping("${app.api-version}" + "/customers")
@Slf4j
@RequiredArgsConstructor
public class CustomerResource {
	
	private final CustomerService customerService;
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CustomerDto>> findById(@PathVariable final String id) {
		log.info("** Find customer by id.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.customerService.findById(Integer.parseInt(id))));
	}
	
	@GetMapping("/identifier/{identifier}")
	public ResponseEntity<ApiResponse<CustomerDto>> findByIdentifier(@PathVariable final String identifier) {
		log.info("** Find customer by identifier.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.customerService.findByIdentifier(identifier)));
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<ApiResponse<CustomerDto>> findByCredentialUsername(@PathVariable final String username) {
		log.info("** Find customer by credential username.. *");
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.customerService.findByCredentialUsername(username)));
	}
	
	@GetMapping("/ssn/{ssn}")
	public ResponseEntity<ApiResponse<Page<CustomerDto>>> findAllBySsn(@PathVariable final String ssn) {
		log.info("** Find customer(s) by ssn.. *");
		final var customersDtos = new PageImpl<>(this.customerService.findAllBySsn(ssn));
		return ResponseEntity.ok(ApiResponse.of2xxPoly(customersDtos.getSize(), customersDtos));
	}
	
}




