package com.brewer.builder;

import com.brewer.model.Grupo;
import com.brewer.model.Usuario;
import com.brewer.model.UsuarioGrupoId;

public final class UsuarioGrupoIdBuilder {
    private UsuarioGrupoId usuarioGrupoId;

    private UsuarioGrupoIdBuilder() {
        usuarioGrupoId = new UsuarioGrupoId();
    }

    public static UsuarioGrupoIdBuilder get() {
        return new UsuarioGrupoIdBuilder();
    }

    public static UsuarioGrupoId criarUsuarioGrupoIdBuilder() {
        return UsuarioGrupoIdBuilder.get()
                .usuario(UsuarioBuilder.criarUsuario())
                .grupo(GrupoBuilder.criarGrupo())
                .build();
    }

    public UsuarioGrupoIdBuilder usuario(Usuario usuario) {
        usuarioGrupoId.setUsuario(usuario);
        return this;
    }

    public UsuarioGrupoIdBuilder grupo(Grupo grupo) {
        usuarioGrupoId.setGrupo(grupo);
        return this;
    }

    public UsuarioGrupoIdBuilder but() {
        return get().usuario(usuarioGrupoId.getUsuario()).grupo(usuarioGrupoId.getGrupo());
    }

    public UsuarioGrupoId build() {
        return usuarioGrupoId;
    }
}
