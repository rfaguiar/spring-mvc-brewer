package com.brewer.service;

import com.brewer.storage.FotoStorage;
import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import com.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Service
public class CadastroCervejaService {

	@Autowired
	private Cervejas cervejas;	

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Transactional
	public void salvar(Cerveja cerveja){
		cervejas.save(cerveja);
	}
	
	@Transactional
	public void excluir(Cerveja cerveja){
		try{
			String foto = cerveja.getFoto();
			cervejas.delete(cerveja);
			cervejas.flush();
			fotoStorage.excluir(foto);
		}catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Imposivel apagar cerveja. Já foi usada em alguma venda.");
		}
	}
	
}
