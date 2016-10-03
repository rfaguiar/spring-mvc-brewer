package com.brewer.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.brewer.model.Cerveja;
import com.brewer.model.ItemVenda;

@SessionScope
@Component
class TabelaItensVenda {

	private String uuid; 

	private List<ItemVenda> itens = new ArrayList<>();
	
	public TabelaItensVenda(String uuid) {
		this.uuid = uuid;
	}
	
	public BigDecimal getValorTotal(){
		
		return itens.stream()
				.map(ItemVenda :: getValorTotal)
				.reduce(BigDecimal :: add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void adicionarIten(Cerveja cerveja, Integer quantidade){
		Optional<ItemVenda> itemVendaOptional = buscarItemPorCerveja(cerveja);
		
		ItemVenda itemVenda = null;
		if(itemVendaOptional.isPresent()){
			itemVenda = itemVendaOptional.get();
			itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);
		}else{
			itemVenda = new ItemVenda();
			itemVenda.setCerveja(cerveja);
			itemVenda.setQuantidade(quantidade);
			itemVenda.setValorUnitario(cerveja.getValor());
			itens.add(0, itemVenda);			
		}	
		
	}
	
	public void alterarQuantidadeItens(Cerveja cerveja, Integer novaQuantidade){
		ItemVenda itemVenda = buscarItemPorCerveja(cerveja).get();
		itemVenda.setQuantidade(novaQuantidade);
	}
	
	public void excluir(Cerveja cerveja){
		int indice = IntStream.range(0, itens.size())
				.filter(i -> itens.get(i).getCerveja().equals(cerveja))
				.findAny().getAsInt();
		itens.remove(indice);
	}
	
	public int total(){
		return itens.size();
	}

	public List<ItemVenda> getItens() {
		
		return itens;
	}
	
	private Optional<ItemVenda> buscarItemPorCerveja(Cerveja cerveja) {
		Optional<ItemVenda> itemVendaOptional = itens.stream()
				.filter(i -> i.getCerveja().equals(cerveja))
				.findAny();
		return itemVendaOptional;
	}	
	
	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaItensVenda other = (TabelaItensVenda) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
}
