package br.com.sinaldasorte.pageobject;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LotofacilPage extends BasePage {
	
	@FindBy(xpath="//*[@ng-repeat='dezena in resultadoLinha']")
	private List<WebElement> numerosSorteados;
	
	@Override
	public List<String> getNumerosSorteados() {
		List<String> sorteios = new LinkedList<>();
		
		super.addNumerosSorteados(sorteios, this.numerosSorteados, 0, 15);

		return sorteios;
	}
}
