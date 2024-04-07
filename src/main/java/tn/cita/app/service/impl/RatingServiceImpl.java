package tn.cita.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.mapper.RatingMapper;
import tn.cita.app.model.dto.RatingDto;
import tn.cita.app.repository.RatingRepository;
import tn.cita.app.service.RatingService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class RatingServiceImpl implements RatingService {
	
	private final RatingRepository ratingRepository;
	
	@Override
	public List<RatingDto> findAllByCustomerId(final Integer customerId) {
		log.info("Find all ratings by customerId.. ");
		return this.ratingRepository
				.findAllByCustomerId(customerId).stream()
					.map(RatingMapper::toDto)
					.toList();
	}
	
}



