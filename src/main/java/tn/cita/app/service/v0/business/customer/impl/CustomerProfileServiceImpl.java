package tn.cita.app.service.v0.business.customer.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tn.cita.app.dto.CustomerDto;
import tn.cita.app.dto.request.ClientPageRequest;
import tn.cita.app.dto.request.CustomerProfileRequest;
import tn.cita.app.dto.response.CustomerProfileResponse;
import tn.cita.app.exception.wrapper.CustomerNotFoundException;
import tn.cita.app.exception.wrapper.PasswordNotMatchException;
import tn.cita.app.exception.wrapper.UsernameAlreadyExistsException;
import tn.cita.app.mapper.CustomerMapper;
import tn.cita.app.mapper.FavouriteMapper;
import tn.cita.app.mapper.RatingMapper;
import tn.cita.app.mapper.ReservationMapper;
import tn.cita.app.repository.CustomerRepository;
import tn.cita.app.repository.FavouriteRepository;
import tn.cita.app.repository.RatingRepository;
import tn.cita.app.repository.ReservationRepository;
import tn.cita.app.service.v0.business.customer.CustomerProfileService;
import tn.cita.app.util.ClientRequestUtils;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {
	
	private final CustomerRepository customerRepository;
	private final ReservationRepository reservationRepository;
	private final FavouriteRepository favouriteRepository;
	private final RatingRepository ratingRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public CustomerProfileResponse fetchProfile(final String username, final ClientPageRequest clientPageRequest) {
		log.info("** Fetch customer profile.. *\n");
		final var customerDto = this.customerRepository
				.findByCredentialUsernameIgnoringCase(username)
				.map(CustomerMapper::map)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		return new CustomerProfileResponse(
				customerDto, 
				null, 
				this.reservationRepository.findAllByCustomerId(customerDto.getId(), 
						ClientRequestUtils.from(clientPageRequest))
					.map(ReservationMapper::map), 
					this.favouriteRepository.findAllByCustomerId(customerDto.getId(), 
							ClientRequestUtils.from(clientPageRequest))
						.map(FavouriteMapper::map),
				new PageImpl<>(this.ratingRepository.findAllByCustomerId(customerDto.getId()).stream()
						.map(RatingMapper::map)
						.distinct()
						.collect(Collectors.toUnmodifiableList())));
	}
	
	@Transactional
	@Override
	public CustomerDto updateProfileInfo(final CustomerProfileRequest customerProfileRequest) {
		
		log.info("** Update customer profile.. *\n");
		
		this.customerRepository
				.findByCredentialUsernameIgnoringCase(customerProfileRequest.getUsername().strip()).ifPresent(c -> {
			if (!c.getCredential().getUsername().equals(customerProfileRequest.getAuthenticatedUsername()))
				throw new UsernameAlreadyExistsException("Username already exists, please choose another");
		});
		
		if (!customerProfileRequest.getPassword().equals(customerProfileRequest.getConfirmPassword()))
			throw new PasswordNotMatchException("Passwords are not matched.. please confirm");
		
		final var authenticatedCustomer = this.customerRepository
				.findByCredentialUsernameIgnoringCase(customerProfileRequest.getAuthenticatedUsername())
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
		authenticatedCustomer.setFirstname(customerProfileRequest.getFirstname().strip());
		authenticatedCustomer.setLastname(customerProfileRequest.getLastname().strip());
		authenticatedCustomer.setEmail(customerProfileRequest.getEmail().strip());
		authenticatedCustomer.setPhone(customerProfileRequest.getPhone().strip());
		authenticatedCustomer.setBirthdate(customerProfileRequest.getBirthdate());
		authenticatedCustomer.setFacebookUrl(customerProfileRequest.getFacebookUrl().strip());
		authenticatedCustomer.setInstagramUrl(customerProfileRequest.getInstagramUrl().strip());
		authenticatedCustomer.setLinkedinUrl(customerProfileRequest.getLinkedinUrl().strip());
		authenticatedCustomer.getCredential().setUsername(customerProfileRequest.getUsername().strip().toLowerCase());
		authenticatedCustomer.getCredential().setPassword(this.passwordEncoder.encode(customerProfileRequest.getPassword()));
		
		return CustomerMapper.map(this.customerRepository.save(authenticatedCustomer));
	}
	
	
	
}
















