package com.brewer.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brewer.repository.Estilos;
import com.brewer.repository.filter.EstiloFilter;

public interface EstilosQueries {

	public Page<Estilos> filtrar(EstiloFilter filtro, Pageable pageable);
}
