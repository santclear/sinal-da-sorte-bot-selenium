package br.com.sinaldasorte.service.util;

import org.springframework.beans.factory.annotation.Value;

public abstract class MensagemEmail {
	
	@Value("${sinaldasorte.front.url}")
	private String sinalDaSorteFrontUrl;
	
	@Value("${sinaldasorte.ws.url}")
	private String sinalDaSorteWsUrl;
	
	@Value("${sinaldasorte.www}")
	private String www;
	
	@Value("${sinaldasorte.email}")
	private String email;
	
	@Value("${sinaldasorte.adverbio}")
	private String adverbio;
	
	@Value("${sinaldasorte.responsavel}")
	private String responsavel;
	
	@Value("${sinaldasorte.copyright}")
	private String copyright;
	
	@Value("${sinaldasorte.organizacao}")
	private String organizacao;
	
	@Value("${sinaldasorte.slogan}")
	private String slogan;
	
	public String erro(String msgErro, String stackTrace) {
		StringBuilder sb = new StringBuilder();
		sb.append(erroTexto(msgErro, stackTrace));
		sb.append(assinaturaTexto());
		sb.append(copyrightTexto());
		sb.append("<split>");
		sb.append(erroHtml(msgErro, stackTrace));
		sb.append(assinaturaHtml());
		sb.append(copyrightHtml());
		return sb.toString();
	}
	
	private String erroTexto(String msgErro, String stackTrace) {
		StringBuilder sb = new StringBuilder();
		sb.append("Mensagem: "+ msgErro +",\n\n");
		sb.append("Stacktrace: "+ stackTrace);;
		return sb.toString();
	}
	
	private String erroHtml(String msgErro, String stackTrace) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='margin: 0 auto; width: 500px; margin-bottom: 60px;'>");
		sb.append("	<p style='color: #424242; margin-bottom: 5px;font-size: 11pt;'>Mensagem: "+ msgErro +",</p>");
		sb.append("	<p style='color: #424242; margin-bottom: 5px;font-size: 11pt;'>Stacktrace: "+ stackTrace +"</p>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private String assinaturaTexto() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n");
		sb.append("--\n");
		sb.append(adverbio +",\n\n");
		sb.append(responsavel +"\n\n");
		sb.append(email +"\n");
		sb.append(www +"\n\n");
		sb.append("Você também pode ser um milionário! "+ slogan +"\n\n");
		return sb.toString();
	}
	
	private String assinaturaHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("	<div style='margin: 0 auto; width: 500px; margin-bottom: 20px;'>");
		sb.append("	<hr>");
		sb.append("<p>");
		sb.append("	<span style='color: #424242; font-size: 11pt;font-weight: bold'>"+ adverbio +"</span>,");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("	<span style='color: #424242; font-size: 11pt;font-weight: bold'>"+ responsavel +"</span>");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("	<span style='font-size: 11pt'>");
		sb.append("		<a style='color: #1b5e20;text-decoration: none;' target='_top' title='"+ email +"'>");
		sb.append("			"+ email +"");
		sb.append("		</a>");
		sb.append("	</span><br/>");
		sb.append("	<span style='font-size: 11pt;'>");
		sb.append("		<a style='color: #1b5e20;text-decoration: none;' href='"+ sinalDaSorteFrontUrl +"' target=\"_blank\" title='"+ www +"'>");
		sb.append("			"+ www +"");
		sb.append("		</a>");
		sb.append("	</span>");
		sb.append("</p>");
		sb.append("	<span style='font-size: 12pt;font-weight: bold; color: #1b5e20;'>");
		sb.append("			"+ slogan +"");
		sb.append("	</span><br/>");
		sb.append("	<img src='http://www.sinaldasorte.com/img/logo.png' alt='Logo Sinal da Sorte' height='150' width='150'></img>");
		sb.append(" </div><br/>");
		return sb.toString();
	}
	
	private String copyrightTexto() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(copyright +" | de "+ organizacao);
		return sb.toString();
	}
	
	private String copyrightHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='margin: 0 auto;width: 500px;overflow: hidden;'>" + 
				"		<div style='width: 50%;float: left;background-color: #1b5e20;height: 50px;line-height: 50px;text-align: center;color: white;'>" + 
				"			"+ copyright +"" + 
				"		</div>" + 
				"		<div style='margin-left: 50%;background-color: #1b5e20;height: 50px;line-height: 50px;text-align: center;color: white;'>" + 
				"			de "+ organizacao + 
				"		</div>" + 
				"	</div>");
		return sb.toString();
	}
}
