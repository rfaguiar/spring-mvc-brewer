package com.brewer.builder;

import com.brewer.model.Grupo;
import com.brewer.model.Permissao;

import java.util.ArrayList;
import java.util.List;

public final class GrupoBuilder {
    private Grupo grupo;

    private GrupoBuilder() {
        grupo = new Grupo();
    }

    public static GrupoBuilder get() {
        return new GrupoBuilder();
    }

    public static List<Grupo> criarListaGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        grupos.add(GrupoBuilder.criarGrupo());
        return grupos;
    }

    public static Grupo criarGrupo() {
        return GrupoBuilder.get()
                .nome("nomeGrupoTeste")
                .permissoes(PermissaoBuilder.criarListaPermissao())
                .build();
    }

    public GrupoBuilder nome(String nome) {
        grupo.setNome(nome);
        return this;
    }

    public GrupoBuilder codigo(Long codigo) {
        grupo.setCodigo(codigo);
        return this;
    }

    public GrupoBuilder permissoes(List<Permissao> permissoes) {
        grupo.setPermissoes(permissoes);
        return this;
    }

    public GrupoBuilder but() {
        return get().nome(grupo.getNome()).codigo(grupo.getCodigo()).permissoes(grupo.getPermissoes());
    }

    public Grupo build() {
        return grupo;
    }
}
