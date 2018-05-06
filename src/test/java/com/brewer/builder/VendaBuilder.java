package com.brewer.builder;

import com.brewer.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class VendaBuilder {
    private Venda venda;

    private VendaBuilder() {
        venda = new Venda();
    }

    public static VendaBuilder get() {
        return new VendaBuilder();
    }

    public static Venda criarVenda() {
        return VendaBuilder.get()
                .codigo(new Long(1))
                .dataCriacao(LocalDateTime.now())
                .valorFrete(new BigDecimal(123))
                .valorTotal(new BigDecimal(456))
                .valorDesconto(new BigDecimal(2))
                .observacao("observacaoTeste")
                .dataHoraEntrega(LocalDateTime.now())
                .cliente(ClienteBuilder.criarCliente())
                .usuario(UsuarioBuilder.criarUsuario())
                .status(StatusVenda.EMITIDA)
                .itens(ItemVendaBuilder.criarListaItenVenda())
                .build();
    }

    public static List<Venda> criarListaVenda() {
        List<Venda> lista = new ArrayList<>();
        lista.add(criarVenda());
        return lista;
    }

    public VendaBuilder codigo(Long codigo) {
        venda.setCodigo(codigo);
        return this;
    }

    public VendaBuilder dataCriacao(LocalDateTime dataCriacao) {
        venda.setDataCriacao(dataCriacao);
        return this;
    }

    public VendaBuilder valorFrete(BigDecimal valorFrete) {
        venda.setValorFrete(valorFrete);
        return this;
    }

    public VendaBuilder valorDesconto(BigDecimal valorDesconto) {
        venda.setValorDesconto(valorDesconto);
        return this;
    }

    public VendaBuilder valorTotal(BigDecimal valorTotal) {
        venda.setValorTotal(valorTotal);
        return this;
    }

    public VendaBuilder observacao(String observacao) {
        venda.setObservacao(observacao);
        return this;
    }

    public VendaBuilder dataHoraEntrega(LocalDateTime dataHoraEntrega) {
        venda.setDataHoraEntrega(dataHoraEntrega);
        return this;
    }

    public VendaBuilder cliente(Cliente cliente) {
        venda.setCliente(cliente);
        return this;
    }

    public VendaBuilder usuario(Usuario usuario) {
        venda.setUsuario(usuario);
        return this;
    }

    public VendaBuilder status(StatusVenda status) {
        venda.setStatus(status);
        return this;
    }

    public VendaBuilder itens(List<ItemVenda> itens) {
        venda.setItens(itens);
        return this;
    }

    public VendaBuilder uuid(String uuid) {
        venda.setUuid(uuid);
        return this;
    }

    public VendaBuilder dataEntrega(LocalDate dataEntrega) {
        venda.setDataEntrega(dataEntrega);
        return this;
    }

    public VendaBuilder horarioEntrega(LocalTime horarioEntrega) {
        venda.setHorarioEntrega(horarioEntrega);
        return this;
    }

    public VendaBuilder but() {
        return get().codigo(venda.getCodigo()).dataCriacao(venda.getDataCriacao()).valorFrete(venda.getValorFrete()).valorDesconto(venda.getValorDesconto()).valorTotal(venda.getValorTotal()).observacao(venda.getObservacao()).dataHoraEntrega(venda.getDataHoraEntrega()).cliente(venda.getCliente()).usuario(venda.getUsuario()).status(venda.getStatus()).itens(venda.getItens()).uuid(venda.getUuid()).dataEntrega(venda.getDataEntrega()).horarioEntrega(venda.getHorarioEntrega());
    }

    public Venda build() {
        return venda;
    }
}
