package com.brewer.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.brewer.model.Usuario;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;		
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UsuarioSistema)) return false;
		if (!super.equals(o)) return false;
		UsuarioSistema that = (UsuarioSistema) o;
		return Objects.equals(usuario, that.usuario);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), usuario);
	}

	@Override
	public String toString() {
		return "UsuarioSistema{" +
				"usuario=" + usuario +
				'}';
	}
}
