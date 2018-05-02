package br.com.sinaldasorte.service.exceptions;

public class ProcessamentoInternoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProcessamentoInternoException(String message) {
		super(message);
	}

	public ProcessamentoInternoException(String message, Throwable cause) {
		super(message, cause);
	}
}
