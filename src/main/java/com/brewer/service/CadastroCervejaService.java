package com.brewer.service;

import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import com.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.brewer.storage.FotoStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

@Service
public class CadastroCervejaService {

	private Cervejas cervejasRepo;
	private FotoStorage fotoStorage;

	@Autowired
	public CadastroCervejaService(Cervejas cervejasRepo, FotoStorage fotoStorage) {
		this.cervejasRepo = cervejasRepo;
		this.fotoStorage = fotoStorage;
	}

	@Transactional
	public void salvar(Cerveja cerveja){
		cervejasRepo.save(cerveja);
	}
	
	@Transactional
	public void excluir(Cerveja cerveja){
		try{
			String foto = cerveja.getFoto();
			cervejasRepo.delete(cerveja);
			cervejasRepo.flush();
			fotoStorage.excluir(foto);
		}catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Imposivel apagar cerveja. Já foi usada em alguma venda.");
		}
	}
	
}
