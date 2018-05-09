package com.brewer.session;

import com.brewer.model.Cerveja;
import com.brewer.model.ItemVenda;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
		Optional<ItemVenda> optVenda = buscarItemPorCerveja(cerveja);
		if (optVenda.isPresent()) {
			optVenda.get()
					.setQuantidade(novaQuantidade);
		}
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
        return itens.stream()
				.filter(i -> i.getCerveja().equals(cerveja))
				.findAny();
	}	
	
	public String getUuid() {
		return uuid;
	}

}
