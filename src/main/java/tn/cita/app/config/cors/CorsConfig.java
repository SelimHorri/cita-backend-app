package tn.cita.app.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import tn.cita.app.constant.AppConstants;

import java.util.List;

@Configuration(proxyBeanMethods = false)
class CorsConfig {
	
	@Bean
	CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
		return new CorsFilter(corsConfigurationSource);
	}
	
	@Primary
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		var corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(List.of("*"));
		corsConfiguration.setAllowedHeaders(List.of("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", AppConstants.USERNAME_AUTH_HEADER, "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization",
				AppConstants.USERNAME_AUTH_HEADER, "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		
		var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/*", corsConfiguration);
		return urlBasedCorsConfigurationSource;
	}
	
}



