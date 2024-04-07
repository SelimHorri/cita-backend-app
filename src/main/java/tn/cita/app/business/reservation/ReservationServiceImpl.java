package tn.cita.app.business.reservation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.ReservationNotFoundException;
import tn.cita.app.mapper.ReservationMapper;
import tn.cita.app.model.dto.ReservationDto;
import tn.cita.app.model.dto.request.ClientPageRequest;
import tn.cita.app.repository.ReservationRepository;
import tn.cita.app.util.ClientPageRequestUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class ReservationServiceImpl implements ReservationService {
	
	private final ReservationRepository reservationRepository;
	
	@Override
	public Page<ReservationDto> findAllByCustomerId(final Integer customerId, final ClientPageRequest clientPageRequest) {
		log.info("Find all reservations by customerId.. *\n");
		return this.reservationRepository.findAllByCustomerId(customerId, 
					PageRequest.of(clientPageRequest.getOffset() - 1, clientPageRequest.getSize(), 
							Sort.by(clientPageRequest.getSortDirection(), clientPageRequest.getSortBy())))
				.map(ReservationMapper::toDto);
	}
	
	@Override
	public ReservationDto findById(final Integer id) {
		log.info("Find reservation by id.. *\n");
		return this.reservationRepository.findById(id)
				.map(ReservationMapper::toDto)
				.orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
	}
	
	@Override
	public ReservationDto findByIdentifier(final String identifier) {
		return this.reservationRepository.findByIdentifier(identifier.strip())
				.map(ReservationMapper::toDto)
				.orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));
	}
	
	@Override
	public ReservationDto findByCode(final String code) {
		log.info("Find reservation by code.. *\n");
		return this.reservationRepository.findByCode(code)
				.map(ReservationMapper::toDto)
				.orElseThrow(() -> new ReservationNotFoundException(String
						.format("Reservation with code: %s not found", code)));
	}
	
	@Override
	public Page<ReservationDto> findAllBySaloonId(final Integer saloonId, final ClientPageRequest clientPageRequest) {
		log.info("Find all paged reservations by saloonId.. *\n");
		return this.reservationRepository.findAllBySaloonId(saloonId, 
					ClientPageRequestUtils.from(clientPageRequest))
				.map(ReservationMapper::toDto);
	}
	
	@Override
	public List<ReservationDto> findAllBySaloonId(final Integer saloonId) {
		log.info("Find all reservations by saloonId.. *\n");
		return this.reservationRepository.findAllBySaloonId(saloonId).stream()
				.map(ReservationMapper::toDto)
				.distinct()
				.toList();
	}
	
}




