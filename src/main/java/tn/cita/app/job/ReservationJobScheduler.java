package tn.cita.app.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.model.domain.ReservationStatus;
import tn.cita.app.model.domain.entity.Reservation;
import tn.cita.app.repository.ReservationRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationJobScheduler {
	
	private final ReservationRepository reservationRepository;
	
	/**
	 * Schedule daily update (at 00h:00min) for each NOT_STARTED reservation, switched to OUTDATED
	 * Runs on a separate Thread configurable from application.yml (profile)
	 */
	@Scheduled(cron = AppConstants.CRON_MIDNIGHT)
	@Transactional
	void scheduleOudatedReservations() {
		
		final var from = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
		final var to = from.plusDays(1);
		
		final long updatedReservationsCount = this.reservationRepository
				.findAllByStatusAndStartDateBetween(ReservationStatus.NOT_STARTED, from, to).stream()
					.map(this::markOutdated)
					.distinct()
					.map(this.reservationRepository::save)
					.peek(r -> log.info("Reservation with code {} has been switched to {}", 
							r.getCode(), r.getStatus().name()))
					.count();
		log.info("All {} not-started reservations of yesterday {} has been marked as OUTDATED at {}", 
				updatedReservationsCount, 
				from.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				Instant.now().atZone(ZoneId.systemDefault()));
	}
	
	/**
	 * Schedule daily update (at 00h:00min) for each IN_PROGRESS reservation, switched to NOT_CLOSED
	 * Runs on a separate Thread configurable from application.yml (profile)
	 */
	@Scheduled(cron = AppConstants.CRON_MIDNIGHT)
	@Transactional
	void scheduleNotClosedReservations() {
		
		final var from = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIDNIGHT);
		final var to = from.plusDays(1);
		
		final long updatedReservationsCount = this.reservationRepository
				.findAllByStatusAndStartDateBetween(ReservationStatus.IN_PROGRESS, from, to).stream()
					.map(this::markNotClosed)
					.distinct()
					.map(this.reservationRepository::save)
					.peek(r -> log.info("Reservation with code {} has been switched to {}", 
							r.getCode(), r.getStatus().name()))
					.count();
		log.info("All {} in-progress reservations of yesterday {} has been marked as NOT_CLOSED at {}", 
				updatedReservationsCount, 
				from.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				Instant.now().atZone(ZoneId.systemDefault()));
	}
	
	private Reservation markOutdated(final Reservation reservation) {
		reservation.setStatus(ReservationStatus.OUTDATED);
		return reservation;
	}
	
	private Reservation markNotClosed(final Reservation reservation) {
		reservation.setStatus(ReservationStatus.NOT_CLOSED);
		return reservation;
	}
	
}




