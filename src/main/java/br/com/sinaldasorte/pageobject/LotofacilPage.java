package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;

public class LotofacilPage extends BasePage {
	
	@FindBy(xpath="//*[@ng-repeat='dezena in resultadoLinha']")
	private List<WebElement> numerosSorteados;
	
	@FindBy(xpath=""
			+ "//h3[contains(text(),'Premiação')]//following-sibling::p[1]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[2]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[3]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[4]"
			+ "|//h3[contains(text(),'Premiação')]//following-sibling::p[5]")
	private List<WebElement> rateios;
	
	@Override
	public List<String> getNumerosSorteados() {
		List<String> sorteios = new LinkedList<>();
		
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 0, 15);

		return sorteios;
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
