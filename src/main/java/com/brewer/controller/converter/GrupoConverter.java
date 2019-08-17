package com.brewer.controller.converter;

import com.brewer.model.Grupo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class GrupoConverter implements Converter<String, Grupo> {

	@Override
	public Grupo convert(String codigo) {
		if(!StringUtils.isEmpty(codigo)){
			Grupo grupo = new Grupo();
			grupo.setCodigo(Long.valueOf(codigo));
			return grupo;
		}
		return null;
	}

}
