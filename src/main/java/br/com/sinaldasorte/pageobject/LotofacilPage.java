package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;

public class LotofacilPage extends BasePage {
	
	// Encontre o elemento que tem o texto 'Acumulado para...', a partir dele encontre o elemento irmão que contenha o texto 'R$'
	@FindBy(xpath="//*[text()='Acumulado para Sorteio Especial da Independência']//following-sibling::*[contains(text(),'R$')]")
	private WebElement  acumuladoEspecial;
	
	@FindBy(xpath="//*[contains(text(),'Estimativa de prêmio do próximo concurso')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement estimativaDePremioParaOProximoConcurso;
	
	@FindBy(xpath="//*[@ng-repeat='dezena in resultadoLinha']")
	private List<WebElement> numerosSorteados;
	
	@FindBy(xpath="//*[text()='Valor acumulado']//following-sibling::*[contains(text(),'R$')]")
	private WebElement acumuladoParaOProximoConcurso;
	
	public BigDecimal getAcumuladoEspecial() {
		// String do sorteio especial, exemplo: R$ 10.000,00
		String texto = acumuladoEspecial.getText();
		// Formato do regex para extrair somente o valor, exemplo: 10.000,00
		Pattern pattern = Pattern.compile("\\d+.+");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			// Exclui os pontos (.), substitui o serador decimal vírgula (,) por ponto(.)
			texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.");
			BigDecimal numero = new BigDecimal(texto);
			return numero;
		} else {
			throw new ObjetoNaoEncontradoException("Não foi possível extrair o valor do Acumulado Especial.");
		}
	}
	
	@Override
	public BigDecimal getEstimativaDePremioParaOProximoConcurso() {
		String texto = estimativaDePremioParaOProximoConcurso.getText();
		Pattern pattern = Pattern.compile("\\d+.+");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.");
			BigDecimal numero = new BigDecimal(texto);
			return numero;
		} else {
			throw new ObjetoNaoEncontradoException("Não foi possível extrair o valor da Estimativa de prêmio do próximo concurso.");
		}
	}
	
	@Override
	public String getNumerosSorteados() {
		StringBuilder numerosSorteadosStr = new StringBuilder();
		for(WebElement numero: numerosSorteados) {
			String numeroStr = numero.getText();
			numerosSorteadosStr.append(numeroStr);
			numerosSorteadosStr.append(";");
		}
		if(!numerosSorteadosStr.toString().isEmpty()) {
			return numerosSorteadosStr.toString().substring(0, numerosSorteadosStr.toString().length() - 1);
		}
		return numerosSorteadosStr.toString();
	}
	
	@Override
	public BigDecimal getAcumuladoParaOProximoConcurso() {
		String texto = acumuladoParaOProximoConcurso.getText();
		Pattern pattern = Pattern.compile("\\d+.+");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.");
			BigDecimal numero = new BigDecimal(texto);
			return numero;
		} else {
			return new BigDecimal(0);
		}
	}
}
