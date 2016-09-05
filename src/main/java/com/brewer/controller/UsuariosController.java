package com.brewer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.model.Usuario;

@Controller
public class UsuariosController {

	@RequestMapping("/usuarios/novo")
	public String novo(Usuario usuario){
		return "usuario/CadastroUsuario";
	}
	
	@RequestMapping(value = "/usuarios/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(usuario);
		}
		attributes.addFlashAttribute("message", "Usuario salvo com sucesso");
		return "redirect:/usuarios/novo";
	}
}
