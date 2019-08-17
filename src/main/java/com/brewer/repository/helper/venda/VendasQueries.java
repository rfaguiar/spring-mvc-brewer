package com.brewer.repository.helper.venda;

import com.brewer.dto.VendaMes;
import com.brewer.dto.VendaOrigem;
import com.brewer.model.Venda;
import com.brewer.repository.filter.VendaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VendasQueries {

	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
	public BigDecimal valorTotalNoAno();
	
	public BigDecimal valorTotalNoMes();
	
	public BigDecimal valorTicketMedioNoAno();
	
	public List<VendaMes> totalPorMes();
	
	public List<VendaOrigem> totalPorOrigem();
}
