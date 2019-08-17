package com.brewer.controller.converter;

import com.brewer.model.Estilo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class EstiloConverter implements Converter<String, Estilo>{

	@Override
	public Estilo convert(String codigo) {
		if(!StringUtils.isEmpty(codigo)){
			Estilo estilo = new Estilo();
			estilo.setCodigo(Long.valueOf(codigo));
			return estilo;
		}
		return null;
	}

}
