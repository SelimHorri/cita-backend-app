package tn.cita.app.business.profile.customer.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
class CustomerProfileResource {
	
	@Qualifier("customerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final CustomerProfileService customerProfileService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<CustomerProfileResponse> fetchProfile(WebRequest request, @RequestParam Map<String, String> params) {
		return ApiResponse.of( 
				this.customerProfileService.fetchProfile(
						this.userRequestExtractorUtil.extractUsername(request),
						ClientPageRequest.from(params)));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<CustomerDto> updateProfileInfo(WebRequest webRequest,
											   @RequestBody @Valid CustomerProfileRequest customerProfileRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.customerProfileService.updateProfileInfo(customerProfileRequest));
	}
	
}




