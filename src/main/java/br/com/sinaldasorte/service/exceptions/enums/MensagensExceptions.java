package br.com.sinaldasorte.service.exceptions.enums;

public enum MensagensExceptions {
	
	SENHA_INVALIDA("e1", "Senha inválida");
	
	private String cod;
	private String mensagem;
	
	private MensagensExceptions(String cod, String mensagem) {
		this.cod = cod;
		this.mensagem = mensagem;
	}

	public String getCod() {
		return cod;
	}

	public String getMensagem() {
		return mensagem;
	}
	
	public static MensagensExceptions toEnum(String cod) {
		
		if(cod == null) {
			return null;
		}
		
		for(MensagensExceptions x: MensagensExceptions.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Código de exceção inválido: "+ cod);
	}
}