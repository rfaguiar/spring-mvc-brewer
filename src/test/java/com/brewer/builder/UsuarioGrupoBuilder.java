package com.brewer.builder;

import com.brewer.model.UsuarioGrupo;
import com.brewer.model.UsuarioGrupoId;

public final class UsuarioGrupoBuilder {
    private UsuarioGrupo usuarioGrupo;

    private UsuarioGrupoBuilder() {
        usuarioGrupo = new UsuarioGrupo();
    }

    public static UsuarioGrupoBuilder get() {
        return new UsuarioGrupoBuilder();
    }

    public static UsuarioGrupo criarUsuarioGrupoId() {
        return UsuarioGrupoBuilder.get()
                .id(UsuarioGrupoIdBuilder.criarUsuarioGrupoIdBuilder())
                .build();
    }

    public UsuarioGrupoBuilder id(UsuarioGrupoId id) {
        usuarioGrupo.setId(id);
        return this;
    }

    public UsuarioGrupoBuilder but() {
        return get().id(usuarioGrupo.getId());
    }

    public UsuarioGrupo build() {
        return usuarioGrupo;
    }
}
