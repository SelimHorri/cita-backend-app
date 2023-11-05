package tn.cita.app.business.auth.register.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.cita.app.business.auth.register.helper.RegistrationHelper;
import tn.cita.app.business.auth.register.model.RegisterRequest;
import tn.cita.app.business.auth.register.model.RegisterResponse;
import tn.cita.app.business.auth.register.service.RegistrationService;
import tn.cita.app.exception.wrapper.*;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.repository.CredentialRepository;
import tn.cita.app.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
class RegistrationServiceImpl implements RegistrationService {
	
	private final CredentialRepository credentialRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final RegistrationHelper registrationHelper;
	
	@Value("${app.api-version}")
	private String apiVersion;
	
	@Override
	public RegisterResponse register(final RegisterRequest registerRequest) {
		log.info("** Register..*");
		
		if (!isValidRole(registerRequest.role()))
			throw new IllegalRoleTypeException("Wrong role type for registration, "
											   + "it should be Customer/Worker/Manager/Owner role");
		log.info("** User role checked successfully! *");
		
		this.credentialRepository
				.findByUsernameIgnoreCase(registerRequest.username()).ifPresent(c -> {
			throw new UsernameAlreadyExistsException(
					"Account with username: %s already exists".formatted(c.getUsername()));
		});
		log.info("** User not exist by username checked successfully! *");
		
		if (!registerRequest.password().equals(registerRequest.confirmPassword()))
			throw new PasswordNotMatchException("Passwords do not match! please check again");
		log.info("** User password confirmation checked successfully! *");
		
		return switch (UserRoleBasedAuthority.valueOf(registerRequest.role())) {
			case CUSTOMER -> this.registrationHelper.registerCustomer(registerRequest);
			case WORKER, MANAGER, OWNER -> this.registrationHelper.registerEmployee(registerRequest);
			case ADMIN -> null; //new RegisterResponse(false, null);
		};
	}
	
	private static boolean isValidRole(final String role) {
		return Arrays.stream(UserRoleBasedAuthority.values())
				.map(UserRoleBasedAuthority::name)
				.anyMatch(roleName -> roleName.equals(role));
	}
	
	@Override
	public String validateToken(final String token) {
		log.info("** Validate registration token..*");
		
		// fetch verificationToken by provided token
		final var verificationToken = this.verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new VerificationTokenNotFoundException("Link has been disabled"));
		
		// check if token expired => if expired, then flush out token and throw exception
		if (verificationToken.getExpireDate().isEqual(LocalDateTime.now()) 
				|| verificationToken.getExpireDate().isBefore(LocalDateTime.now())) {
			
			this.verificationTokenRepository.deleteByToken(token);
			throw new VerificationTokenExpiredException("Link has been expired");
		}
		
		// activate user
		final var credential = verificationToken.getCredential();
		credential.setIsEnabled(true);
		this.credentialRepository.save(credential);
		log.info("** User enabled successfully! *");
		
		// token should be deleted also after activating user to prevent re-access to url token
		this.verificationTokenRepository.deleteByToken(token);
		log.info("** User token has been deleted! *");
		
		return "User has been activated successfully, go and login!";
	}
	
	/**
	 * Careful: Contruct validateToken API URL dynamically.<br>
	 * if validateToken API URL changes, this contruction must be changed.
	 * @param username
	 * @return RegisterResponse
	 */
	@Override
	public RegisterResponse resendToken(final String username) {
		log.info("** resend token **");
		
		final var credential = this.credentialRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new CredentialNotFoundException(
						"Account: %s is not registered".formatted(username)));
		
		if (credential.getIsEnabled())
			throw new BusinessException("Account: '%s' is already enabled, go and login"
					.formatted(credential.getUsername()));
		
		return this.registrationHelper.sendAccountValidation(credential,
				ServletUriComponentsBuilder.fromCurrentServletMapping()
						.path(this.apiVersion)
						.path("/register")
						.path("/{token}"));
	}
	
}





