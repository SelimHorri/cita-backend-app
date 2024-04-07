package tn.cita.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.time.Duration;

/**
 * Define all constants related to the App
 * @author selim
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstants {
	
	/**
	 * USERNAME_AUTH_HEADER: is a custom header to be sure that 
	 * no other user can access another user resources
	 */
	public static final String USERNAME_AUTH_HEADER = "UsernameAuth";
	public static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
	
	/**
	 * DateTime custom API formats
	 */
	public static final String LOCAL_DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOCAL_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	public static final String ZONED_DATE_TIME_FORMAT = "dd-MM-yyyy__HH:mm:ss:SSSSSS";
	
	/**
	 * API Context to setup Version for webservices
	 */
	public static final String API_CONTEXT_V0 = "/api/v0";
	
	/**
	 * WhiteList URLs to be authorised with http security
	 */
	public static final String[] WHITELIST_URLS = {
			"/",
			"/h2-console/**",
			"/api/v*/authenticate/**",
			"/api/v*/register/**",
			"/v3/api-docs/**",
			"/swagger-ui*/**",
	};
	
	public static final String[] WHITELIST_URLS_GET = {
			"/actuator/info",
			"/actuator/health",
			"/actuator/health/readiness",
			"/actuator/health/liveness",
			"/api/v*/actuator/health",
			"/api/v*/tags/**",
			"/api/v*/saloon-tags/**",
			"/api/v*/saloons/**",
			"/api/v*/saloon-images/**",
			"/api/v*/locations/**",
			"/api/v*/categories/**",
			"/api/v*/service-details/**",
			"/api/v*/ratings/**",
	};
	
	public static final String[] WHITE_BLACKLISTED_URLS_GET = {
			"/api/v*/service-details/reservationId/**",
			"/api/v*/service-details/reservationIdentifier/**",
	};
	
	/**
	 * Default page size for Pageable objects
	 */
	public static final int PAGE_SIZE = 5;
	
	public static final String MAIL_SOURCE = "cita.team.mail@gmail.com";
	
	/**
	 * Verification Token expires after specified minutes from localdatetime.now
	 */
	public static final long USER_EXPIRES_AFTER_MINUTES = Duration.ofMinutes(30).toMinutes();
	
	/**
	 * Delay that customer can pass a valid reservation from localdatetime.now
	 */
	public static final long VALID_START_DATE_AFTER_MINUTES = Duration.ofMinutes(30).toMinutes();
	
	/**
	 * Midnight  cron pattern
	 */
	public static final String CRON_MIDNIGHT = "0 0 0 * * *";
	
	/**
	 * Runs each Monday at 12am..
	 */
	public static final String CRON_MONDAY_MIDAY = "0 0 12 * * 1";
	
}



