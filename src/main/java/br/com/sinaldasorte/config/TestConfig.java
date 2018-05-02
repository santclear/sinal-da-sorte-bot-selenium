package br.com.sinaldasorte.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.sinaldasorte.service.MockEmailService;
import br.com.sinaldasorte.service.ProfileTestService;
import br.com.sinaldasorte.service.interfaces.EmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private ProfileTestService service;
	
	@Bean
	public boolean init() {
		service.init();
		return true;
	}
	
	// @Bean faz com que o método esteja disponível no sistema como componente
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
