package com.brewer.builder;

import com.brewer.dto.VendaOrigem;

import java.util.ArrayList;
import java.util.List;

public final class VendaOrigemBuilder {
    private VendaOrigem vendaOrigem;

    private VendaOrigemBuilder() {
        vendaOrigem = new VendaOrigem();
    }

    public static VendaOrigemBuilder get() {
        return new VendaOrigemBuilder();
    }

    public static List<VendaOrigem> criarListaVendaOrigem() {
        List<VendaOrigem> lista = new ArrayList<>();
        lista.add(criarVendaOrigem());
        return lista;
    }

    public static VendaOrigem criarVendaOrigem() {
        return VendaOrigemBuilder.get()
                .mes("Janeiro")
                .totalNacional(new Integer(10))
                .totalInternacional(new Integer(20))
                .build();
    }

    public VendaOrigemBuilder mes(String mes) {
        vendaOrigem.setMes(mes);
        return this;
    }

    public VendaOrigemBuilder totalNacional(Integer totalNacional) {
        vendaOrigem.setTotalNacional(totalNacional);
        return this;
    }

    public VendaOrigemBuilder totalInternacional(Integer totalInternacional) {
        vendaOrigem.setTotalInternacional(totalInternacional);
        return this;
    }

    public VendaOrigemBuilder but() {
        return get().mes(vendaOrigem.getMes()).totalNacional(vendaOrigem.getTotalNacional()).totalInternacional(vendaOrigem.getTotalInternacional());
    }

    public VendaOrigem build() {
        return vendaOrigem;
    }
}
