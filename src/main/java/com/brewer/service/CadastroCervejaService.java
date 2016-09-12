package com.brewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.repository.Cervejas;
import com.brewer.service.event.cerveja.CervejaSalvaEvent;
import com.brewer.model.Cerveja;

@Service
public class CadastroCervejaService {

	@Autowired
	private Cervejas cervejas;	

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Transactional
	public void salvar(Cerveja cerveja){
		cervejas.save(cerveja);
		
		publisher.publishEvent(new CervejaSalvaEvent(cerveja));
	}
	
}
