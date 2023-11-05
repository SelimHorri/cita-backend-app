package tn.cita.app.business.profile.employee.worker.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequiredArgsConstructor
public class WorkerProfileResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerProfileService workerProfileService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<WorkerProfileResponse>> fetchProfile(final WebRequest webRequest) {
		log.info("** Fetch worker profile info.. *\n");
		return ResponseEntity.ok(ApiResponse.of2xxMono( 
				this.workerProfileService.fetchProfile(this.userRequestExtractorUtil.extractUsername(webRequest))));
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse<EmployeeDto>> updateProfileInfo(final WebRequest webRequest, 
			@RequestBody @Valid final WorkerProfileRequest workerProfileRequest) {
		log.info("** Update worker profile info.. *\n");
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ResponseEntity.ok(ApiResponse.of2xxMono(this.workerProfileService.updateProfileInfo(workerProfileRequest)));
	}
	
}




