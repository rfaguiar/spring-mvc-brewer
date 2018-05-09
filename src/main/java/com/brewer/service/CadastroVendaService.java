package com.brewer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.brewer.service.exception.VendaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brewer.model.StatusVenda;
import com.brewer.model.Venda;
import com.brewer.repository.Vendas;
import com.brewer.service.event.venda.VendaEvent;

@Service
public class CadastroVendaService {

	private Vendas vendas;
	private ApplicationEventPublisher publish;

    @Autowired
	public CadastroVendaService(Vendas vendas, ApplicationEventPublisher publish) {
		this.vendas = vendas;
		this.publish = publish;
	}

	@Transactional
	public Venda salvar(Venda venda) {
		if(venda.isSalvarProibido()){
			throw new VendaException("Usúario tentando salvar uma venda proibida");
		}
		if (venda.isNova()) {
			venda.setDataCriacao(LocalDateTime.now());
		} else {
			Venda vendaExistente = vendas.findOne(venda.getCodigo());
			venda.setDataCriacao(vendaExistente.getDataCriacao());
		}
		
		if (venda.getDataEntrega() != null) {
			venda.setDataHoraEntrega(LocalDateTime.of(venda.getDataEntrega()
					, venda.getHorarioEntrega() != null ? venda.getHorarioEntrega() : LocalTime.NOON));
		}
		
		return vendas.saveAndFlush(venda);
	}

	@Transactional
	public void emitir(Venda venda) {
		venda.setStatus(StatusVenda.EMITIDA);
		salvar(venda);
		
		publish.publishEvent(new VendaEvent(venda));
	}
	
	@PreAuthorize("#venda.usuario == principal.usuario or hasRole('CANCELAR_VENDA')")
	@Transactional
	public void cancelar(Venda venda) {
		Venda vendaExistente = vendas.findOne(venda.getCodigo());
		
		vendaExistente.setStatus(StatusVenda.CANCELADA);
		vendas.save(vendaExistente);
	}

}
