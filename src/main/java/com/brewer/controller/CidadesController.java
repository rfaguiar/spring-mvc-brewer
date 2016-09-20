package com.brewer.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.model.Cidade;
import com.brewer.repository.Cidades;

@Controller
@RequestMapping("/cidades")
public class CidadesController {

	@Autowired
	private Cidades cidades;
	
	
	@RequestMapping("/novo")
	public String novo(Cidade cidade){
		return "/cidade/CadastroCidade";
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Cidade cidade, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cidade);
		}
		attributes.addFlashAttribute("mensagem", "Cidade salva com sucesso");
		return "redirect:/cidades/novo";
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(@RequestParam(name = "estado", defaultValue = "-1") Long CodigoEstado){
		
		return cidades.findByEstadoCodigo(CodigoEstado);
	}
}
