package tn.cita.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
class HttpClientsConfig {
	
	@Bean
	RestClient restClient(RestClient.Builder builder) {
		return builder
				.requestFactory(new JdkClientHttpRequestFactory())
				.build();
	}
	
}



