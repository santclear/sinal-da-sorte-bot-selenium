package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	// Encontre o elemento que tem o texto 'Arrecadação total', a partir dele encontre o irmão que tenha o filho que contem o texto 'R$'
	@FindBy(xpath="//h3[text()='Arrecadação total']//following-sibling::*//*[contains(text(),'R$')]")
	private WebElement arrecadacaoTotal;
	
	// Encontre o elemento que tem o texto 'Detalhamento', a partir dele encontre o elemento irmão 'p' que tenha um elemento filho 'strong'
	@FindBy(xpath="//h3[text()='Detalhamento']//following-sibling::p//strong")
	private List<WebElement> cidadesUfs;
	
	@FindBy(xpath="//h3[text()='Premiação']//following-sibling::p")
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
	
	public BigDecimal getArrecadacaoTotal() {
		// String do sorteio especial, exemplo: R$ 10.000,00
		String texto = arrecadacaoTotal.getText();
		// Formato do regex para extrair somente o valor, exemplo: 10.000,00
		Pattern pattern = Pattern.compile("\\d+.+");
		Matcher matcher = pattern.matcher(texto);
		if (matcher.find()) {
			// Exclui os pontos (.), substitui o serador decimal vírgula (,) por ponto(.)
			texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.");
			BigDecimal numero = new BigDecimal(texto);
			return numero;
		} else {
			throw new ObjetoNaoEncontradoException("Não foi possível extrair o valor da Arrecadação Total.");
		}
	}
	
	public String getCidades() {
		StringBuilder cidadesStr = new StringBuilder();
		for(WebElement cidadeUf: cidadesUfs) {
			if(!cidadeUf.getText().isEmpty()) {
				String cidadesUfsStr[] = cidadeUf.getText().split("-");
				String cidadeStr = cidadesUfsStr[0].trim();
				cidadesStr.append(cidadeStr);
				cidadesStr.append(";");
			}
		}
		if(!cidadesStr.toString().isEmpty()) {
			return cidadesStr.toString().substring(0, cidadesStr.toString().length() - 1);
		}
		return cidadesStr.toString();
	}
	
	public String getUfs() {
		StringBuilder ufsStr = new StringBuilder();
		for(WebElement cidadeUf: cidadesUfs) {
			if(!cidadeUf.getText().isEmpty()) {
				String cidadesUfsStr[] = cidadeUf.getText().split("-");
				String ufStr = cidadesUfsStr[1].trim();
				ufsStr.append(ufStr);
				ufsStr.append(";");
			}
		}
		if(!ufsStr.toString().isEmpty()) {
			return ufsStr.toString().substring(0, ufsStr.toString().length() - 1);
		}
		return ufsStr.toString();
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
	
	public List<BigDecimal> getRateios() {
		LinkedList<BigDecimal> rateiosBd = new LinkedList<>();
		for(WebElement rateio: rateios) {
			String texto = rateio.getText();
			Pattern pattern = Pattern.compile("R\\$\\s[0-9\\.\\,]+");
			Matcher matcher = pattern.matcher(texto);
			if (matcher.find()) {
				texto = matcher.group().replaceAll("\\.", "").replaceAll(",", "\\.").replace("R$", "").trim();
				BigDecimal rateioBd = new BigDecimal(texto);
				rateiosBd.add(rateioBd);
			}
		}
		rateiosBd.pollLast();
		return rateiosBd;
	}
	
	public List<Integer> getNumeroGanhadores() {
		LinkedList<Integer> numeroGanhadores = new LinkedList<>();
		for(WebElement rateio: rateios) {
			String texto = rateio.getText();
			Pattern pattern = Pattern.compile("\\d+\\saposta");
			Matcher matcher = pattern.matcher(texto);
			if (matcher.find()) {
				texto = matcher.group().replaceAll("\\D", "").trim();
				Integer numeroGanhoresInt = new Integer(texto);
				numeroGanhadores.add(numeroGanhoresInt);
			}
		}
		return numeroGanhadores;
	}
	
	public Concurso paraConcursoEntity(Loteria loteria) {
		Concurso obj = new Concurso();
		obj.setArrecadacaoTotal(this.getArrecadacaoTotal());
		obj.setCidade(this.getCidades());
		obj.setUf(this.getUfs());
		obj.setEstimativaDePremioParaOProximoConcurso(this.getEstimativaDePremioParaOProximoConcurso());
		obj.setDataDoSorteio(this.getDataSorteio());
		obj.setNumero(this.getConcurso());
		obj.setLoteria(loteria);
		return obj;
	}
	
	public List<Sorteio> paraSorteiosEntityList(Concurso concurso) {
		List<Sorteio> sorteios = new LinkedList<>();
		Sorteio obj = new Sorteio();
		obj.setNumerosSorteados(this.getNumerosSorteados());
		obj.setNumero(0);
		obj.setConcurso(concurso);
		sorteios.add(obj);
		return sorteios;
	}
	
	public List<Rateio> paraRateiosEntityList(List<Sorteio> sorteios) {
		List<Rateio> rateios = new LinkedList<>();
		int tipoDePremio = 1;
		for(BigDecimal rateio: this.getRateios()) {
			Rateio obj = new Rateio();
			obj.setRateio(rateio);
			obj.setTipoDePremio(String.valueOf(tipoDePremio));
			obj.setSorteio(sorteios.get(0));
			if(tipoDePremio == 1) obj.setAcumuladoParaOProximoConcurso(this.getAcumuladoParaOProximoConcurso());
			rateios.add(obj);
			tipoDePremio++;
		}
		int i = 0;
		for(Integer numeroGanhaores: this.getNumeroGanhadores()) {
			rateios.get(i).setNumeroDeGanhadores(numeroGanhaores);
			i++;
		}
		return rateios;
	}
	
	abstract BigDecimal getEstimativaDePremioParaOProximoConcurso();
	
	abstract String getNumerosSorteados();
	
	abstract BigDecimal getAcumuladoParaOProximoConcurso();
}
