package com.brewer.repository.helper.cerveja;

import com.brewer.dto.CervejaDTO;
import com.brewer.dto.ValorItensEstoque;
import com.brewer.model.Cerveja;
import com.brewer.repository.filter.CervejaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CervejasQueries {

	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
	
	public List<CervejaDTO> porSkuOuNome(String skuOuNome); 
	
	public ValorItensEstoque valorItensEstoque();
}
