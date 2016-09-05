package com.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.brewer.repository.Cervejas;
import com.brewer.model.Cerveja;

@Controller
public class CervejasController {

	@Autowired
	private Cervejas cervejas;
	
	@RequestMapping("/cervejas/novo")
	public String novo(Cerveja cerveja) {
		//Optional<Cerveja> findBySku = cervejas.findBySkuIgnoreCase("AA1111");//Apagar
		//System.out.println(findBySku.isPresent());
		
		return "cerveja/CadastroCerveja";
	}
	
	@RequestMapping(value = "/cervejas/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cerveja);
		}
		attributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso");
		return "redirect:/cervejas/novo";
	}
}
