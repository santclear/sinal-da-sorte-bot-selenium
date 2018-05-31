package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	@FindBy(xpath=""
			+ "//h3[contains(text(),'Premiação')]//following-sibling::p[1]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[2]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[3]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[4]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[5]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[6]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[7]")
	private List<WebElement> rateios;
	
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
		else sorteios.add(null);
		if(!cidadesUfs0.isEmpty()) super.addCidades(sorteios, cidadesUfs0);
		else sorteios.add(null);
		
		return sorteios;
	}
	
	@Override
	public List<String> getUfs() {
		List<String> sorteios = new LinkedList<>();
		
		if(!cidadesUfs20.isEmpty()) super.addUfs(sorteios, cidadesUfs20);
		else sorteios.add(null);
		if(!cidadesUfs0.isEmpty()) super.addUfs(sorteios, cidadesUfs0);
		else sorteios.add(null);
		
		return sorteios;
	}
	
	@Override
	public BigDecimal getAcumuladoParaOProximoConcurso() {
		return super.getAcumuladoParaOProximoConcurso(acumuladoParaOProximoConcurso);
	}
	
	@Override
	public List<List<BigDecimal>> getRateios() {
		List<List<BigDecimal>> sorteios = new LinkedList<>();
		
		this.addRateios(sorteios, rateios);
		
		return sorteios;
	}
	
	@Override
	public void addRateios(List<List<BigDecimal>> sorteios, List<WebElement> rateios) {
		LinkedList<BigDecimal> rateiosBd = new LinkedList<>();
		for(WebElement rateio: rateios) {
			String texto = rateio.getText();
			Pattern pattern = Pattern.compile("(R\\$\\s[0-9\\.\\,]+)|(\\wão\\s\\wouve)");
			Matcher matcher = pattern.matcher(texto);
			if (matcher.find()) {
				texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.").replace("R$", "").trim();
				texto = texto.toLowerCase();
				BigDecimal rateioBd = Objects.deepEquals(texto, "não houve")?new BigDecimal(0):new BigDecimal(texto);
				rateiosBd.add(rateioBd);
			}
		}
		sorteios.add(rateiosBd);
	}
	
	@Override
	public List<List<Integer>> getNumeroGanhadores() {
		List<List<Integer>> sorteios = new LinkedList<>();
		
		this.addNumeroGanhadores(sorteios, rateios);
		
		return sorteios;
	}
	
	@Override
	public void addNumeroGanhadores(List<List<Integer>> sorteios, List<WebElement> rateios) {
		LinkedList<Integer> numeroGanhadores = new LinkedList<>();
		for(WebElement rateio: rateios) {
			String texto = rateio.getText();
			Pattern pattern = Pattern.compile("(\\d+\\saposta)|(\\wão\\s\\wouve)");
			Matcher matcher = pattern.matcher(texto);
			if (matcher.find()) {
				texto = matcher.group();
				texto = texto.toLowerCase();
				Integer numeroGanhoresInt = Objects.deepEquals(texto, "não houve")?new Integer(0):new Integer(texto.replaceAll("\\D", "").trim());
				numeroGanhadores.add(numeroGanhoresInt);
			}
		}
		sorteios.add(numeroGanhadores);
	}
	
	@Override
	public Concurso paraConcursoEntity(Loteria loteria) {
		Concurso obj = new Concurso();
		obj.setLocalizacaoSorteio(super.getLocalizacaoSorteio());
		obj.setArrecadacaoTotal(this.getArrecadacaoTotal());
		obj.setEstimativaDePremioParaOProximoConcurso(this.getEstimativaDePremioParaOProximoConcurso());
		obj.setDataDoSorteio(this.getDataSorteio());
		obj.setNumero(this.getConcurso());
		obj.setLoteria(loteria);
		return obj;
	}
	
	public List<Rateio> paraRateiosEntityList(List<Sorteio> sorteios) {
		List<Rateio> rateios = new LinkedList<>();
		int tipoDePremio = 1;
		List<BigDecimal> rateiosList = this.getRateios().get(0);
		for(BigDecimal rateio: rateiosList) {
			Rateio obj = new Rateio();
			obj.setRateio(rateio);
			obj.setTipoDePremio(String.valueOf(tipoDePremio));
			obj.setSorteio(sorteios.get(0));
			switch(tipoDePremio) {
				case 1:
					obj.setAcumuladoParaOProximoConcurso(getAcumuladoParaOProximoConcurso());
					obj.setCidades(this.getCidades().get(0));
					obj.setUfs(this.getUfs().get(0));
					break;
				case 7:
					obj.setCidades(this.getCidades().get(1));
					obj.setUfs(this.getUfs().get(1));
					break;
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
