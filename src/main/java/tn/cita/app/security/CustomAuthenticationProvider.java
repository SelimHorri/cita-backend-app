package tn.cita.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.cita.app.exception.wrapper.IllegalUserDetailsStateException;
import tn.cita.app.exception.wrapper.PasswordNotMatchException;

// @Service
@RequiredArgsConstructor
class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		var userDetails = this.userDetailsService.loadUserByUsername(authentication.getName());
		
		if (!this.passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword()))
			throw new PasswordNotMatchException("Incorrect password");
		
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
		
		return new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(),
				userDetails.getPassword(),
				userDetails.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
}



