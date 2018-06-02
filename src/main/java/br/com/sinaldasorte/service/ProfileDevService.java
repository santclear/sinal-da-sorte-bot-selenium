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
public class ProfileDevService {
	private WebDriver driver;
	
	@Autowired
	private RateioService rateioService;
	@Autowired
	private LoteriaService loteriaService;
	@Autowired
	private ConcursoService concursoService;
	
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
			
			/* Lotofácil */ // 900
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotofacil/");
			LotofacilPage lotofacilPage = PageFactory.initElements(driver, LotofacilPage.class);
			loteria = loteriaService.encontre(1L);
			Integer ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(1L);
			Integer numeroConcursoInicial = ultimoNumeroConcurso + 1;
			Integer numeroConcursoFinal = lotofacilPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(lotofacilPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+lotofacilPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+lotofacilPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+lotofacilPage.getConcurso()+"...");
			}
			
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				concurso = lotofacilPage.paraConcursoEntity(loteria);
				
				sorteios = lotofacilPage.paraSorteiosEntityList(concurso);
				
				rateios = lotofacilPage.paraRateiosEntityList(sorteios);
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(lotofacilPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
			
			/* Duplasena */ // 1484
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/duplasena/");
			DuplaSenaPage duplaSenaPage = PageFactory.initElements(driver, DuplaSenaPage.class);
			loteria = loteriaService.encontre(6L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(6L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = duplaSenaPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(duplaSenaPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+duplaSenaPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+duplaSenaPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+duplaSenaPage.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				concurso = duplaSenaPage.paraConcursoEntity(loteria);
				
				sorteios = duplaSenaPage.paraSorteiosEntityList(concurso);
					
				rateios = new LinkedList<Rateio>();
				rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 0));
				rateios.addAll(duplaSenaPage.paraRateiosEntityList(sorteios, 1));
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(duplaSenaPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
			
			/* Lotomania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/lotomania/");
			LotomaniaPage lotomaniaPage = PageFactory.initElements(driver, LotomaniaPage.class);
			loteria = loteriaService.encontre(4L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(4L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = lotomaniaPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(lotomaniaPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+lotomaniaPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+lotomaniaPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+lotomaniaPage.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				concurso = lotomaniaPage.paraConcursoEntity(loteria);
				
				sorteios = lotomaniaPage.paraSorteiosEntityList(concurso);
				
				rateios = lotomaniaPage.paraRateiosEntityList(sorteios);
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(lotomaniaPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
			
			/* Timemania */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/timemania/");
			TimemaniaPage timemaniaPage = PageFactory.initElements(driver, TimemaniaPage.class);
			loteria = loteriaService.encontre(5L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(5L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = timemaniaPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(timemaniaPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+timemaniaPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+timemaniaPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+timemaniaPage.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				concurso = timemaniaPage.paraConcursoEntity(loteria);
				
				sorteios = timemaniaPage.paraSorteiosEntityList(concurso);
				
				rateios = timemaniaPage.paraRateiosEntityList(sorteios);
				
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(timemaniaPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");

			/* Mega-Sena */ // 1000
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/");
			MegaSenaPage megaSenaPage = PageFactory.initElements(driver, MegaSenaPage.class);
			loteria = loteriaService.encontre(2L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(2L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = megaSenaPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(megaSenaPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+megaSenaPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+megaSenaPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+megaSenaPage.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
								
				concurso = megaSenaPage.paraConcursoEntity(loteria);
				
				sorteios = megaSenaPage.paraSorteiosEntityList(concurso);
				
				rateios = megaSenaPage.paraRateiosEntityList(sorteios);
				
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(megaSenaPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
			
			/* Quina */ // 2630
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/quina/");
			QuinaPage quinaPage = PageFactory.initElements(driver, QuinaPage.class);
			loteria = loteriaService.encontre(3L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(3L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = quinaPage.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(quinaPage.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+quinaPage.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+quinaPage.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+quinaPage.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 1000);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				concurso = quinaPage.paraConcursoEntity(loteria);
				
				sorteios = quinaPage.paraSorteiosEntityList(concurso);
				
				rateios = quinaPage.paraRateiosEntityList(sorteios);
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(quinaPage.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
			
			/* Dia de Sorte */
			driver.get("http://loterias.caixa.gov.br/wps/portal/loterias/landing/diadesorte/");
			DiaDeSortePage diaDeSorte = PageFactory.initElements(driver, DiaDeSortePage.class);
			loteria = loteriaService.encontre(7L);
			ultimoNumeroConcurso = this.concursoService.ultimoNumeroConcurso(7L);
			numeroConcursoInicial = ultimoNumeroConcurso + 1;
			numeroConcursoFinal = diaDeSorte.getConcurso() + 1;
			if(ultimoNumeroConcurso.equals(diaDeSorte.getConcurso())) {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD "+ultimoNumeroConcurso+" é igual ao número do último concurso no site "+diaDeSorte.getConcurso());
			} else {
				System.out.println(loteria.getNome()+"::Número do último concurso no BD : "+ultimoNumeroConcurso);
				System.out.println(loteria.getNome()+"::Número do último concurso no site : "+diaDeSorte.getConcurso());
				System.out.println(loteria.getNome()+"::Salvando entre "+numeroConcursoInicial+" e "+diaDeSorte.getConcurso()+"...");
			}
			for(Integer i = numeroConcursoInicial; i < numeroConcursoFinal; i++) {
				buscaConcurso = driver.findElement(By.id("buscaConcurso"));
				buscaConcurso.sendKeys(i.toString());
				
				wait = new WebDriverWait(driver, 250);
				buscaConcurso.sendKeys(Keys.ENTER);
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='resultados']//*[contains(text(),'Concurso')]"), i.toString()));
				
				loteria = loteriaService.encontre(7L);
				
				concurso = diaDeSorte.paraConcursoEntity(loteria);
				
				sorteios = diaDeSorte.paraSorteiosEntityList(concurso);
				
				rateios = diaDeSorte.paraRateiosEntityList(sorteios);
				
				System.out.println(loteria.getNome()+"::Concurso: "+concurso.getNumero());
				rateioService.insiraTodos(rateios);
				buscaConcurso.clear();
			}
			if(!ultimoNumeroConcurso.equals(diaDeSorte.getConcurso())) System.out.println(loteria.getNome()+"::Concursos salvos!");
		} finally {
			driver.close();
		}	
	}
}