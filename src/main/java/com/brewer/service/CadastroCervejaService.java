package com.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.repository.Cervejas;
import com.brewer.model.Cerveja;

@Service
public class CadastroCervejaService {

	@Autowired
	private Cervejas cervejas;
	
	@Transactional
	public void salvar(Cerveja cerveja){
		cervejas.save(cerveja);
	}
	
}
