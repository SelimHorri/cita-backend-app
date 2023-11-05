package tn.cita.app.business.profile.customer.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.profile.customer.model.CustomerProfileRequest;
import tn.cita.app.business.profile.customer.model.CustomerProfileResponse;
import tn.cita.app.business.profile.customer.service.CustomerProfileService;
import tn.cita.app.model.dto.CustomerDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/customers/profile")
@Slf4j
@RequiredArgsConstructor
public class CustomerProfileResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerProfileService customerProfileService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<CustomerProfileResponse>> fetchProfile(
					final WebRequest request, @RequestParam final Map<String, String> params) {
		log.info("** Fetch customer profile info.. *\n");
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.customerProfileService.fetchProfile(
						this.userRequestExtractorUtil.extractUsername(request),
						ClientPageRequest.from(params))));
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse<CustomerDto>> updateProfileInfo(
					final WebRequest webRequest, @RequestBody @Valid final CustomerProfileRequest customerProfileRequest) {
		log.info("** Update customer profile info.. *\n");
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.customerProfileService.updateProfileInfo(customerProfileRequest)));
	}
	
}




