package com.brewer.repository.helper.cerveja;

import java.util.List;

import com.brewer.model.Cerveja;
import com.brewer.repository.filter.CervejaFilter;

public interface CervejasQueries {

	public List<Cerveja> filtrar(CervejaFilter filtro);
}
