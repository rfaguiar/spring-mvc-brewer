package com.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import com.brewer.session.TabelaItensVenda;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private TabelaItensVenda tabelaItensVenda;

	@GetMapping("/nova")
	public String nova() {
		return "venda/CadastroVenda";
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja){
		Cerveja cerveja = cervejas.findOne(codigoCerveja);
		
		tabelaItensVenda.adicionarIten(cerveja, 1);
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItensVenda.getItens());
		
		return mv;
	}

}
