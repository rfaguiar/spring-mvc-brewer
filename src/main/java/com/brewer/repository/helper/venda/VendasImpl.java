package com.brewer.repository.helper.venda;

import com.brewer.Constantes;
import com.brewer.dto.VendaMes;
import com.brewer.dto.VendaOrigem;
import com.brewer.model.StatusVenda;
import com.brewer.model.Venda;
import com.brewer.repository.filter.VendaFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public class VendasImpl implements VendasQueries {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

    public VendasImpl() {}

    public VendasImpl(EntityManager entityManager, PaginacaoUtil paginacaoUtil) {
        this.entityManager = entityManager;
        this.paginacaoUtil = paginacaoUtil;
    }

    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);

        List list = criteria.list();
        Long total = total(filtro);
        return new PageImpl<>(list, pageable, total);
	}
	

	@Transactional(readOnly = true)
	@Override
	public Venda buscarComItens(Long codigo) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("codigo", codigo));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        Object result = criteria.uniqueResult();
        return (Venda) result;
	}

	@Override
	public BigDecimal valorTotalNoAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				entityManager.createQuery("select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
					.setParameter("ano", Year.now().getValue())
					.setParameter(Constantes.STATUS, StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTotalNoMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				entityManager.createQuery("select sum(valorTotal) from Venda where month(dataCriacao) = :mes and status = :status", BigDecimal.class)
					.setParameter("mes", MonthDay.now().getMonthValue())
					.setParameter(Constantes.STATUS, StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTicketMedioNoAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(
				entityManager.createQuery("select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
					.setParameter("ano", Year.now().getValue())
					.setParameter(Constantes.STATUS, StatusVenda.EMITIDA)
					.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendaMes> totalPorMes() {
		List<VendaMes> vendasMes = entityManager.createNamedQuery("Vendas.totalPorMes").getResultList();
		LocalDate hoje = LocalDate.now();

		for(int i = 1; i <= 6; i++){
			String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
			Optional<VendaMes> findOpt = vendasMes.stream()
                                                .filter(v -> mesIdeal.equals(v.getMes()))
                                                .findAny();
			if(!findOpt.isPresent()){
				vendasMes.add(i -1, new VendaMes(mesIdeal, 0));
			}
			hoje = hoje.minusMonths(1);
		}
		
		return vendasMes;
	}
	
	@Override
	public List<VendaOrigem> totalPorOrigem() {
		List<VendaOrigem> vendasNacionalidade = entityManager.createNamedQuery("Vendas.porOrigem", VendaOrigem.class).getResultList();
		
		LocalDate now = LocalDate.now();
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", now.getYear(), now.getMonth().getValue());
            Optional<VendaOrigem> findOpt = vendasNacionalidade.stream()
                                            .filter(v -> v.getMes().equals(mesIdeal))
                                            .findAny();
			if (!findOpt.isPresent()) {
				vendasNacionalidade.add(i - 1, new VendaOrigem(mesIdeal, 0, 0));
			}
			
			now = now.minusMonths(1);
		}
		
		return vendasNacionalidade;
	}
	
	private Long total(VendaFilter filtro) {
		Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Venda.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltro(VendaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			filtro.getCriteriaFiltros(criteria);
		}
	}



}
