package com.brewer.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.brewer.model.Cerveja;
import com.brewer.model.ItemVenda;

@SessionScope
@Component
public class TabelasItensSession {

	private Set<TabelaItensVenda> tabelas;

	public TabelasItensSession() {
        tabelas = new HashSet<>();
    }

	public TabelasItensSession(Set<TabelaItensVenda> tabelas) {
		this.tabelas = tabelas;
	}

	public void adicionarItem(String uuid, Cerveja cerveja, int quantidade) {
		TabelaItensVenda tabela = buscarTabelaPorId(uuid);
		tabela.adicionarIten(cerveja, quantidade);
		tabelas.add(tabela);
	}


	public void alterarQuantidadeItens(String uuid, Cerveja cerveja, Integer novaQuantidade) {
		TabelaItensVenda tabela = buscarTabelaPorId(uuid);
		tabela.alterarQuantidadeItens(cerveja, novaQuantidade);
	}

	public void excluirItem(String uuid, Cerveja cerveja) {
		TabelaItensVenda tabela = buscarTabelaPorId(uuid);
		tabela.excluir(cerveja);
	}

	public List<ItemVenda> getItens(String uuid) {		
		return buscarTabelaPorId(uuid).getItens();
	}
	
	public Object getValorTotal(String uuid) {
		return buscarTabelaPorId(uuid).getValorTotal();
	}
	
	private TabelaItensVenda buscarTabelaPorId(String uuid) {
		return tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid))
				.findAny()
				.orElse(new TabelaItensVenda(uuid));
	}
}
