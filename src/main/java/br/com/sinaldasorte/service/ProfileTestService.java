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
import br.com.sinaldasorte.pageobject.LotomaniaPage;
import br.com.sinaldasorte.pageobject.TimemaniaPage;

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
			WebElement buscaConcurso = null;
			WebDriverWait wait = null;
			Loteria loteria = null;
			Concurso concurso = null;
			List<Sorteio> sorteios = null;
			List<Rateio> rateios = null;
			
			/* Lotofácil */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil/");
			LotofacilPage lotofacilPage = PageFactory.initElements(driver, LotofacilPage.class);
			
			buscaConcurso = driver.findElement(By.id("buscaConcurso"));
			buscaConcurso.sendKeys("1660");
			
			wait = new WebDriverWait(driver, 1000);
			buscaConcurso.sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1660"));
			
			loteria = loteriaService.encontre(1L);
			
			concurso = lotofacilPage.paraConcursoEntity(loteria);
			
			sorteios = lotofacilPage.paraSorteiosEntityList(concurso);
			
			rateios = lotofacilPage.paraRateiosEntityList(sorteios);
			rateioService.insiraTodos(rateios);
			
			/* Duplasena */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/");
			DuplaSenaPage duplaSenaPage = PageFactory.initElements(driver, DuplaSenaPage.class);
			
			buscaConcurso = driver.findElement(By.id("buscaConcurso"));
			buscaConcurso.sendKeys("1709");
			
			wait = new WebDriverWait(driver, 1000);
			buscaConcurso.sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1709"));
			
			loteria = loteriaService.encontre(6L);
			
			concurso = duplaSenaPage.paraConcursoEntity(loteria);
			
			sorteios = duplaSenaPage.paraSorteiosEntityList(concurso);
				
			rateios = new LinkedList<Rateio>();
			rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 0));
			rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 1));
			rateioService.insiraTodos(rateios);
			
			/* Lotomania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotomania/");
			LotomaniaPage lotomaniaPage = PageFactory.initElements(driver, LotomaniaPage.class);
			
			buscaConcurso = driver.findElement(By.id("buscaConcurso"));
			buscaConcurso.sendKeys("1863");
			
			wait = new WebDriverWait(driver, 1000);
			buscaConcurso.sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1863"));
			
			loteria = loteriaService.encontre(4L);
			
			concurso = lotomaniaPage.paraConcursoEntity(loteria);
			
			sorteios = lotomaniaPage.paraSorteiosEntityList(concurso);
			
			rateios = lotomaniaPage.paraRateiosEntityList(sorteios);
			rateioService.insiraTodos(rateios);
			
			/* Timemania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania/");
			TimemaniaPage timemaniaPage = PageFactory.initElements(driver, TimemaniaPage.class);
			
			buscaConcurso = driver.findElement(By.id("buscaConcurso"));
			buscaConcurso.sendKeys("1140");
			
			wait = new WebDriverWait(driver, 1000);
			buscaConcurso.sendKeys(Keys.ENTER);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), "1140"));
			
			loteria = loteriaService.encontre(5L);
			
			concurso = timemaniaPage.paraConcursoEntity(loteria);
			
			sorteios = timemaniaPage.paraSorteiosEntityList(concurso);
			
			rateios = timemaniaPage.paraRateiosEntityList(sorteios);
			rateioService.insiraTodos(rateios);
			
		} finally {
			driver.close();
		}	
		
	}
}