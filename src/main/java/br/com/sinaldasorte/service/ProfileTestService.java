package br.com.sinaldasorte.service;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.domain.Loteria;
import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;
import br.com.sinaldasorte.pageobject.DuplaSenaPage;
import br.com.sinaldasorte.pageobject.LotofacilPage;

@Service
public class ProfileTestService {
	private WebDriver driver;
	
	@Autowired
	private RateioService rateioService;
	@Autowired
	private LoteriaService loteriaService;
	
	public void init() {
		try {
			System.setProperty("webdriver.chrome.driver", "/home/santclear/webdrivers/chromedriver");
			driver = new ChromeDriver();
			
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil/");
			LotofacilPage lotofacilPage = PageFactory.initElements(driver, LotofacilPage.class);
			
			WebElement buscaConcurso = driver.findElement(By.id("buscaConcurso"));
			buscaConcurso.sendKeys("1658");
			
			WebDriverWait wait = new WebDriverWait(driver, 1000);
			buscaConcurso.sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1658"));
			
			Loteria loteria = loteriaService.encontre(1L);
			
			Concurso concurso = lotofacilPage.paraConcursoEntity(loteria);
			
			List<Sorteio> sorteios = lotofacilPage.paraSorteiosEntityList(concurso);
			
			List<Rateio> rateios = lotofacilPage.paraRateiosEntityList(sorteios);
			rateioService.insiraTodos(rateios);
			
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/");
			DuplaSenaPage duplaSenaPage = PageFactory.initElements(driver, DuplaSenaPage.class);
			
//			WebElement buscaConcurso = driver.findElement(By.id("buscaConcurso"));
//			buscaConcurso.sendKeys("1630");
//			
//			WebDriverWait wait = new WebDriverWait(driver, 1000);
//			buscaConcurso.sendKeys(Keys.ENTER);
//			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1630"));
			
			loteria = loteriaService.encontre(6L);
			
			concurso = duplaSenaPage.paraConcursoEntity(loteria);
			
			sorteios = duplaSenaPage.paraSorteiosEntityList(concurso);
				
			rateios = new LinkedList<Rateio>();
			rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 0));
			rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 1));
			rateioService.insiraTodos(rateios);
			
		} finally {
			driver.close();
		}	
		
	}
}