package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;

public class MegaSenaPage extends BasePage {
	
	@FindBy(xpath="//*[@class='numbers megasena']//li")
	private List<WebElement> numerosSorteados;
	
	@FindBy(xpath="//*[contains(text(),'Acumulado próximo concurso')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement acumuladoParaOProximoConcurso;
	
	@FindBy(xpath="//h3[contains(text(),'Detalhamento')]//following-sibling::p//span[contains(@ng-show,'regiao.sgUf')]")
	private List<WebElement> cidadesUfs;
	
	@FindBy(xpath=""
			+ "//h3[contains(text(),'Premiação')]//following-sibling::p[1]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[2]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[3]")
	private List<WebElement> rateios;
	
	@Override
	public List<String> getNumerosSorteados() {
		List<String> sorteios = new LinkedList<>();
		
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 0, 6);

		return sorteios;
	}
	
	@Override
	public BigDecimal getAcumuladoParaOProximoConcurso() {
		try {
			return super.getAcumuladoParaOProximoConcurso(acumuladoParaOProximoConcurso);
		} catch(NoSuchElementException e) {
			return new BigDecimal(0);
		}
	}
	
	@Override
	public List<String> getCidades() {
		List<String> sorteios = new LinkedList<>();
		
		this.addCidades(sorteios, cidadesUfs);
		
		return sorteios;
	}
	
	@Override
	protected void addCidades(List<String> sorteios, List<WebElement> cidadesUfs) {
		StringBuilder cidadesStr = new StringBuilder();
		for(WebElement cidadeUf: cidadesUfs) {
			if(!cidadeUf.getText().isEmpty()) {
				String cidadesUfsStr[] = cidadeUf.getText().split("-");
				String cidadeStr = ";";
				if(cidadesUfsStr.length > 1) {
					cidadeStr = cidadesUfsStr[0].trim();
					WebElement descricaoQtdApostas = cidadeUf.findElement(By.xpath("./ancestor::p"));
					for(int i = 0; i < this.getQtdGanhadoresNaRegiao(descricaoQtdApostas); i++) {
						cidadesStr.append(cidadeStr);
						cidadesStr.append(";");
					}
				}
			}
		}
		
		if(!cidadesStr.toString().isEmpty()) {
			sorteios.add(cidadesStr.toString().substring(0, cidadesStr.toString().length() - 1));
		} else {
			sorteios.add(null);
		}
	}
	
	@Override
	public List<String> getUfs() {
		List<String> sorteios = new LinkedList<>();
		
		this.addUfs(sorteios, cidadesUfs);
		
		return sorteios;
	}
	
	@Override
	protected void addUfs(List<String> sorteios, List<WebElement> cidadesUfs) {
		StringBuilder ufsStr = new StringBuilder();
		for(WebElement cidadeUf: cidadesUfs) {
			if(!cidadeUf.getText().isEmpty()) {
				String cidadesUfsStr[] = cidadeUf.getText().split("-");
				String ufStr = ";";
				if(cidadesUfsStr.length < 2) {
					ufStr = cidadesUfsStr[0].trim();
				} else {
					ufStr = cidadesUfsStr[1].trim();
				}
				WebElement descricaoQtdApostas = cidadeUf.findElement(By.xpath("./ancestor::p"));
				for(int i = 0; i < this.getQtdGanhadoresNaRegiao(descricaoQtdApostas); i++) {
					ufsStr.append(ufStr);
					ufsStr.append(";");
				}
			}
		}
		if(!ufsStr.toString().isEmpty()) {
			sorteios.add(ufsStr.toString().substring(0, ufsStr.toString().length() - 1));
		} else {
			sorteios.add(null);
		}
	}
	
	@Override
	protected Integer getQtdGanhadoresNaRegiao(WebElement descricaoQtdApostas) {
		String qtdApostasStr = descricaoQtdApostas.getAttribute("innerText");
		Pattern pattern = Pattern.compile("\\d+\\s+aposta");
		Matcher matcher = pattern.matcher(qtdApostasStr);
		Integer qtdApostas = null;
		if (matcher.find()) {
			pattern = Pattern.compile("\\d+");
			matcher = pattern.matcher(matcher.group());
			if (matcher.find()) {
				qtdApostas = Integer.parseInt(matcher.group());
			}
		}
		return qtdApostas;
	}
	
	@Override
	public List<List<Integer>> getNumeroGanhadores() {
		List<List<Integer>> sorteios = new LinkedList<>();
		
		this.addNumeroGanhadores(sorteios, rateios);
		
		return sorteios;
	}
	
	public List<List<BigDecimal>> getRateios() {
		List<List<BigDecimal>> sorteios = new LinkedList<>();
		
		this.addRateios(sorteios, rateios);
		
		return sorteios;
	}
	
	@Override
	public List<Rateio> paraRateiosEntityList(List<Sorteio> sorteios) {
		List<Rateio> rateios = new LinkedList<>();
		int tipoDePremio = 1;
		for(BigDecimal rateio: this.getRateios().get(0)) {
			Rateio obj = new Rateio();
			obj.setRateio(rateio);
			obj.setTipoDePremio(String.valueOf(tipoDePremio));
			obj.setSorteio(sorteios.get(0));
			if(tipoDePremio == 1) {
				obj.setAcumuladoParaOProximoConcurso(this.getAcumuladoParaOProximoConcurso());
				obj.setCidades(this.getCidades().get(0));
				obj.setUfs(this.getUfs().get(0));
			}
			rateios.add(obj);
			tipoDePremio++;
		}
		int i = 0;
		for(Integer numeroGanhaores: this.getNumeroGanhadores().get(0)) {
			rateios.get(i).setNumeroDeGanhadores(numeroGanhaores);
			i++;
		}
		return rateios;
	}
}
