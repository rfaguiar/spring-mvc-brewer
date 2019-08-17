package com.brewer.config;

import com.brewer.service.CadastroCervejaService;
import com.brewer.storage.FotoStorage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {CadastroCervejaService.class, FotoStorage.class})
public class ServiceConfig {

}
