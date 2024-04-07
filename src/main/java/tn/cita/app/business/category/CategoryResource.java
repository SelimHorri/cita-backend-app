package tn.cita.app.business.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.CategoryDto;
import tn.cita.app.model.dto.response.api.ApiResponse;

@RestController
@RequestMapping("${app.api-version}" + "/categories")
@RequiredArgsConstructor
class CategoryResource {
	
	private final CategoryService categoryService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<CategoryDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.categoryService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{saloonId}")
	ApiResponse<Page<CategoryDto>> findAllBySaloonId(@PathVariable String saloonId) {
		final var categoriesDtos = new PageImpl<>(this.categoryService.findAllBySaloonId(Integer.parseInt(saloonId)));
		return ApiResponse.of(categoriesDtos.getSize(), categoriesDtos);
	}
	
}




