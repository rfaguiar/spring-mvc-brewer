package com.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.model.Cidade;
import com.brewer.repository.Cidades;
import com.brewer.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CadastroCidadeService {

	private Cidades cidadesRepo;

	@Autowired
	public CadastroCidadeService(Cidades cidadesRepo) {
		this.cidadesRepo = cidadesRepo;
	}

	@Transactional
	public void salvar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = cidadesRepo.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if (cidadeExistente.isPresent()) {
			throw new NomeCidadeJaCadastradaException("Nome de cidade já cadastrado");
		}
		
		cidadesRepo.save(cidade);
	}

	
}
