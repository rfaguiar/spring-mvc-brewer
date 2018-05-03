package com.brewer.builder;

import com.brewer.model.Cidade;
import com.brewer.model.Estado;

public final class CidadeBuilder {
    private Cidade cidade;

    private CidadeBuilder() {
        cidade = new Cidade();
    }

    public static CidadeBuilder get() {
        return new CidadeBuilder();
    }

    public static Cidade criarCidade() {
        return CidadeBuilder.get()
                .codigo(new Long(1))
                .nome("cidadeTeste")
                .estado(EstadoBuilder.criarEstado())
                .build();
    }

    public CidadeBuilder nome(String nome) {
        cidade.setNome(nome);
        return this;
    }

    public CidadeBuilder codigo(Long codigo) {
        cidade.setCodigo(codigo);
        return this;
    }

    public CidadeBuilder estado(Estado estado) {
        cidade.setEstado(estado);
        return this;
    }

    public CidadeBuilder but() {
        return get().nome(cidade.getNome()).codigo(cidade.getCodigo()).estado(cidade.getEstado());
    }

    public Cidade build() {
        return cidade;
    }
}
