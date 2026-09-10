package com.brewer.repository.paginacao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class PaginacaoUtil {

	
	public void preparar(Criteria criteria, Pageable pageable){
		//paginação
		int totalRegistrosPorPagina = pageable.getPageSize();
		int paginaAtual = pageable.getPageNumber();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		criteria.setFirstResult(primeiroRegistro);
		criteria.setMaxResults(totalRegistrosPorPagina);		

		//ordenação
		Sort sort = pageable.getSort();
		if(sort != null){
			Iterator<Sort.Order> orders = sort.iterator();
			if (orders.hasNext()) {
				Sort.Order order = orders.next();
				String property = order.getProperty();
				criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
			}
		}
	}
}
