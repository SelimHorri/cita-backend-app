package tn.cita.app.business.rating.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.business.rating.customer.model.CustomerRatingResponse;
import tn.cita.app.business.rating.customer.service.CustomerRatingService;
import tn.cita.app.exception.wrapper.CustomerNotFoundException;
import tn.cita.app.mapper.CustomerMapper;
import tn.cita.app.mapper.RatingMapper;
import tn.cita.app.repository.CustomerRepository;
import tn.cita.app.repository.RatingRepository;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class CustomerRatingServiceImpl implements CustomerRatingService {
	
	private final CustomerRepository customerRepository;
	private final RatingRepository ratingRepository;
	
	@Override
	public CustomerRatingResponse fetchAllRatings(final String username) {
		log.info("Fetch all ratings by customer.. *\n");
		final var customerDto = this.customerRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(CustomerMapper::toDto)
				.orElseThrow(() ->
						new CustomerNotFoundException("Customer with username: %s not found".formatted(username)));
		return new CustomerRatingResponse(
				customerDto,
				new PageImpl<>(this.ratingRepository
						.findAllByCustomerId(customerDto.getId()).stream()
							.map(RatingMapper::toDto)
							.toList()));
	}
	
}





