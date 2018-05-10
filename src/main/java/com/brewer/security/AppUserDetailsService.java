package com.brewer.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brewer.model.Usuario;
import com.brewer.repository.Usuarios;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private Usuarios usuariosRepo;

	@Autowired
	public AppUserDetailsService(Usuarios usuariosRepo) {
		this.usuariosRepo = usuariosRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		Optional<Usuario> usuarioOptional = usuariosRepo.porEmailEAtivo(email);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		
		
		
		return new UsuarioSistema(usuario, getPermissoes(usuario));
		
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		//LISTA de permissoes do usuario
		List<String> permissoes = usuariosRepo.permissoes(usuario);
		permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));
		
		return authorities;
	}

}
