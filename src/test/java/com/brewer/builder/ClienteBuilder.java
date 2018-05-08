package com.brewer.builder;

import com.brewer.model.Cliente;
import com.brewer.model.Endereco;
import com.brewer.model.TipoPessoa;

import java.util.ArrayList;
import java.util.List;

public final class ClienteBuilder {
    private Cliente cliente;

    private ClienteBuilder() {
        cliente = new Cliente();
    }

    public static ClienteBuilder get() {
        return new ClienteBuilder();
    }

    public static Cliente criarCliente() {
        return ClienteBuilder.get()
                .nome("nomeCliente")
                .tipoPessoa(TipoPessoa.FISICA)
                .cpfOuCnpj("82078350877")
                .telefone("19999999999")
                .email("email@teste.com")
                .endereco(EnderecoBuilder.criarEndereco())
                .build();
    }

    public static List<Cliente> criarListaCliente() {
        List<Cliente> lista = new ArrayList<>();
        lista.add(ClienteBuilder.criarCliente());
        return lista;
    }

    public ClienteBuilder codigo(Long codigo) {
        cliente.setCodigo(codigo);
        return this;
    }

    public ClienteBuilder nome(String nome) {
        cliente.setNome(nome);
        return this;
    }

    public ClienteBuilder tipoPessoa(TipoPessoa tipoPessoa) {
        cliente.setTipoPessoa(tipoPessoa);
        return this;
    }

    public ClienteBuilder cpfOuCnpj(String cpfOuCnpj) {
        cliente.setCpfOuCnpj(cpfOuCnpj);
        return this;
    }

    public ClienteBuilder telefone(String telefone) {
        cliente.setTelefone(telefone);
        return this;
    }

    public ClienteBuilder email(String email) {
        cliente.setEmail(email);
        return this;
    }

    public ClienteBuilder endereco(Endereco endereco) {
        cliente.setEndereco(endereco);
        return this;
    }

    public ClienteBuilder but() {
        return get().codigo(cliente.getCodigo()).nome(cliente.getNome()).tipoPessoa(cliente.getTipoPessoa()).cpfOuCnpj(cliente.getCpfOuCnpj()).telefone(cliente.getTelefone()).email(cliente.getEmail()).endereco(cliente.getEndereco());
    }

    public Cliente build() {
        return cliente;
    }
}
