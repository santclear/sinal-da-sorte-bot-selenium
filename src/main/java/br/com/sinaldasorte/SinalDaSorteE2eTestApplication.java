package br.com.sinaldasorte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SinalDaSorteE2eTestApplication extends ServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SinalDaSorteE2eTestApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SinalDaSorteE2eTestApplication.class);
	}
}
