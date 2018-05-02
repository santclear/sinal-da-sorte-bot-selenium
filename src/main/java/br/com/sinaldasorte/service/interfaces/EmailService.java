package br.com.sinaldasorte.service.interfaces;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

	void sendEmail(SimpleMailMessage mensagem);
	void envieErro(String msgErro, String stackTrace);
}
