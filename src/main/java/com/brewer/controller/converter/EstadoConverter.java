package com.brewer.controller.converter;

import com.brewer.model.Estado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class EstadoConverter implements Converter<String, Estado>{

	@Override
	public Estado convert(String codigo) {
		if(!StringUtils.isEmpty(codigo)){
			Estado estado = new Estado();
			estado.setCodigo(Long.valueOf(codigo));
			return estado;
		}
		return null;
	}
}
