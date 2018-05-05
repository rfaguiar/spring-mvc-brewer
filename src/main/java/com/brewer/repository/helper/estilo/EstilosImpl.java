package com.brewer.repository.helper.estilo;

import com.brewer.model.Estilo;
import com.brewer.repository.filter.EstiloFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class EstilosImpl implements EstilosQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	public EstilosImpl() {}

	public EstilosImpl(EntityManager manager, PaginacaoUtil paginacaoUtil) {
		this.manager = manager;
		this.paginacaoUtil = paginacaoUtil;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable){
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		
		//filtros e consulta
		adicionarfiltro(filtro, criteria);
		List list = criteria.list();
		Long total = total(filtro);
		return new PageImpl<>(list, pageable, total);
	}
	
	private Long total(EstiloFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		adicionarfiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());		
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarfiltro(EstiloFilter filtro, Criteria criteria) {
		if(filtro != null && !StringUtils.isEmpty(filtro.getNome())){
			criteria.add(Restrictions.eq("nome", filtro.getNome()));
		}
	}

}
