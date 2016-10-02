package com.brewer.model;

import java.math.BigDecimal;

public class ItemVenda /*extends BaseEntity */{

	private static final long serialVersionUID = 1L;

	private Integer quantidade;
	private BigDecimal valorUnitario;
	private Cerveja cerveja;
	
	public BigDecimal getValorTotal(){
		return valorUnitario.multiply(new BigDecimal(quantidade));
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public Cerveja getCerveja() {
		return cerveja;
	}
	public void setCerveja(Cerveja cerveja) {
		this.cerveja = cerveja;
	}
	
}
