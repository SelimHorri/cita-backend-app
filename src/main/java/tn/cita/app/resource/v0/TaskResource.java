package tn.cita.app.resource.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.cita.app.model.dto.TaskDto;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.service.TaskService;

@RestController
@RequestMapping("${app.api-version}" + "/tasks")
@RequiredArgsConstructor
class TaskResource {
	
	private final TaskService taskService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/identifier/{identifier}")
	ApiResponse<TaskDto> findByIdentifier(@PathVariable String identifier) {
		return ApiResponse.of(this.taskService.findByIdentifier(identifier.strip()));
	}
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/reservationId/{reservationId}")
	ApiResponse<Page<TaskDto>> findAllByReservationId(@PathVariable String reservationId) {
		var tasks = new PageImpl<>(this.taskService.findAllByReservationId(Integer.parseInt(reservationId)));
		return ApiResponse.of(tasks.getSize(), tasks);
	}
	
}



