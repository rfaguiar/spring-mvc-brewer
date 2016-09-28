package com.brewer.service;

import com.brewer.repository.Usuarios;

public enum StatusUsuario {

	ATIVAR {public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(u -> u.setAtivo(true));
			}},
	DESATIVAR {public void executar(Long[] codigos, Usuarios usuarios) {
			usuarios.findByCodigoIn(codigos).forEach(u -> u.setAtivo(false));
			}};
	
	public abstract void executar(Long[] codigos, Usuarios usuarios);
}
