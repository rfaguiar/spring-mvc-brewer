package com.brewer.session;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.brewer.model.Cerveja;
import com.brewer.session.TabelaItensVenda;

public class TabelaItensVendaTest {

	private TabelaItensVenda tabelaItensVenda;
	
	@Before
	public void setUp(){
		this.tabelaItensVenda = new TabelaItensVenda("1");
	}
	
	@Test
	public void deveCalcularValorTotalSemItens() throws Exception {
		assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveCalcularValorTotalComUmItem() throws Exception {
		Cerveja cerveja = new Cerveja();
		BigDecimal valor = new BigDecimal("8.90");
		cerveja.setValor(valor);
		
		tabelaItensVenda.adicionarIten(cerveja, 1);
		
		assertEquals(valor, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveCalcularValorTotalComVariosItens() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1l);
		BigDecimal v1 = new BigDecimal("8.90");
		c1.setValor(v1);
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		BigDecimal v2 = new BigDecimal("4.99");
		c2.setValor(v2);
		
		tabelaItensVenda.adicionarIten(c1, 1);
		tabelaItensVenda.adicionarIten(c2, 2);
		
		assertEquals(new BigDecimal("18.88"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveManterTamanhoDaListaParaMesmasCervejas() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("4.50"));
		
		tabelaItensVenda.adicionarIten(c1, 1);
		tabelaItensVenda.adicionarIten(c1, 1);
		
		assertEquals(1, tabelaItensVenda.total());
		assertEquals(new BigDecimal("9.00"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveAlterarQuantidadeDoItem() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1L);
		c1.setValor(new BigDecimal("4.50"));
		
		tabelaItensVenda.adicionarIten(c1, 1);
		
		tabelaItensVenda.alterarQuantidadeItens(c1, 3);
		
		assertEquals(new BigDecimal("13.50"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveExluirItem() throws Exception {
		Cerveja c1 = new Cerveja();
		c1.setCodigo(1l);
		c1.setValor(new BigDecimal("8.90"));
		
		Cerveja c2 = new Cerveja();
		c2.setCodigo(2l);
		c2.setValor(new BigDecimal("4.99"));
		
		Cerveja c3 = new Cerveja();
		c3.setCodigo(3l);
		c3.setValor(new BigDecimal("2.00"));
		
		tabelaItensVenda.adicionarIten(c1, 1);
		tabelaItensVenda.adicionarIten(c2, 2);
		tabelaItensVenda.adicionarIten(c3, 1);
		
		tabelaItensVenda.excluir(c2);
		
		assertEquals(2, tabelaItensVenda.total());
		assertEquals(new BigDecimal("10.90"), tabelaItensVenda.getValorTotal());
	}
}
