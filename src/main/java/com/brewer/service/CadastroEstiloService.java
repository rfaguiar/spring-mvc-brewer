package com.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.repository.Estilos;
import com.brewer.model.Estilo;

@Service
public class CadastroEstiloService {

	@Autowired
	private Estilos estilos;
	
	@Transactional
	public void salvar(Estilo estilo) {
		estilos.save(estilo);		
	}

}
