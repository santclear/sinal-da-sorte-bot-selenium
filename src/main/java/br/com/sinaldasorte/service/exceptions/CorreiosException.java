package br.com.sinaldasorte.service.exceptions;

public class CorreiosException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CorreiosException(String msg) {
		super(msg);
	}
	
	public CorreiosException(String msg, Throwable cause) {
		super(msg, cause);
	}
}