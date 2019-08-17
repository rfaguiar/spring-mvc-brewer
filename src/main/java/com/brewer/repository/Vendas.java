package com.brewer.repository;

import com.brewer.model.Venda;
import com.brewer.repository.helper.venda.VendasQueries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

}
