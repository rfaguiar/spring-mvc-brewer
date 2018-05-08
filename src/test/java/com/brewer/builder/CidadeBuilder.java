package com.brewer.builder;

import com.brewer.model.Cidade;
import com.brewer.model.Estado;

import java.util.ArrayList;
import java.util.List;

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
                .nome("cidadeTeste")
                .estado(EstadoBuilder.criarEstado())
                .build();
    }

    public static List<Cidade> criarListaCidade() {
        List<Cidade> lista = new ArrayList<>();
        lista.add(CidadeBuilder.criarCidade());
        return lista;
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
