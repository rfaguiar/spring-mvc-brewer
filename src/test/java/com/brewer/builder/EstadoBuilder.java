package com.brewer.builder;

import com.brewer.model.Estado;

import java.util.ArrayList;
import java.util.List;

public final class EstadoBuilder {
    private Estado estado;

    private EstadoBuilder() {
        estado = new Estado();
    }

    public static EstadoBuilder get() {
        return new EstadoBuilder();
    }

    public static Estado criarEstado() {
        return EstadoBuilder.get()
                .nome("EstadoTeste")
                .sigla("ET")
                .build();
    }

    public static List<Estado> criarListaEstados() {
        List<Estado> lista = new ArrayList<>();
        lista.add(EstadoBuilder.criarEstado());
        return lista;
    }

    public EstadoBuilder nome(String nome) {
        estado.setNome(nome);
        return this;
    }

    public EstadoBuilder sigla(String sigla) {
        estado.setSigla(sigla);
        return this;
    }

    public EstadoBuilder codigo(Long codigo) {
        estado.setCodigo(codigo);
        return this;
    }

    public EstadoBuilder but() {
        return get().nome(estado.getNome()).sigla(estado.getSigla()).codigo(estado.getCodigo());
    }

    public Estado build() {
        return estado;
    }
}
