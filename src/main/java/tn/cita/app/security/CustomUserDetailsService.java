package tn.cita.app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.IllegalCredentialsException;
import tn.cita.app.exception.wrapper.IllegalUserDetailsStateException;
import tn.cita.app.mapper.CredentialMapper;
import tn.cita.app.repository.CredentialRepository;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class CustomUserDetailsService implements UserDetailsService {
	
	private final CredentialRepository credentialRepository;
	
	@Override
	public UserDetails loadUserByUsername(final String username) {
		log.info("** Load user by username.. *");
		
		final UserDetails userDetails = new CustomUserDetails(
				this.credentialRepository
						.findByUsernameIgnoreCase(username.strip().toLowerCase())
						.map(CredentialMapper::toDto)
						.orElseThrow(() -> new IllegalCredentialsException("Username is not registered")));
		
		if (!userDetails.isEnabled())
			throw new IllegalUserDetailsStateException(String
					.format("User with username: %s is disabled, checkout your mail to activate it", userDetails.getUsername()));
		if (!userDetails.isAccountNonExpired())
			throw new IllegalUserDetailsStateException(String
					.format("User account with username: %s is expired", userDetails.getUsername()));
		if (!userDetails.isAccountNonLocked())
			throw new IllegalUserDetailsStateException(String
					.format("User account with username: %s is locked", userDetails.getUsername()));
		if (!userDetails.isCredentialsNonExpired())
			throw new IllegalUserDetailsStateException(String
					.format("User credentials with username: %s are expired", userDetails.getUsername()));
		
		return userDetails;
	}
	
}



