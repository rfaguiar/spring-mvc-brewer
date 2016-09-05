package com.brewer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.model.Estilo;

@Controller
public class EstilosController {

	@RequestMapping("/estilos/novo")
	public String novo(Estilo estilo){
		return "/estilo/CadastroEstilo";
	}
	
	@RequestMapping(value = "/estilos/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Estilo estilo, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(estilo);
		}
		attributes.addFlashAttribute("mensagem", "Estilo salva com sucesso");
		return "redirect:/estilos/novo";
	}
}
