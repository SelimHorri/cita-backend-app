package tn.cita.app.business.profile.employee.worker.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.profile.employee.worker.model.WorkerProfileRequest;
import tn.cita.app.business.profile.employee.worker.model.WorkerProfileResponse;
import tn.cita.app.business.profile.employee.worker.service.WorkerProfileService;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/workers/profile")
@RequiredArgsConstructor
class WorkerProfileResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerProfileService workerProfileService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<WorkerProfileResponse> fetchProfile(WebRequest webRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.workerProfileService.fetchProfile(extractUsername));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<EmployeeDto> updateProfileInfo(WebRequest webRequest, 
			@RequestBody @Valid WorkerProfileRequest workerProfileRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.workerProfileService.updateProfileInfo(workerProfileRequest));
	}
	
}




