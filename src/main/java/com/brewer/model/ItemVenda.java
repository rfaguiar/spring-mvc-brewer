package com.brewer.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item_venda")
public class ItemVenda extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer quantidade;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	@ManyToOne
	@JoinColumn(name = "codigo_cerveja")
	private Cerveja cerveja;

	@ManyToOne
	@JoinColumn(name = "codigo_venda")
	private Venda venda;

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

	public BigDecimal getValorTotal() {
		return valorUnitario.multiply(new BigDecimal(quantidade));
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemVenda)) return false;
        if (!super.equals(o)) return false;
        ItemVenda itemVenda = (ItemVenda) o;
        return Objects.equals(quantidade, itemVenda.quantidade) &&
                Objects.equals(valorUnitario, itemVenda.valorUnitario);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), quantidade, valorUnitario);
    }

    @Override
    public String toString() {
        return "ItemVenda{" +
                "quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                '}';
    }
}
