package com.brewer.builder;

import com.brewer.model.Estado;

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
                .codigo(new Long(1))
                .nome("EstadoTeste")
                .sigla("ET")
                .build();
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
