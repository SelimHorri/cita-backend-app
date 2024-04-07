package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.TagDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.TagService;

import java.util.Map;

@RestController
@RequestMapping("${app.api-version}" + "/tags")
@RequiredArgsConstructor
class TagResource {
	
	private final TagService tagService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	ApiResponse<Page<TagDto>> findAll(@RequestParam Map<String, String> params) {
		var tags = this.tagService.findAll(ClientPageRequest.from(params));
		return ApiResponse.of(tags.getSize(), tags);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{id}")
	ApiResponse<TagDto> findById(@PathVariable String id) {
		return ApiResponse.of(this.tagService.findById(Integer.parseInt(id)));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<TagDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.tagService.findByIdentifier(identifier.strip()));
	}
	
}



