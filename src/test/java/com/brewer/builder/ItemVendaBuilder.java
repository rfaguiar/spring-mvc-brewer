package com.brewer.builder;

import com.brewer.model.Cerveja;
import com.brewer.model.ItemVenda;
import com.brewer.model.Venda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ItemVendaBuilder {
    private ItemVenda itemVenda;

    private ItemVendaBuilder() {
        itemVenda = new ItemVenda();
    }

    public static ItemVendaBuilder get() {
        return new ItemVendaBuilder();
    }

    public static List<ItemVenda> criarListaItenVenda() {
        List<ItemVenda> lista = new ArrayList<>();
        lista.add(ItemVendaBuilder.criarItemVenda());
        return lista;
    }

    private static ItemVenda criarItemVenda() {
        return ItemVendaBuilder.get()
                .codigo(new Long(1))
                .quantidade(10)
                .valorUnitario(new BigDecimal(2))
                .cerveja(CervejaBuilder.criarCerveja())
                .build();
    }

    public static boolean validarListaItensVenda(List<ItemVenda> lista1, List<ItemVenda> lista2) {
        return validarItemVenda(lista1.get(0), lista2.get(0));
    }

    public static boolean validarItemVenda(ItemVenda itemVenda1, ItemVenda itemVenda2) {
        return itemVenda1.getCodigo().equals(itemVenda2.getCodigo()) &&
                itemVenda1.getQuantidade().equals(itemVenda2.getQuantidade()) &&
                itemVenda1.getValorTotal().equals(itemVenda2.getValorTotal()) &&
                itemVenda1.getValorUnitario().equals(itemVenda2.getValorUnitario());
    }

    public ItemVendaBuilder quantidade(Integer quantidade) {
        itemVenda.setQuantidade(quantidade);
        return this;
    }

    public ItemVendaBuilder codigo(Long codigo) {
        itemVenda.setCodigo(codigo);
        return this;
    }

    public ItemVendaBuilder valorUnitario(BigDecimal valorUnitario) {
        itemVenda.setValorUnitario(valorUnitario);
        return this;
    }

    public ItemVendaBuilder cerveja(Cerveja cerveja) {
        itemVenda.setCerveja(cerveja);
        return this;
    }

    public ItemVendaBuilder venda(Venda venda) {
        itemVenda.setVenda(venda);
        return this;
    }

    public ItemVendaBuilder but() {
        return get().quantidade(itemVenda.getQuantidade()).codigo(itemVenda.getCodigo()).valorUnitario(itemVenda.getValorUnitario()).cerveja(itemVenda.getCerveja()).venda(itemVenda.getVenda());
    }

    public ItemVenda build() {
        return itemVenda;
    }
}
