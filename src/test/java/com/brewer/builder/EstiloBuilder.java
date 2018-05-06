package com.brewer.builder;

import com.brewer.model.Estilo;

import java.util.ArrayList;
import java.util.List;

public final class EstiloBuilder {

    private Estilo estilo;

    private EstiloBuilder() {
        estilo = new Estilo();
    }

    public static EstiloBuilder get() {
        return new EstiloBuilder();
    }

    public EstiloBuilder nome(String nome) {
        estilo.setNome(nome);
        return this;
    }

    public EstiloBuilder codigo(Long codigo) {
        estilo.setCodigo(codigo);
        return this;
    }

    public EstiloBuilder but() {
        return get().nome(estilo.getNome()).codigo(estilo.getCodigo());
    }

    public Estilo build() {
        return estilo;
    }


    public static boolean validarEstilo(Estilo estilo1, Estilo estilo2) {
        return estilo1.equals(estilo2);
    }

    public static boolean validarListaEstilo(List<Estilo> listaEstilos1, List<Estilo> listaEstilos2) {
        return validarEstilo(listaEstilos1.get(0), listaEstilos2.get(0));
    }

    public static List<Estilo> criarListaEstilos() {
        Estilo estilo = criarEstilo();
        List<Estilo> listaEstilos = new ArrayList<>();
        listaEstilos.add(estilo);
        return listaEstilos;
    }

    public static Estilo criarEstilo(){
        return EstiloBuilder.get()
                .nome("estiloTest")
                .build();
    }
}
