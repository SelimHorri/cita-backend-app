package tn.cita.app.service.v0.business.employee.worker;

import tn.cita.app.dto.TaskDto;
import tn.cita.app.dto.request.TaskBeginRequest;
import tn.cita.app.dto.response.ReservationDetailResponse;

public interface WorkerReservationDetailService {
	
	ReservationDetailResponse getReservationDetails(final Integer reservationId);
	TaskDto beginTask(final TaskBeginRequest taskBeginRequest);
	
}











