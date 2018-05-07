package com.brewer.builder;

import com.brewer.model.Permissao;

import java.util.ArrayList;
import java.util.List;

public final class PermissaoBuilder {
    private Permissao permissao;

    private PermissaoBuilder() {
        permissao = new Permissao();
    }

    public static PermissaoBuilder get() {
        return new PermissaoBuilder();
    }

    public static List<Permissao> criarListaPermissao() {
        List<Permissao> lista = new ArrayList<>();
        lista.add(PermissaoBuilder.criarPermissao());
        return lista;
    }

    private static Permissao criarPermissao() {
        return PermissaoBuilder.get()
                .nome("nomePermissao")
                .build();
    }

    public PermissaoBuilder nome(String nome) {
        permissao.setNome(nome);
        return this;
    }

    public PermissaoBuilder codigo(Long codigo) {
        permissao.setCodigo(codigo);
        return this;
    }

    public PermissaoBuilder but() {
        return get().nome(permissao.getNome()).codigo(permissao.getCodigo());
    }

    public Permissao build() {
        return permissao;
    }
}
