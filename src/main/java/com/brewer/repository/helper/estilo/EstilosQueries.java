package com.brewer.repository.helper.estilo;

import com.brewer.model.Estilo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.brewer.repository.Estilos;
import com.brewer.repository.filter.EstiloFilter;

public interface EstilosQueries {

	public Page<Estilo> filtrar(EstiloFilter filtro, Pageable pageable);
}
