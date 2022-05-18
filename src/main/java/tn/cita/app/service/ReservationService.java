package tn.cita.app.service;

import java.util.List;

import tn.cita.app.dto.ReservationDto;
import tn.cita.app.dto.response.ReservationContainerResponse;

public interface ReservationService {
	
	List<ReservationDto> findAllByCustomerId(final Integer customerId);
	ReservationDto findById(final Integer id);
	ReservationDto findByCode(final String code);
	ReservationContainerResponse getReservationDetails(final Integer reservationId);
	ReservationContainerResponse updateReservationDetails(final ReservationContainerResponse reservationContainerResponse);
	ReservationDto cancelReservation(final ReservationDto reservationDtoRequest);
	
}







