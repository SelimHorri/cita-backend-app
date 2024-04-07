package tn.cita.app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cita.app.exception.wrapper.IllegalCredentialsException;
import tn.cita.app.exception.wrapper.IllegalUserDetailsStateException;
import tn.cita.app.repository.CredentialRepository;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
class CustomUserDetailsService implements UserDetailsService {
	
	private final CredentialRepository credentialRepository;
	
	@Override
	public UserDetails loadUserByUsername(final String username) {
		log.info("Load user by username..");
		
		final UserDetails userDetails = this.credentialRepository
				.findByUsernameIgnoreCase(username.strip().toLowerCase())
				.map(CustomUserDetails::new)
				.orElseThrow(() -> new IllegalCredentialsException("Username is not registered"));
		
		if (!userDetails.isEnabled())
			throw new IllegalUserDetailsStateException(
					"User with username: %s is disabled, checkout your mail to activate it".formatted(userDetails.getUsername()));
		if (!userDetails.isAccountNonExpired())
			throw new IllegalUserDetailsStateException(
					"User account with username: %s is expired".formatted(userDetails.getUsername()));
		if (!userDetails.isAccountNonLocked())
			throw new IllegalUserDetailsStateException("User account with username: %s is locked".formatted(userDetails.getUsername()));
		if (!userDetails.isCredentialsNonExpired())
			throw new IllegalUserDetailsStateException(
					"User credentials with username: %s are expired".formatted(userDetails.getUsername()));
		
		return this.credentialRepository.findByUsernameIgnoreCase(username.strip().toLowerCase())
				.map(CustomUserDetails::new)
				.orElseThrow(() -> new IllegalCredentialsException("Username is not registered"));
	}
	
}



