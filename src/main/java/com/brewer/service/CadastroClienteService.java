package com.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.model.Cliente;
import com.brewer.repository.Clientes;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;

@Service
public class CadastroClienteService {

	@Autowired
	private Clientes clientes;
	
	@Transactional
	public void salvar(Cliente cliente){
		
		Optional<Cliente> clientesExistentes = clientes.findByCpfOuCnpj(cliente.getCpfOuCnpjSemFormatacao());
		
		if(clientesExistentes.isPresent()){
			throw new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado");
		}
		
		clientes.save(cliente);
	}
}
