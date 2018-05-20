package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.domain.Loteria;
import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;

public class LotomaniaPage extends BasePage {
	
	@FindBy(xpath="//*[contains(@ng-repeat, 'numeros in result')]")
	private List<WebElement> numerosSorteados;
	
	@FindBy(xpath="//h3[contains(text(),'Detalhamento - 20')]//following-sibling::div[contains(@ng-show, 'resultado.ganhadoresPorRegiao1')]//strong")
	private List<WebElement> cidadesUfs20;
	
	@FindBy(xpath="//h3[contains(text(),'Detalhamento - 0')]//following-sibling::p//strong")
	private List<WebElement> cidadesUfs0;
	
	@FindBy(xpath="//*[contains(text(),'Acumulado próximo')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement acumuladoParaOProximoConcurso;
	
	@Override
	public List<String> getNumerosSorteados() {
		List<String> sorteios = new LinkedList<>();
		
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 0, 20);
		
		return sorteios;
	}
	
	@Override
	public List<String> getCidades() {
		List<String> sorteios = new LinkedList<>();
		
		if(!cidadesUfs20.isEmpty()) super.addCidades(sorteios, cidadesUfs20);
		else sorteios.add(";");
		if(!cidadesUfs0.isEmpty()) super.addCidades(sorteios, cidadesUfs0);
		else sorteios.add(";");
		
		return sorteios;
	}
	
	@Override
	public List<String> getUfs() {
		List<String> sorteios = new LinkedList<>();
		
		if(!cidadesUfs20.isEmpty()) super.addUfs(sorteios, cidadesUfs20);
		else sorteios.add(";");
		if(!cidadesUfs0.isEmpty()) super.addUfs(sorteios, cidadesUfs0);
		else sorteios.add(";");
		
		return sorteios;
	}
	
	public BigDecimal getAcumuladoParaOProximoConcurso(WebDriver driver) {
		try {
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
		} catch(NoSuchElementException e) {
			return new BigDecimal(0);
		}
	}
	
	public Concurso paraConcursoEntity(Loteria loteria) {
		Concurso obj = new Concurso();
		obj.setArrecadacaoTotal(this.getArrecadacaoTotal());
		obj.setEstimativaDePremioParaOProximoConcurso(this.getEstimativaDePremioParaOProximoConcurso());
		obj.setDataDoSorteio(this.getDataSorteio());
		obj.setNumero(this.getConcurso());
		obj.setLoteria(loteria);
		return obj;
	}
	
	public List<Rateio> paraRateiosEntityList(List<Sorteio> sorteios, WebDriver driver) {
		List<Rateio> rateios = new LinkedList<>();
		int tipoDePremio = 1;
		List<BigDecimal> rateiosList = super.getRateios().get(0);
		for(BigDecimal rateio: rateiosList) {
			Rateio obj = new Rateio();
			obj.setRateio(rateio);
			obj.setTipoDePremio(String.valueOf(tipoDePremio));
			obj.setSorteio(sorteios.get(0));
			switch(tipoDePremio) {
				case 1:
					obj.setAcumuladoParaOProximoConcurso(getAcumuladoParaOProximoConcurso(driver));
					obj.setCidades(this.getCidades().get(0));
					obj.setUfs(this.getUfs().get(0));
					break;
				case 7:
					obj.setCidades(this.getCidades().get(1));
					obj.setUfs(this.getUfs().get(1));
					break;
				default:
					obj.setCidades(";");
					obj.setUfs(";");
					
			}
			rateios.add(obj);
			tipoDePremio++;
		}
		int i = 0;
		List<Integer> numeroGanhadoresList = this.getNumeroGanhadores().get(0);
		for(Integer numeroGanhaores: numeroGanhadoresList) {
			rateios.get(i).setNumeroDeGanhadores(numeroGanhaores);
			i++;
		}
		return rateios;
	}
}
