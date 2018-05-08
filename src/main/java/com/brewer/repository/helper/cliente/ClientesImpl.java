package com.brewer.repository.helper.cliente;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.springframework.util.StringUtils;

import com.brewer.model.Cliente;
import com.brewer.repository.filter.ClienteFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;

public class ClientesImpl implements ClientesQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	public ClientesImpl() {}

	public ClientesImpl(EntityManager manager, PaginacaoUtil paginacaoUtil) {
		this.manager = manager;
		this.paginacaoUtil = paginacaoUtil;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable){
		
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		
		//filtros e consulta
		adicionarfiltro(filtro, criteria);
		
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(ClienteFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		adicionarfiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());		
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarfiltro(ClienteFilter filtro, Criteria criteria) {
		if(filtro != null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.eq("nome", filtro.getNome()));
			}
			if(!StringUtils.isEmpty(filtro.getCpfOuCnpj())){
				criteria.add(Restrictions.eq("cpfOuCnpj", filtro.getCpfOuCnpj()));
			}
		}
	}

}
