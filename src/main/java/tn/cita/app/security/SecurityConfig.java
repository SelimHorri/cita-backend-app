package tn.cita.app.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;
import tn.cita.app.config.filter.JwtRequestFilter;
import tn.cita.app.constant.AppConstants;
import tn.cita.app.model.domain.UserRoleBasedAuthority;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
	
	private final JwtRequestFilter jwtRequestFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigSource) throws Exception {
		return http
				// .cors(cors -> cors.configurationSource(corsConfigSource))
				.cors(CorsConfigurer<HttpSecurity>::disable)
				.csrf(CsrfConfigurer<HttpSecurity>::disable)
				.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
						.requestMatchers(antMatcher(HttpMethod.OPTIONS, "/**")).permitAll()
						.requestMatchers(mapUrls(AppConstants.WHITELIST_URLS)).permitAll()
						.requestMatchers(mapUrls(HttpMethod.GET, AppConstants.WHITE_BLACKLISTED_URLS_GET)).authenticated()
						.requestMatchers(mapUrls(HttpMethod.GET, AppConstants.WHITELIST_URLS_GET)).permitAll()
						.requestMatchers(antMatcher("/api/v*/customers/**"))
							.hasRole(UserRoleBasedAuthority.CUSTOMER.name())
						.requestMatchers(antMatcher("/api/v*/employees/workers/**"))
							.hasAnyRole(UserRoleBasedAuthority.WORKER.name(),
										UserRoleBasedAuthority.MANAGER.name(),
										UserRoleBasedAuthority.OWNER.name())
						.requestMatchers(antMatcher("/api/v*/employees/managers/**"))
							.hasAnyRole(UserRoleBasedAuthority.MANAGER.name(),
										UserRoleBasedAuthority.OWNER.name())
						.requestMatchers(antMatcher("/api/v*/employees/owners/**"))
							.hasRole(UserRoleBasedAuthority.OWNER.name())
						.requestMatchers(antMatcher("/api/v*/employees/**"))
							.hasAnyRole(UserRoleBasedAuthority.WORKER.name(),
										UserRoleBasedAuthority.MANAGER.name(),
										UserRoleBasedAuthority.OWNER.name())
						.requestMatchers(antMatcher("/api/v*/admins/**"))
							.hasRole(UserRoleBasedAuthority.ADMIN.name())
						.anyRequest().authenticated())
				.exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> response
						.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access point")))
				.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
												  PasswordEncoder passwordEncoder) {
		var daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}
	
	private static RequestMatcher[] mapUrls(HttpMethod httpMethod, String[] paths) {
		return Arrays.stream(paths)
				.map(path -> antMatcher(httpMethod, path))
				.map(RequestMatcher.class::cast)
				.toArray(RequestMatcher[]::new);
	}
	
	private static RequestMatcher[] mapUrls(String[] paths) {
		return Arrays.stream(paths)
				.map(AntPathRequestMatcher::antMatcher)
				.map(RequestMatcher.class::cast)
				.toArray(RequestMatcher[]::new);
	}
	
}



