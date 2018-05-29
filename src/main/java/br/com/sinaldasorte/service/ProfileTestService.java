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
import br.com.sinaldasorte.pageobject.DiaDeSortePage;
import br.com.sinaldasorte.pageobject.DuplaSenaPage;
import br.com.sinaldasorte.pageobject.LotofacilPage;
import br.com.sinaldasorte.pageobject.LotomaniaPage;
import br.com.sinaldasorte.pageobject.MegaSenaPage;
import br.com.sinaldasorte.pageobject.QuinaPage;
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
			
			for(Integer i = 1645; i < 1665; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());//1660
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(1L);
				
				concurso = lotofacilPage.paraConcursoEntity(loteria);
				
				sorteios = lotofacilPage.paraSorteiosEntityList(concurso);
				
				rateios = lotofacilPage.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			
			/* Duplasena */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/");
			DuplaSenaPage duplaSenaPage = PageFactory.initElements(driver, DuplaSenaPage.class);
			
			for(Integer i = 1771; i < 1791; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());//1709
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(6L);
				
				concurso = duplaSenaPage.paraConcursoEntity(loteria);
				
				sorteios = duplaSenaPage.paraSorteiosEntityList(concurso);
					
				rateios = new LinkedList<Rateio>();
				rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 0));
				rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 1));
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			
			/* Lotomania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotomania/");
			LotomaniaPage lotomaniaPage = PageFactory.initElements(driver, LotomaniaPage.class);
			
			for(Integer i = 1848; i < 1868; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());//1863
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(4L);
				
				concurso = lotomaniaPage.paraConcursoEntity(loteria);
				
				sorteios = lotomaniaPage.paraSorteiosEntityList(concurso);
				
				rateios = lotomaniaPage.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			
			/* Timemania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania/");
			TimemaniaPage timemaniaPage = PageFactory.initElements(driver, TimemaniaPage.class);
			
			for(Integer i = 1163; i < 1183; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());//1140
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(5L);
				
				concurso = timemaniaPage.paraConcursoEntity(loteria);
				
				sorteios = timemaniaPage.paraSorteiosEntityList(concurso);
				
				rateios = timemaniaPage.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}

			/* Mega-Sena */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/");
			MegaSenaPage megaSenaPage = PageFactory.initElements(driver, MegaSenaPage.class);
			
			for(Integer i = 2022; i < 2042; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(2L);
				
				concurso = megaSenaPage.paraConcursoEntity(loteria);
				
				sorteios = megaSenaPage.paraSorteiosEntityList(concurso);
				
				rateios = megaSenaPage.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			
			/* Quina */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/quina/");
			QuinaPage quinaPage = PageFactory.initElements(driver, QuinaPage.class);
			
			for(Integer i = 4665; i < 4685; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(3L);
				
				concurso = quinaPage.paraConcursoEntity(loteria);
				
				sorteios = quinaPage.paraSorteiosEntityList(concurso);
				
				rateios = quinaPage.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			
			/* Dia de Sorte */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/diadesorte/");
			DiaDeSortePage diaDeSorte = PageFactory.initElements(driver, DiaDeSortePage.class);
			
			for(Integer i = 1; i < 5; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 250);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(3L);
				
				concurso = diaDeSorte.paraConcursoEntity(loteria);
				
				sorteios = diaDeSorte.paraSorteiosEntityList(concurso);
				
				rateios = diaDeSorte.paraRateiosEntityList(sorteios);
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
		} finally {
			driver.close();
		}	
		
	}
}