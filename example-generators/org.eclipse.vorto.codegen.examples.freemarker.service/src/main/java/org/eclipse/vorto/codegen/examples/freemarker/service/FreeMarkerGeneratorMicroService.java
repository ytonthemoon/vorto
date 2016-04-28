package org.eclipse.vorto.codegen.examples.freemarker.service;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.examples.FreeMarker;
import org.eclipse.vorto.service.generator.web.AbstractBackendCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FreeMarkerGeneratorMicroService extends AbstractBackendCodeGenerator {

	@Bean
	public IVortoCodeGenerator freemarker() {
		return new FreeMarker();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(FreeMarkerGeneratorMicroService.class, args);
	}
}
