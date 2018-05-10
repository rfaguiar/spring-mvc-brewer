package com.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.repository.Estilos;
import com.brewer.service.exception.NomeEstiloJaCadastradoException;
import com.brewer.model.Estilo;

@Service
public class CadastroEstiloService {

	private Estilos estilosRepo;

    @Autowired
	public CadastroEstiloService(Estilos estilosRepo) {
		this.estilosRepo = estilosRepo;
	}

	@Transactional
	public Estilo salvar(Estilo estilo) {
		Optional<Estilo> estiloOptional = estilosRepo.findByNomeIgnoreCase(estilo.getNome());
		if(estiloOptional.isPresent()){
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado");
		}
		
		return estilosRepo.saveAndFlush(estilo);
	}

}
