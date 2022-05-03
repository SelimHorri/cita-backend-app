package tn.cita.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tn.cita.app.constant.AppConstant;
import tn.cita.app.dto.CustomerDto;
import tn.cita.app.dto.response.CustomerContainerResponse;
import tn.cita.app.exception.wrapper.CustomerNotFoundException;
import tn.cita.app.mapper.CustomerMapper;
import tn.cita.app.repository.CustomerRepository;
import tn.cita.app.service.CustomerService;
import tn.cita.app.service.FavouriteService;
import tn.cita.app.service.RatingService;
import tn.cita.app.service.ReservationService;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;
	private final ReservationService reservationService;
	private final FavouriteService favouriteService;
	private final RatingService ratingService;
	
	@Transactional(readOnly = true)
	@Override
	public List<CustomerDto> findAll(final int pageOffset) {
		log.info("** CustomerServiceImpl; List CustomerDto; find All with pageOffset service...*\n");
		return this.customerRepository.findAll(PageRequest.of(pageOffset - 1, AppConstant.PAGE_SIZE))
				.stream()
					.map(CustomerMapper::map)
					.distinct()
					.collect(Collectors.toUnmodifiableList());
	}
	
	@Transactional(readOnly = true)
	@Override
	public CustomerDto findById(final Integer id) {
		log.info("** CustomerServiceImpl; CustomerDto; find user by id service...*\n");
		return this.customerRepository.findById(id)
				.map(CustomerMapper::map)
				.orElseThrow(() -> new CustomerNotFoundException(String
						.format("Customer with id: %d not found", id)));
	}
	
	@Override
	public boolean deleteById(final Integer id) {
		log.info("** CustomerServiceImpl; boolean; delete user by id service...*\n");
		this.customerRepository.deleteById(id);
		return !this.customerRepository.existsById(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public CustomerDto findByCredentialUsernameIgnoringCase(final String username) {
		return this.customerRepository.findByCredentialUsernameIgnoringCase(username)
				.map(CustomerMapper::map)
				.orElseThrow(() -> new CustomerNotFoundException(String
						.format("Customer with username: %s not found", username)));
	}
	
	@Transactional(readOnly = true)
	@Override
	public CustomerContainerResponse getProfileByUsername(final String username) {
		
		final var customerDto = this.findByCredentialUsernameIgnoringCase(username);
		
		return new CustomerContainerResponse(
				this.findByCredentialUsernameIgnoringCase(username), 
				customerDto.getCredentialDto(), 
				this.reservationService.findAllByCustomerId(customerDto.getId()), 
				this.favouriteService.findAllByCustomerId(customerDto.getId()),
				this.ratingService.findAllByCustomerId(customerDto.getId()));
	}
	
	@Transactional(readOnly = true)
	@Override
	public CustomerContainerResponse getFavouritesByUsername(final String username) {

		final var customerDto = this.findByCredentialUsernameIgnoringCase(username);
		
		return CustomerContainerResponse.builder()
				.customerDto(customerDto)
				.credentialDto(customerDto.getCredentialDto())
				.favouriteDtos(this.favouriteService.findAllByCustomerId(customerDto.getId()))
				.build();
	}
	
	
	
}














