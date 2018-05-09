package com.brewer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.brewer.model.Usuario;
import com.brewer.repository.Usuarios;
import com.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.brewer.service.exception.SenhaObrigatoriaUsuarioException;

@Service
public class CadastroUsuarioService {

	private Usuarios usuariosRepo;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CadastroUsuarioService(Usuarios usuariosRepo, PasswordEncoder passwordEncoder) {
		this.usuariosRepo = usuariosRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void salvar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuariosRepo.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent() && usuarioExistente.get().equals(usuario)) {
			throw new EmailUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		
		if(usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())){
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		if(usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())){
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
		}else if(usuarioExistente.isPresent() && StringUtils.isEmpty(usuario.getSenha())){
			usuario.setSenha(usuarioExistente.get().getSenha());
		}
		usuario.setConfirmacaoSenha(usuario.getSenha());
		
		if(usuarioExistente.isPresent() && !usuario.isNovo() && usuario.getAtivo() == null){
			usuario.setAtivo(usuarioExistente.get().getAtivo());
		}
		
		usuariosRepo.save(usuario);
	}

	@Transactional
	public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
		statusUsuario.executar(codigos, usuariosRepo);
	}
	
}
