package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Cliente;
import com.brewer.model.TipoPessoa;
import com.brewer.repository.Clientes;
import com.brewer.repository.Estados;
import com.brewer.repository.filter.ClienteFilter;
import com.brewer.service.CadastroClienteService;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClientesController {

	private Estados estadosRepo;
    private CadastroClienteService cadastroClienteService;
    private Clientes clientesRepo;

    @Autowired
	public ClientesController(Estados estadosRepo, CadastroClienteService cadastroClienteService, Clientes clientesRepo) {
		this.estadosRepo = estadosRepo;
		this.cadastroClienteService = cadastroClienteService;
		this.clientesRepo = clientesRepo;
	}

	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente){		
		ModelAndView mv = new ModelAndView(Constantes.CADASTRO_CLIENTE_VIEW);
		mv.addObject(Constantes.TIPOS_PESSOA, TipoPessoa.values());
		mv.addObject(Constantes.ESTADOS, estadosRepo.findAll());
		return mv;				
	}
	
	@PostMapping(value = "/novo")
	public ModelAndView cadastrar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cliente);
		}
		try{
			cadastroClienteService.salvar(cliente);
		}catch(CpfCnpjClienteJaCadastradoException e){
			result.rejectValue(Constantes.CPF_OU_CNPJ, e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cliente salvo com sucesso");
		return new ModelAndView(Constantes.REDIRECT_CLIENTES_NOVO);
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter, BindingResult result, @PageableDefault(size = 2) Pageable pageable,
                                  HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_CLIENTE_VIEW);
		
		PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(clientesRepo.filtrar(clienteFilter, pageable), httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Cliente> pesquisar(String nome){
		
		validarTamanhoNome(nome);
		
		return clientesRepo.findByNomeStartingWithIgnoreCase(nome);
	}

	private void validarTamanhoNome(String nome) {
		if(StringUtils.isEmpty(nome) || nome.length() < 3){
			throw new IllegalArgumentException();
		}		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException exception){
		return ResponseEntity.badRequest().build();
	}
}
