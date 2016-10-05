package com.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brewer.model.Venda;
import com.brewer.repository.helper.venda.VendasQueries;

public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

}
