package br.com.sinaldasorte.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import br.com.sinaldasorte.service.interfaces.EmailService;
import br.com.sinaldasorte.service.util.MensagemEmail;

public abstract class AbstractEmailService extends MensagemEmail implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Value("${sinaldasorte.email}")
	private String email;
	
	@Override
	public void envieErro(String msgErro, String stackTrace) {
		SimpleMailMessage sm = prepareErro(msgErro, stackTrace);
		sendEmail(sm);
	}
	
	protected SimpleMailMessage prepareErro(String msgErro, String stackTrace) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(email);
		sm.setFrom(sender);
		sm.setSubject("Sinal da Sorte: "+ msgErro);
		sm.setSentDate(new Date(System.currentTimeMillis()));
		
		sm.setText(erro(msgErro, stackTrace));
		return sm;
	}
}
