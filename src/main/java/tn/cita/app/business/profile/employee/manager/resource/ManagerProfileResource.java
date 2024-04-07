package tn.cita.app.business.profile.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.profile.employee.manager.model.ManagerProfileRequest;
import tn.cita.app.business.profile.employee.manager.model.ManagerProfileResponse;
import tn.cita.app.business.profile.employee.manager.service.ManagerProfileService;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/profile")
@RequiredArgsConstructor
class ManagerProfileResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerProfileService managerProfileService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<ManagerProfileResponse> fetchProfile(WebRequest webRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerProfileService.fetchProfile(extractUsername));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<EmployeeDto> updateProfileInfo(WebRequest webRequest,
											   @RequestBody @Valid ManagerProfileRequest managerProfileRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerProfileService.updateProfileInfo(managerProfileRequest));
	}
	
}




