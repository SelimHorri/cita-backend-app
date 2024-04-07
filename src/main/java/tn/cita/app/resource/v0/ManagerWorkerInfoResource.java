package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.model.dto.EmployeeDto;
import tn.cita.app.model.dto.response.ManagerWorkerInfoResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.ManagerWorkerInfoService;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/workers")
@RequiredArgsConstructor
class ManagerWorkerInfoResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerWorkerInfoService managerWorkerInfoService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<ManagerWorkerInfoResponse> fetchAllSubWorkers(WebRequest webRequest) {
		var extractUsername = this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerWorkerInfoService.fetchAllSubWorkers(extractUsername));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{workerId}")
	ApiResponse<EmployeeDto> fetchWorkerInfo(WebRequest webRequest,@PathVariable String workerId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerWorkerInfoService.fetchWorkerInfo(Integer.parseInt(workerId)));
	}
	
}



