package tn.cita.app.business.reservation.employee.worker.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import tn.cita.app.business.reservation.employee.worker.service.WorkerReservationDetailService;
import tn.cita.app.model.dto.response.ReservationDetailResponse;
import tn.cita.app.model.dto.response.api.ApiResponse;
import tn.cita.app.util.UserRequestExtractorUtil;

@RestController
@RequestMapping("${app.api-version}" + "/employees/workers/reservations/details")
@Slf4j
@RequiredArgsConstructor
public class WorkerReservationDetailResource {
	
	@Qualifier("workerRequestExtractorUtil")
	private final UserRequestExtractorUtil userRequestExtractorUtil;
	private final WorkerReservationDetailService workerReservationDetailService;
	
	@GetMapping("/{reservationId}")
	public ResponseEntity<ApiResponse<ReservationDetailResponse>> fetchReservationDetails(
					final WebRequest request, @PathVariable final String reservationId) {
		log.info("** Fetch worker reservation details.. *");
		this.userRequestExtractorUtil.extractUsername(request);
		return ResponseEntity.ok(ApiResponse.of2xxMono(
				this.workerReservationDetailService.fetchReservationDetails(Integer.parseInt(reservationId))));
	}
	
}




