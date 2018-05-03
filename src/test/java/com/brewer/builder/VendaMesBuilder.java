package com.brewer.builder;

import com.brewer.dto.VendaMes;

import java.util.ArrayList;
import java.util.List;

public final class VendaMesBuilder {
    private VendaMes vendaMes;

    private VendaMesBuilder() {
        vendaMes = new VendaMes();
    }

    public static VendaMesBuilder get() {
        return new VendaMesBuilder();
    }

    public static List<VendaMes> criarListaVendaMes() {
        List<VendaMes> lista = new ArrayList<>();
        lista.add(criarVendaMes());
        return lista;
    }

    public static VendaMes criarVendaMes() {
        return VendaMesBuilder.get()
                .mes("Janeiro")
                .total(new Integer(10))
                .build();

    }

    public VendaMesBuilder mes(String mes) {
        vendaMes.setMes(mes);
        return this;
    }

    public VendaMesBuilder total(Integer total) {
        vendaMes.setTotal(total);
        return this;
    }

    public VendaMesBuilder but() {
        return get().mes(vendaMes.getMes()).total(vendaMes.getTotal());
    }

    public VendaMes build() {
        return vendaMes;
    }
}
