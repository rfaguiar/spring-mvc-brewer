package com.brewer.builder;

import com.brewer.model.Cidade;
import com.brewer.model.Endereco;
import com.brewer.model.Estado;

public final class EnderecoBuilder {
    private Endereco endereco;

    private EnderecoBuilder() {
        endereco = new Endereco();
    }

    public static EnderecoBuilder get() {
        return new EnderecoBuilder();
    }

    public static Endereco criarEndereco() {
        return EnderecoBuilder.get()
                .logradouro("logTeste")
                .numero("123")
                .complemento("complemTeste")
                .cep("13179180")
                .cidade(CidadeBuilder.criarCidade())
                .estado(EstadoBuilder.criarEstado())
                .build();
    }

    public EnderecoBuilder logradouro(String logradouro) {
        endereco.setLogradouro(logradouro);
        return this;
    }

    public EnderecoBuilder numero(String numero) {
        endereco.setNumero(numero);
        return this;
    }

    public EnderecoBuilder complemento(String complemento) {
        endereco.setComplemento(complemento);
        return this;
    }

    public EnderecoBuilder cep(String cep) {
        endereco.setCep(cep);
        return this;
    }

    public EnderecoBuilder cidade(Cidade cidade) {
        endereco.setCidade(cidade);
        return this;
    }

    public EnderecoBuilder estado(Estado estado) {
        endereco.setEstado(estado);
        return this;
    }

    public EnderecoBuilder but() {
        return get().logradouro(endereco.getLogradouro()).numero(endereco.getNumero()).complemento(endereco.getComplemento()).cep(endereco.getCep()).cidade(endereco.getCidade()).estado(endereco.getEstado());
    }

    public Endereco build() {
        return endereco;
    }
}
