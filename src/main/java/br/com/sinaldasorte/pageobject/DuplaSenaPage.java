package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;

public class DuplaSenaPage extends BasePage {
	
	@FindBy(xpath="//*[contains(@ng-repeat, 'dezena in result')]")
	private List<WebElement> numerosSorteados;
	
	@FindBy(xpath="//h3[contains(text(),'Detalhamento - 1')]//following-sibling::p//strong")
	private List<WebElement> cidadesUfs1;
	
	@FindBy(xpath="//h3[contains(text(),'Detalhamento - 2')]//following-sibling::p//strong")
	private List<WebElement> cidadesUfs2;
	
	@FindBy(xpath="//h3[contains(text(),'Premiação - 1')]//following-sibling::p")
	private List<WebElement> rateios1;
	
	@FindBy(xpath="//h3[contains(text(),'Premiação - 2')]//following-sibling::p")
	private List<WebElement> rateios2;
	
	@Override
	public List<String> getNumerosSorteados() {
		List<String> sorteios = new LinkedList<>();
		
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 0, 6);
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 6, 12);
		

		return sorteios;
	}
	
	@Override
	public List<String> getCidades() {
		List<String> sorteios = new LinkedList<>();
		
		if(!cidadesUfs1.isEmpty()) super.addCidades(sorteios, cidadesUfs1);
//		else sorteios.add(";");
		else sorteios.add(null);
		if(!cidadesUfs2.isEmpty()) super.addCidades(sorteios, cidadesUfs2);
//		else sorteios.add(";");
		else sorteios.add(null);
		
		return sorteios;
	}
	
	@Override
	public List<String> getUfs() {
		List<String> sorteios = new LinkedList<>();
		
		if(!cidadesUfs1.isEmpty()) super.addUfs(sorteios, cidadesUfs1);
//		else sorteios.add(";");
		else sorteios.add(null);
		if(!cidadesUfs2.isEmpty()) super.addUfs(sorteios, cidadesUfs2);
//		else sorteios.add(";");
		else sorteios.add(null);
		
		return sorteios;
	}
	
	@Override
	public List<List<BigDecimal>> getRateios() {
		List<List<BigDecimal>> sorteios = new LinkedList<>();
		
		if(!rateios1.isEmpty()) super.addRateios(sorteios, rateios1.subList(0, 5));
		if(!rateios2.isEmpty()) super.addRateios(sorteios, rateios2);
		
		return sorteios;
	}
	
	@Override
	public List<List<Integer>> getNumeroGanhadores() {
		List<List<Integer>> sorteios = new LinkedList<>();
		
		if(!rateios1.isEmpty()) super.addNumeroGanhadores(sorteios, rateios1.subList(0, 4));
		if(!rateios2.isEmpty()) super.addNumeroGanhadores(sorteios, rateios2);
		
		return sorteios;
	}
	
	@Override
	public List<Sorteio> paraSorteiosEntityList(Concurso concurso) {
		List<Sorteio> sorteios = new LinkedList<>();

		sorteios.add(getSorteio(concurso, 0));
		sorteios.add(getSorteio(concurso, 1));
		
		return sorteios;
	}
	
	private Sorteio getSorteio(Concurso concurso, Integer numeroSorteio) {
		Sorteio obj = new Sorteio();
		obj.setNumerosSorteados(this.getNumerosSorteados().get(numeroSorteio));
		obj.setNumero(numeroSorteio + 1);
		obj.setConcurso(concurso);
		return obj;
	}
	
	public List<Rateio> paraRateiosEntityList(List<Sorteio> sorteios, Integer numeroSorteio) {
		List<Rateio> rateios = new LinkedList<>();
		int tipoDePremio = 1;
		List<BigDecimal> rateiosList = this.getRateios().get(numeroSorteio);
		for(BigDecimal rateio: rateiosList) {
			Rateio obj = new Rateio();
			obj.setRateio(rateio);
			obj.setTipoDePremio(String.valueOf(tipoDePremio));
			obj.setSorteio(sorteios.get(numeroSorteio));
			if(tipoDePremio == 1) {
				obj.setAcumuladoParaOProximoConcurso(this.getAcumuladoParaOProximoConcurso());
				obj.setCidades(this.getCidades().get(numeroSorteio));
				obj.setUfs(this.getUfs().get(numeroSorteio));
			}
			rateios.add(obj);
			tipoDePremio++;
		}
		int i = 0;
		List<Integer> numeroGanhadoresList = this.getNumeroGanhadores().get(numeroSorteio);
		for(Integer numeroGanhaores: numeroGanhadoresList) {
			rateios.get(i).setNumeroDeGanhadores(numeroGanhaores);
			i++;
		}
		return rateios;
	}
}
