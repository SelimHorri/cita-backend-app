package tn.cita.app.business.auth.register.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tn.cita.app.business.auth.register.model.RegisterRequest;
import tn.cita.app.business.auth.register.model.RegisterResponse;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.model.domain.entity.Credential;
import tn.cita.app.model.domain.entity.Customer;
import tn.cita.app.model.domain.entity.Employee;
import tn.cita.app.model.domain.entity.VerificationToken;
import tn.cita.app.model.dto.notif.MailNotification;
import tn.cita.app.repository.CustomerRepository;
import tn.cita.app.repository.EmployeeRepository;
import tn.cita.app.repository.VerificationTokenRepository;
import tn.cita.app.util.NotificationUtil;
import tn.cita.app.util.UserRoleUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RegistrationHelper {
	
	@Qualifier("mailNotificationUtil")
	private final NotificationUtil mailNotificationUtil;
	private final VerificationTokenRepository verificationTokenRepository;
	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;
	
	public RegisterResponse registerCustomer(final RegisterRequest registerRequest) {
		log.info("Register Customer process... ! ");
		
		final var savedCustomer = this.customerRepository
				.save(Customer.builder()
						.firstname(registerRequest.firstname())
						.lastname(registerRequest.lastname())
						.email(registerRequest.email())
						.phone(registerRequest.phone())
						.birthdate(registerRequest.birthdate())
						.credential(
								Credential.builder()
										.username(registerRequest.username())
										.password(this.passwordEncoder
												.encode(registerRequest.password()))
										.userRoleBasedAuthority(UserRoleUtils
												.getUserRoleFrom(registerRequest.role()))
										.isEnabled(false)
										.isAccountNonExpired(true)
										.isAccountNonLocked(true)
										.isCredentialsNonExpired(true)
										.build())
						.build());
		log.info("Customer saved successfully! ");
		
		return this.sendAccountValidation(savedCustomer.getCredential(),
				ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{token}"));
	}
	
	public RegisterResponse registerEmployee(final RegisterRequest registerRequest) {
		log.info("Register Employee process... ! ");
		
		final var savedEmployee = this.employeeRepository
				.save(Employee.builder()
						.firstname(registerRequest.firstname())
						.lastname(registerRequest.lastname())
						.email(registerRequest.email())
						.phone(registerRequest.phone())
						.birthdate(registerRequest.birthdate())
						.credential(
								Credential.builder()
										.username(registerRequest.username())
										.password(this.passwordEncoder
												.encode(registerRequest.password()))
										.userRoleBasedAuthority(UserRoleUtils
												.getUserRoleFrom(registerRequest.role()))
										.isEnabled(false)
										.isAccountNonExpired(true)
										.isAccountNonLocked(true)
										.isCredentialsNonExpired(true)
										.build())
						.build());
		log.info("Employee saved successfully! ");
		
		return this.sendAccountValidation(savedEmployee.getCredential(),
				ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{token}"));
	}
	
	public RegisterResponse sendAccountValidation(final Credential credential, final UriComponentsBuilder uriBuilder) {
		final var verificationToken = new VerificationToken(
				UUID.randomUUID().toString(),
				LocalDateTime.now().plusMinutes(AppConstants.USER_EXPIRES_AFTER_MINUTES),
				credential);
		final var savedVerificationToken = this.verificationTokenRepository.save(verificationToken);
		log.info("Verification token saved successfully! ");
		
		this.sendValidationMail(savedVerificationToken, uriBuilder);
		
		return new RegisterResponse("""
				User with username %s has been saved successfully.
				Check your email to enable your account.
				Please consider that link will expire after %dmin from registration.
				""".formatted(credential.getUsername(), AppConstants.USER_EXPIRES_AFTER_MINUTES));
	}
	
	private void sendValidationMail(final VerificationToken verificationToken, final UriComponentsBuilder uriBuilder) {
		if (verificationToken.getCredential()
				.getUserRoleBasedAuthority() == UserRoleBasedAuthority.CUSTOMER) {
			this.mailNotificationUtil.sendHtmlMail(
					new MailNotification(
							verificationToken.getCredential().getCustomer().getEmail(),
							"Registration"),
					Map.of(
							"username", verificationToken.getCredential().getUsername(),
							"confirmLink", uriBuilder.build(verificationToken.getToken()).toString()));
			log.info("Mail sent successfully to: {}! ", verificationToken.getCredential().getCustomer().getEmail());
		}
		else {
			this.mailNotificationUtil.sendHtmlMail(
					new MailNotification(
							verificationToken.getCredential().getEmployee().getEmail(),
							"Registration"),
					Map.of(
							"username", verificationToken.getCredential().getUsername(),
							"confirmLink", uriBuilder.build(verificationToken.getToken()).toString()));
			log.info("Mail sent successfully to: {}! ", verificationToken.getCredential().getEmployee().getEmail());
		}
	}
	
}




