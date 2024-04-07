package tn.cita.app.business.category.employee.manager.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.category.employee.manager.model.CategoryRequest;
import tn.cita.app.business.category.employee.manager.service.ManagerCategoryService;
import tn.cita.app.model.dto.CategoryDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/managers/categories")
@RequiredArgsConstructor
class ManagerCategoryResource {
	
	@Qualifier("managerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final ManagerCategoryService managerCategoryService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<Page<CategoryDto>> fetchAll(WebRequest webRequest) {
		var categoriesDtos = this.managerCategoryService.fetchAll(
				this.userRequestExtractorUtil.extractUsername(webRequest));
		return ApiResponse.of(categoriesDtos.getSize(), categoriesDtos);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{categoryId}")
	ApiResponse<CategoryDto> fetchById(WebRequest webRequest,
									   @PathVariable String categoryId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(
				this.managerCategoryService.fetchById(Integer.parseInt(categoryId)));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<CategoryDto> createCategory(WebRequest webRequest,
											@RequestBody @Valid CategoryRequest categoryRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerCategoryService.saveCategory(categoryRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping
	ApiResponse<CategoryDto> updateCategory(WebRequest webRequest,
											@RequestBody @Valid CategoryRequest categoryRequest) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerCategoryService.updateCategory(categoryRequest));
	}
	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/{categoryId}")
	ApiResponse<Boolean> deleteCategory(WebRequest webRequest,
										@PathVariable String categoryId) {
		this.userRequestExtractorUtil.extractUsername(webRequest);
		return ApiResponse.of(this.managerCategoryService.deleteCategory(Integer.parseInt(categoryId)));
	}
	
}




