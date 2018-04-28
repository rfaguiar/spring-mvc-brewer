package com.brewer.repository.filter;

import com.brewer.Constantes;
import com.brewer.model.StatusVenda;
import com.brewer.model.TipoPessoa;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VendaFilter {

	private Long codigo;
	private StatusVenda status;

	private LocalDate desde;
	private LocalDate ate;
	private BigDecimal valorMinimo;
	private BigDecimal valorMaximo;

	private String nomeCliente;
	private String cpfOuCnpjCliente;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public LocalDate getDesde() {
		return desde;
	}

	public void setDesde(LocalDate desde) {
		this.desde = desde;
	}

	public LocalDate getAte() {
		return ate;
	}

	public void setAte(LocalDate ate) {
		this.ate = ate;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCpfOuCnpjCliente() {
		return cpfOuCnpjCliente;
	}

	public void setCpfOuCnpjCliente(String cpfOuCnpjCliente) {
		this.cpfOuCnpjCliente = cpfOuCnpjCliente;
	}

    public Criteria getCriteriaFiltros(Criteria criteria) {
		if (!StringUtils.isEmpty(codigo)) {
			criteria.add(Restrictions.eq("codigo", codigo));
		}

		if (status != null) {
			criteria.add(Restrictions.eq(Constantes.STATUS, status));
		}

		if (desde != null) {
			LocalDateTime desdeDate = LocalDateTime.of(this.desde, LocalTime.of(0, 0));
			criteria.add(Restrictions.ge("dataCriacao", desdeDate));
		}

		if (ate != null) {
			LocalDateTime ateDate = LocalDateTime.of(this.ate, LocalTime.of(23, 59));
			criteria.add(Restrictions.le("dataCriacao", ateDate));
		}

		if (valorMinimo != null) {
			criteria.add(Restrictions.ge("valorTotal", valorMinimo));
		}

		if (valorMaximo != null) {
			criteria.add(Restrictions.le("valorTotal", valorMaximo));
		}

		if (!StringUtils.isEmpty(nomeCliente)) {
			criteria.add(Restrictions.ilike("c.nome", nomeCliente, MatchMode.ANYWHERE));
		}

		if (!StringUtils.isEmpty(cpfOuCnpjCliente)) {
			criteria.add(Restrictions.eq("c.cpfOuCnpj", TipoPessoa.removerFormatacao(cpfOuCnpjCliente)));
		}
		return criteria;
    }
}