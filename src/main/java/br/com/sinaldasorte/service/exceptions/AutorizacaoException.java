package br.com.sinaldasorte.service.exceptions;

public class AutorizacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AutorizacaoException(String msg) {
		super(msg);
	}
	
	public AutorizacaoException(String msg, Throwable cause) {
		super(msg, cause);
	}
}