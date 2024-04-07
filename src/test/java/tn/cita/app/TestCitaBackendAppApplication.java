package tn.cita.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestCitaBackendAppApplication {
	
	@RestartScope
	@ServiceConnection
	@Bean
	MySQLContainer<?> mySQLContainer() {
		return new MySQLContainer<>(DockerImageName.parse("mysql:5.7.40"));
	}
	
	public static void main(String[] args) {
		SpringApplication.from(CitaBackendAppApplication::main)
				.with(TestCitaBackendAppApplication.class)
				.run(args);
	}
	
}



