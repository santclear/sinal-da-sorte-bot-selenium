package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.domain.Loteria;
import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;
import br.com.sinaldasorte.service.exceptions.ObjetoNaoEncontradoException;
import br.com.sinaldasorte.service.exceptions.ProcessamentoInternoException;

public abstract class BasePage {
	// Encontre o elemento que tem 'id=resultados' e a partir dele encontre o elemento filho que contenha o texto 'Concurso' 
	@FindBy(xpath="//*[@id='resultados']//*[contains(text(),'Concurso')]")
	private WebElement concurso;
	
	@FindBy(xpath="//*[contains(text(), 'Sorteio realizado')]")
	private WebElement localizacaoSorteio;
	
	@FindBy(xpath="//*[contains(text(),'Estimativa de prêmio do próximo concurso')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement estimativaDePremioParaOProximoConcurso;
	
	@FindBy(xpath="//*[contains(text(),'Valor acumulado')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement acumuladoParaOProximoConcurso;
	
	// Encontre o elemento que tem o texto 'Acumulado para...', a partir dele encontre o elemento irmão que contenha o texto 'R$'
	@FindBy(xpath="//*[contains(text(),'Acumulado para Sorteio Especial')]//following-sibling::*[contains(text(),'R$')]")
	private WebElement  acumuladoEspecial;
	
	// Encontre o elemento que tem o texto 'Arrecadação total', a partir dele encontre o irmão que tenha o filho que contem o texto 'R$'
	@FindBy(xpath="//h3[text()='Arrecadação total']//following-sibling::*//*[contains(text(),'R$')]")
	private WebElement arrecadacaoTotal;
	
	// Encontre o elemento que tem o texto 'Detalhamento', a partir dele encontre o elemento irmão 'p' que tenha um elemento filho 'strong'
	@FindBy(xpath="//h3[contains(text(),'Detalhamento')]//following-sibling::p//strong")
	private List<WebElement> cidadesUfs;
	
	@FindBy(xpath="//h3[contains(text(),'Premiação')]//following-sibling::p")
	private List<WebElement> rateios;
	
	public Integer getConcurso() {
		// String do concurso, exemplo: Concurso 1655 (27/04/2018)
		String texto = concurso.getText();
		// Formato do regex para extrair somente o número do concurso, exemplo: 1655
		Pattern pattern = Pattern.compile("\\s\\d+\\s");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group().trim());
		} else {
			throw new ObjetoNaoEncontradoException("Não foi possível extrair o número do concurso.");
		}
	}
	
	public String getLocalizacaoSorteio() {
		return localizacaoSorteio.getText().trim();
	}
	
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
	
	public BigDecimal getAcumuladoParaOProximoConcurso() {
		try {
			return this.getAcumuladoParaOProximoConcurso(acumuladoParaOProximoConcurso);
		} catch(NoSuchElementException e) {
			return new BigDecimal(0);
		}
	}
	
	public BigDecimal getAcumuladoParaOProximoConcurso(WebElement element) {
		String texto = element.getText();
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
	
	public BigDecimal getArrecadacaoTotal() {
		try {
			// String do sorteio especial, exemplo: R$ 10.000,00
			String texto = arrecadacaoTotal.getText();
			// Formato do regex para extrair somente o valor, exemplo: 10.000,00
			Pattern pattern = Pattern.compile("\\d+.+");
			Matcher matcher = pattern.matcher(texto);
			matcher.find();
			// Exclui os pontos (.), substitui o serador decimal vírgula (,) por ponto(.)
			texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.");
			BigDecimal numero = new BigDecimal(texto);
			return numero;
		} catch(NoSuchElementException e) {
			return new BigDecimal(0);
		}
	}
	
	public List<String> getCidades() {
		List<String> sorteios = new LinkedList<>();
		
		this.addCidades(sorteios, cidadesUfs);
		
		return sorteios;
	}
	
	protected void addCidades(List<String> sorteios, List<WebElement> cidadesUfs) {
		StringBuilder cidadesStr = new StringBuilder();
		for(WebElement cidadeUf: cidadesUfs) {
			if(!cidadeUf.getText().isEmpty()) {
				String cidadesUfsStr[] = cidadeUf.getText().split("-");
				String cidadeStr = ";";
				if(cidadesUfsStr.length > 1) {
					cidadeStr = cidadesUfsStr[0].trim();
					WebElement descricaoQtdApostas = cidadeUf.findElement(By.xpath("./ancestor::span[1]//following-sibling::span[2]"));
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
	
	public List<String> getUfs() {
		List<String> sorteios = new LinkedList<>();
		
		this.addUfs(sorteios, cidadesUfs);
		
		return sorteios;
	}
	
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
				WebElement descricaoQtdApostas = cidadeUf.findElement(By.xpath("./ancestor::span[1]//following-sibling::span[2]"));
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
	
	protected Integer getQtdGanhadoresNaRegiao(WebElement descricaoQtdApostas) {
		String qtdApostasStr = descricaoQtdApostas.getAttribute("innerText");
		qtdApostasStr = qtdApostasStr.replaceAll("\\n", "").replaceAll("\\t", "");
		qtdApostasStr = qtdApostasStr.substring(0, 6);
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(qtdApostasStr);
		Integer qtdApostas = null;
		if (matcher.find()) {
			qtdApostas = Integer.parseInt(matcher.group());
		}
		return qtdApostas;
	}
	
	public Calendar getDataSorteio() {
		// String do concurso, exemplo: Concurso 1655 (27/04/2018)
		String texto = concurso.getText();
		// Formato do regex para extrair somente o número do concurso, exemplo: 1655
		Pattern pattern = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar dataSorteio = Calendar.getInstance();
			
			try {
				dataSorteio.setTime(sdf.parse(matcher.group()));
			} catch (ParseException e) {
				throw new ProcessamentoInternoException("Ocorreu um erro ao tentar efetuar o parse da data do concurso para objeto Calendar.");
			}
			
			return dataSorteio;
		} else {
			throw new ObjetoNaoEncontradoException("Não foi possível extrair a data do concurso.");
		}
	}
	
	public List<List<BigDecimal>> getRateios() {
		List<List<BigDecimal>> sorteios = new LinkedList<>();
		
		this.addRateios(sorteios, rateios);
		
		return sorteios;
	}
	
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
			if("Veja o detalhamento".equals(texto)) rateiosBd.add(new BigDecimal(0));
		}
		
		sorteios.add(rateiosBd);
	}
	
	public List<List<Integer>> getNumeroGanhadores() {
		List<List<Integer>> sorteios = new LinkedList<>();
		
		this.addNumeroGanhadores(sorteios, rateios);
		
		return sorteios;
	}
	
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
	
	public Concurso paraConcursoEntity(Loteria loteria) {
		Concurso obj = new Concurso();
		obj.setLocalizacaoSorteio(this.getLocalizacaoSorteio());
		obj.setAcumuladoEspecial(this.getAcumuladoEspecial());
		obj.setArrecadacaoTotal(this.getArrecadacaoTotal());
		obj.setEstimativaDePremioParaOProximoConcurso(this.getEstimativaDePremioParaOProximoConcurso());
		obj.setDataDoSorteio(this.getDataSorteio());
		obj.setNumero(this.getConcurso());
		obj.setLoteria(loteria);
		return obj;
	}
	
	public List<Sorteio> paraSorteiosEntityList(Concurso concurso) {
		List<Sorteio> sorteios = new LinkedList<>();
		Sorteio obj = new Sorteio();
		obj.setNumerosSorteados(this.getNumerosSorteados().get(0));
		
		obj.setNumero(1);
		obj.setConcurso(concurso);
		sorteios.add(obj);
		return sorteios;
	}
	
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
	
	abstract List<String> getNumerosSorteados();
	
	protected void addNumerosSorteados(List<String> sorteios, List<WebElement> numerosSorteados, int indiceDezenaInicial, int indiceDezenaFinal) {
		StringBuilder numerosSorteadosStr = new StringBuilder();
		for(int i = indiceDezenaInicial; i < indiceDezenaFinal; i++) {
			WebElement numero = numerosSorteados.get(i);
			String numeroStr = numero.getText();
			numerosSorteadosStr.append(numeroStr);
			numerosSorteadosStr.append(";");
		}
		if(!numerosSorteadosStr.toString().isEmpty()) {
			sorteios.add(numerosSorteadosStr.toString().substring(0, numerosSorteadosStr.toString().length() - 1));
		}
	}
}
