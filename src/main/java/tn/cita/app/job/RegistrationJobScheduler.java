package tn.cita.app.job;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.model.domain.UserRoleBasedAuthority;
import tn.cita.app.repository.CredentialRepository;
import tn.cita.app.repository.CustomerRepository;
import tn.cita.app.repository.EmployeeRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationJobScheduler {
	
	private final CredentialRepository credentialRepository;
	private final CustomerRepository customerRepository;
	private final EmployeeRepository employeeRepository;
	
	/**
	 * Takes all disabled customers registered in the system, and delete them all due to unconfirmed state..
	 * Executed each weak (each 7 days)
	 */
	@Scheduled(cron = AppConstants.CRON_MONDAY_MIDAY)
	void scheduleCustomerUnconfirmedRegistration() {
		
		final var counted = this.credentialRepository
				.findAllByIsEnabledAndUserRoleBasedAuthority(false, UserRoleBasedAuthority.CUSTOMER).stream()
					.filter(c -> Instant.now().minus(1, ChronoUnit.WEEKS).isBefore(c.getCreatedAt()))
					.distinct()
					.map(c -> {
						var id = c.getCustomer().getId();
						this.customerRepository.deleteById(id);
						return id;
					})
					.peek(id -> log.info("Customer with id: {} has been deleted", id))
					.count();
		
		log.info("{} customers have been deleted at {} due to unconfirmed accounts", 
				counted, 
				Instant.now().atZone(ZoneId.systemDefault()));
	}
	
	/**
	 * Takes all disabled employees registered in the system, and delete them all due to unconfirmed state..
	 * Executed each weak (each 7 days)
	 */
	@Scheduled(cron = AppConstants.CRON_MONDAY_MIDAY)
	void scheduleEmployeeUnconfirmedRegistration() {
		
		var counted = 0L;
		
		for (var role : UserRoleBasedAuthority.values()) {
			counted += switch (role) {
				case WORKER, MANAGER, OWNER -> this.processEmployeeRemoval(role);
				default -> counted;
			};
		}
		
		log.info("{} employees have been deleted at {} due to unconfirmed accounts", 
				counted, 
				Instant.now().atZone(ZoneId.systemDefault()));
	}
	
	private long processEmployeeRemoval(@NonNull final UserRoleBasedAuthority role) {
		return this.credentialRepository
				.findAllByIsEnabledAndUserRoleBasedAuthority(false, role).stream()
					.filter(c -> Instant.now().minus(1, ChronoUnit.WEEKS).isBefore(c.getCreatedAt()))
					.distinct()
					.map(c -> {
						var id = c.getEmployee().getId();
						this.employeeRepository.deleteById(id);
						return id;
					})
					.peek(id -> log.info("{} with id: {} has been deleted", role.name(), id))
					.count();
	}
	
}




