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
		if (!StringUtils.isEmpty(getCodigo())) {
			criteria.add(Restrictions.eq("codigo", getCodigo()));
		}

		if (getStatus() != null) {
			criteria.add(Restrictions.eq(Constantes.STATUS, getStatus()));
		}

		if (getDesde() != null) {
			LocalDateTime desdeDate = LocalDateTime.of(this.getDesde(), LocalTime.of(0, 0));
			criteria.add(Restrictions.ge("dataCriacao", desdeDate));
		}

		if (getAte() != null) {
			LocalDateTime ateDate = LocalDateTime.of(this.getAte(), LocalTime.of(23, 59));
			criteria.add(Restrictions.le("dataCriacao", ateDate));
		}

		if (getValorMinimo() != null) {
			criteria.add(Restrictions.ge("valorTotal", getValorMinimo()));
		}

		if (getValorMaximo() != null) {
			criteria.add(Restrictions.le("valorTotal", getValorMaximo()));
		}

        getCriteriaFiltrosCliente(criteria);

		return criteria;
    }

    private Criteria getCriteriaFiltrosCliente(Criteria criteria) {

        boolean temNomeCliente = !StringUtils.isEmpty(getNomeCliente());
        boolean temCpfOuCnpjCliente = !StringUtils.isEmpty(getCpfOuCnpjCliente());

        if (temNomeCliente || temCpfOuCnpjCliente) {
            criteria.createAlias("cliente", "c");

            if (temNomeCliente) {
                criteria.add(Restrictions.ilike("c.nome", getNomeCliente(), MatchMode.ANYWHERE));
            }

            if (temCpfOuCnpjCliente) {
                criteria.add(Restrictions.eq("c.cpfOuCnpj", TipoPessoa.removerFormatacao(cpfOuCnpjCliente)));
            }
        }
        return criteria;
    }
}