package com.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.brewer.brewer.storage.FotoStorage;
import com.brewer.service.CadastroCervejaService;
import com.brewer.storage.local.FotoStorageLocal;

@Configuration
@ComponentScan(basePackageClasses = CadastroCervejaService.class)
public class ServiceConfig {

	@Bean
	public FotoStorage fotoStorage(){
		return new FotoStorageLocal();
	}
	
}
