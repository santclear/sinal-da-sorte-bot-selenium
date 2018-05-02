package br.com.sinaldasorte.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.sinaldasorte.service.ProfileDevService;
import br.com.sinaldasorte.service.SmtpEmailService;
import br.com.sinaldasorte.service.interfaces.EmailService;
import br.com.sinaldasorte.service.util.OAuth2SaslClientFactory;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private ProfileDevService service;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean init() {
		
		if (!"create".equals(strategy)) {
			return false;
		}
		
		service.init();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("sinaldasorteweb@gmail.com");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.smtp.sasl.enable", "true");
	    props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
	    props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, "ya29.GluABdUOc-j5BsivWqpfetuDrvOjhNJ-ONXC_T9KecI3XmeLQKgPUD5c7eYgDZuv-puSo4kEWEkGhwHyE-szrsvTbHll0Qxek5ZaQDYXNtmVgqfV1yi00ovV0Ado");
	     
	    return mailSender;
	}
}
