package com.brewer.controller;

import com.brewer.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.brewer.repository.Cervejas;
import com.brewer.repository.Clientes;
import com.brewer.repository.Vendas;

@Controller
public class DashboardController {

	private Vendas vendasRepo;
	private Cervejas cervejasRepo;
	private Clientes clientesRepo;

	@Autowired
	public DashboardController(Vendas vendasRepo, Cervejas cervejasRepo, Clientes clientesRepo) {
		this.vendasRepo = vendasRepo;
		this.cervejasRepo = cervejasRepo;
		this.clientesRepo = clientesRepo;
	}

	@GetMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView(Constantes.DASHBOARD_VIEW);
		
		mv.addObject(Constantes.VENDAS_NO_ANO, vendasRepo.valorTotalNoAno());
		mv.addObject(Constantes.VENDAS_NO_MES, vendasRepo.valorTotalNoMes());
		mv.addObject(Constantes.TICKET_MEDIO, vendasRepo.valorTicketMedioNoAno());
		
		mv.addObject(Constantes.VALOR_ITENS_ESTOQUE, cervejasRepo.valorItensEstoque());
		mv.addObject(Constantes.TOTAL_CLIENTES, clientesRepo.count());
		
		return mv;
}
}
