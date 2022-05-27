package tn.cita.app.service;

import org.springframework.data.domain.Page;

import tn.cita.app.dto.CustomerDto;
import tn.cita.app.dto.request.ClientPageRequest;
import tn.cita.app.dto.response.CustomerContainerResponse;

public interface CustomerService {
	
	Page<CustomerDto> findAll(final int pageOffset);
	CustomerDto findById(final Integer id);
	CustomerDto findByCredentialUsernameIgnoringCase(final String username);
	boolean deleteById(final Integer id);
	CustomerContainerResponse getProfileByUsername(final String username, final ClientPageRequest clientPageRequest);
	CustomerContainerResponse getFavouritesByUsername(final String username, final ClientPageRequest clientPageRequest);
	CustomerContainerResponse getReservationsByUsername(final String username, final ClientPageRequest clientPageRequest);
	CustomerContainerResponse getRatingsByUsername(final String username);
	Boolean deleteFavourite(final String username, final Integer saloonId);
	
}













