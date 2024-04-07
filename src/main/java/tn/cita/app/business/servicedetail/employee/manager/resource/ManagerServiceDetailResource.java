package tn.cita.app.business.servicedetail.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.servicedetail.employee.manager.model.ServiceDetailRequest;
import tn.cita.app.business.servicedetail.employee.manager.service.ManagerServiceDetailService;
import tn.cita.app.model.dto.ServiceDetailDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/service-details")
@RequiredArgsConstructor
class ManagerServiceDetailResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerServiceDetailService managerServiceDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<Page<ServiceDetailDto>> fetchAll(WebRequest webRequest) {
		var serviceDetailsDtos = this.managerServiceDetailService.fetchAll(
				this.userRequestExtractorUtil.extractUsername(webRequest));
		return ApiResponse.of(serviceDetailsDtos.getSize(), serviceDetailsDtos);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{serviceDetailId}")
	ApiResponse<ServiceDetailDto> fetchById(WebRequest webRequest, @PathVariable String serviceDetailId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerServiceDetailService.fetchById(Integer.parseInt(serviceDetailId)));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<ServiceDetailDto> createServiceDetail(WebRequest webRequest,
													  @RequestBody @Valid ServiceDetailRequest serviceDetailRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerServiceDetailService.saveServiceDetail(serviceDetailRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<ServiceDetailDto> updateServiceDetail(WebRequest webRequest,
													  @RequestBody @Valid ServiceDetailRequest serviceDetailRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerServiceDetailService.updateServiceDetail(serviceDetailRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/{serviceDetailId}")
	ApiResponse<Boolean> deleteServiceDetail(final WebRequest webRequest, @PathVariable String serviceDetailId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerServiceDetailService.deleteServiceDetail(Integer.parseInt(serviceDetailId)));
	}
	
}




