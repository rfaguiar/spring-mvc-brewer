package com.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.brewer.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Estilo;
import com.brewer.repository.Estilos;
import com.brewer.repository.filter.EstiloFilter;
import com.brewer.service.CadastroEstiloService;
import com.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

	private CadastroEstiloService cadastroEstiloService;
	private Estilos estilosRepo;

    @Autowired
    public EstilosController(CadastroEstiloService cadastroEstiloService, Estilos estilosRepo) {
        this.cadastroEstiloService = cadastroEstiloService;
        this.estilosRepo = estilosRepo;
    }

    @RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		return new ModelAndView(Constantes.CADASTRO_ESTILO_VIEW);
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(estilo);
		}
		
		try {
			cadastroEstiloService.salvar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			result.rejectValue(Constantes.NOME, e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Estilo salvo com sucesso");
		return new ModelAndView(Constantes.REDIRECT_ESTILOS_NOVO_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity salvar(@RequestBody @Valid Estilo estilo, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError(Constantes.NOME).getDefaultMessage());
		}
		
		estilo = cadastroEstiloService.salvar(estilo);
		return ResponseEntity.ok(estilo);
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter estiloFilter, @PageableDefault(size = 2) Pageable pageable,
								  HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_ESTILOS_VIEW);
		
		PageWrapper<Estilo> paginaWrapper = new PageWrapper<>(estilosRepo.filtrar(estiloFilter, pageable), httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		return mv;
	}
	
}
