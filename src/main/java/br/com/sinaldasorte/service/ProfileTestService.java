package br.com.sinaldasorte.service;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sinaldasorte.domain.Concurso;
import br.com.sinaldasorte.domain.Loteria;
import br.com.sinaldasorte.domain.Rateio;
import br.com.sinaldasorte.domain.Sorteio;
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
			Loteria loteria = loteriaService.encontre(1L);
			
			Concurso concurso = lotofacilPage.paraConcursoEntity(loteria);
			concurso.setAcumuladoEspecial(lotofacilPage.getAcumuladoEspecial());
			
			List<Sorteio> sorteios = lotofacilPage.paraSorteiosEntityList(concurso);
			
			List<Rateio> rateios = lotofacilPage.paraRateiosEntityList(sorteios);
			rateioService.insiraTodos(rateios);
			
		} finally {
			driver.close();
		}	
		
	}
}