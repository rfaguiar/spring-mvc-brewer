package com.brewer.service;

import com.brewer.model.Cliente;
import com.brewer.repository.Clientes;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroClienteService {

	private Clientes clientesRepo;

	@Autowired
	public CadastroClienteService(Clientes clientesRepo) {
		this.clientesRepo = clientesRepo;
	}

	@Transactional
	public void salvar(Cliente cliente){
		
		Optional<Cliente> clientesExistentes = clientesRepo.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		
		if(clientesExistentes.isPresent()){
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clientesRepo.save(cliente);
	}
}
