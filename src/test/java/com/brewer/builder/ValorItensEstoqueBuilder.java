package com.brewer.builder;

import com.brewer.dto.ValorItensEstoque;

import java.math.BigDecimal;

public final class ValorItensEstoqueBuilder {
    private ValorItensEstoque valorItensEstoque;

    private ValorItensEstoqueBuilder() {
        valorItensEstoque = new ValorItensEstoque();
    }

    public static ValorItensEstoqueBuilder get() {
        return new ValorItensEstoqueBuilder();
    }

    public static ValorItensEstoque criarValorItensEstoque() {
        return ValorItensEstoqueBuilder.get()
                .valor(new BigDecimal(10))
                .totalItens(new Long(2))
                .build();
    }

    public ValorItensEstoqueBuilder valor(BigDecimal valor) {
        valorItensEstoque.setValor(valor);
        return this;
    }

    public ValorItensEstoqueBuilder totalItens(Long totalItens) {
        valorItensEstoque.setTotalItens(totalItens);
        return this;
    }

    public ValorItensEstoqueBuilder but() {
        return get().valor(valorItensEstoque.getValor()).totalItens(valorItensEstoque.getTotalItens());
    }

    public ValorItensEstoque build() {
        return valorItensEstoque;
    }
}
