package tn.cita.app.service.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import tn.cita.app.exception.wrapper.CredentialNotFoundException;
import tn.cita.app.exception.wrapper.IllegalUserDetailsStateException;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.model.domain.entity.Credential;
import tn.cita.app.repository.CredentialRepository;
import tn.cita.app.security.CustomUserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomUserDetailsServiceTest {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@MockBean
	private CredentialRepository credentialRepository;
	
	@Disabled
	@Test
	void givenValidUsername_whenLoadUserByUsername_thenUserDetailsShouldBeFound() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorri")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(true)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
				.thenReturn(Optional.of(credential));
		
		final var expectedUserDetails = new CustomUserDetails(credential);
		final var userDetails = this.userDetailsService.loadUserByUsername(credential.getUsername());
		
		assertThat(userDetails)
				.isNotNull()
				.isInstanceOf(UserDetails.class);
		assertThat(userDetails.getUsername()).isEqualTo(expectedUserDetails.getUsername());
		assertThat(userDetails.getAuthorities()).allSatisfy(sga -> assertThat(sga.getAuthority())
				.isEqualTo(credential.getUserRoleBasedAuthority().getRole()));
		assertThat(userDetails.isEnabled()).isTrue();
		assertThat(userDetails.isAccountNonExpired()).isTrue();
		assertThat(userDetails.isAccountNonLocked()).isTrue();
		assertThat(userDetails.isCredentialsNonExpired()).isTrue();
	}
	
	@Disabled
	@Test
	void givenInvalidUsername_whenLoadUserByUsername_thenShouldThrowCredentialNotFoundException() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorrishdkfbzkih")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(true)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
				.thenThrow(CredentialNotFoundException.class);
		
		final var credentialNotFoundException = assertThrows(CredentialNotFoundException.class, 
				() -> this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()));
		
		assertThat(credentialNotFoundException)
				.isNotNull()
				.isInstanceOf(CredentialNotFoundException.class);
	}
	
	@Disabled
	@Test
	void givenValidUsername_whenLoadUserByUsernameAndUserIsDisabled_thenIllegalUserDetailsStateExceptionShouldBeThrown() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorrishdkfbzkih")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(false)
				.isAccountNonExpired(true)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
				.thenReturn(Optional.of(credential));
		
		final var userDetails = new CustomUserDetails(credential);
		
		final var illegalUserDetailsStateException = assertThrows(IllegalUserDetailsStateException.class, 
				() -> this.userDetailsService.loadUserByUsername(userDetails.getUsername()));
		
		assertThat(illegalUserDetailsStateException)
				.isNotNull()
				.isInstanceOf(IllegalUserDetailsStateException.class);
		assertThat(illegalUserDetailsStateException.getMessage())
				.startsWith("User ")
				.endsWith(" to activate it")
				.isEqualTo(String
						.format("User with username: %s is disabled, checkout your mail to activate it", 
								userDetails.getUsername()));
	}
	
	@Disabled
	@Test
	void givenValidUsername_whenLoadUserByUsernameAndUserAccountIsExpired_thenIllegalUserDetailsStateExceptionShouldBeThrown() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorrishdkfbzkih")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(false)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
				.thenReturn(Optional.of(credential));
		
		final UserDetails userDetails = new CustomUserDetails(credential);
		
		final var illegalUserDetailsStateException = assertThrows(IllegalUserDetailsStateException.class, 
				() -> this.userDetailsService.loadUserByUsername(userDetails.getUsername()));
		
		assertThat(illegalUserDetailsStateException)
				.isNotNull()
				.isInstanceOf(IllegalUserDetailsStateException.class);
		assertThat(illegalUserDetailsStateException.getMessage())
				.startsWith("User ")
				.endsWith(" is expired")
				.isEqualTo(String.format("User account with username: %s is expired", userDetails.getUsername()));
	}
	
	@Disabled
	@Test
	void givenValidUsername_whenLoadUserByUsernameAndUserAccountIsLocked_thenIllegalUserDetailsStateExceptionShouldBeThrown() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorrishdkfbzkih")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(true)
				.isAccountNonLocked(false)
				.isCredentialsNonExpired(true)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
		.thenReturn(Optional.of(credential));
		
		final UserDetails userDetails = new CustomUserDetails(credential);
		
		final var illegalUserDetailsStateException = assertThrows(IllegalUserDetailsStateException.class, 
				() -> this.userDetailsService.loadUserByUsername(userDetails.getUsername()));
		
		assertThat(illegalUserDetailsStateException)
				.isNotNull()
				.isInstanceOf(IllegalUserDetailsStateException.class);
		assertThat(illegalUserDetailsStateException.getMessage())
				.startsWith("User ")
				.endsWith(" is locked")
				.isEqualTo(String.format("User account with username: %s is locked", userDetails.getUsername()));
	}
	
	@Disabled
	@Test
	void givenValidUsername_whenLoadUserByUsernameAndUserCredentialsAreExpired_thenIllegalUserDetailsStateExceptionShouldBeThrown() {
		
		final var credential = Credential.builder()
				.id(null)
				.username("selimhorrishdkfbzkih")
				.userRoleBasedAuthority(UserRoleBasedAuthority.WORKER)
				.isEnabled(true)
				.isAccountNonExpired(true)
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(false)
				.build();
		
		when(this.credentialRepository.findByUsernameIgnoreCase(credential.getUsername()))
				.thenReturn(Optional.of(credential));
		
		final UserDetails userDetails = new CustomUserDetails(credential);
		
		final var illegalUserDetailsStateException = assertThrows(IllegalUserDetailsStateException.class, 
				() -> this.userDetailsService.loadUserByUsername(userDetails.getUsername()));
		
		assertThat(illegalUserDetailsStateException)
				.isNotNull()
				.isInstanceOf(IllegalUserDetailsStateException.class);
		assertThat(illegalUserDetailsStateException.getMessage())
				.startsWith("User ")
				.endsWith(" are expired")
				.isEqualTo(String.format("User credentials with username: %s are expired", userDetails.getUsername()));
	}
	
}





