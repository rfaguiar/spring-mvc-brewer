package com.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Cliente;
import com.brewer.model.TipoPessoa;
import com.brewer.repository.Clientes;
import com.brewer.repository.Estados;
import com.brewer.repository.filter.ClienteFilter;
import com.brewer.service.CadastroClienteService;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private Estados estados;
	
	@Autowired
	CadastroClienteService cadastroClienteService;
	
	@Autowired
	Clientes clientes;

	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente){		
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estados.findAll());
		return mv;				
	}
	
	@PostMapping(value = "/novo")
	public ModelAndView cadastrar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cliente);
		}
		try{
			cadastroClienteService.salvar(cliente);
		}catch(CpfCnpjClienteJaCadastradoException e){
			result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso");
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView("cliente/PesquisaClientes");
		
		PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(clientes.filtrar(clienteFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
}
