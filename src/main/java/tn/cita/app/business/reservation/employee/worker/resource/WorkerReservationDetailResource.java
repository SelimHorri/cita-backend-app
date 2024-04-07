package tn.cita.app.business.reservation.employee.worker.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationDetailService;
import tn.cita.app.model.dto.response.ReservationDetailResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/workers/reservations/details")
@RequiredArgsConstructor
class WorkerReservationDetailResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerReservationDetailService workerReservationDetailService;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{reservationId}")
	ApiResponse<ReservationDetailResponse> fetchReservationDetails(WebRequest request, @PathVariable String reservationId) {
		this.userRequestExtractorUtil.extractUsername(request);
		return ApiResponse.of(this.workerReservationDetailService.fetchReservationDetails(Integer.parseInt(reservationId)));
	}
	
}




