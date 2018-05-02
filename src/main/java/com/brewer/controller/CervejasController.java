package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.controller.page.PageWrapper;
import com.brewer.dto.CervejaDTO;
import com.brewer.model.Cerveja;
import com.brewer.model.Origem;
import com.brewer.model.Sabor;
import com.brewer.repository.Cervejas;
import com.brewer.repository.Estilos;
import com.brewer.repository.filter.CervejaFilter;
import com.brewer.service.CadastroCervejaService;
import com.brewer.service.exception.ImpossivelExcluirEntidadeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {

	private CadastroCervejaService cadastroCervejaService;
	private Estilos estilosRepo;
	private Cervejas cervejasRepo;

	@Autowired
	public CervejasController(CadastroCervejaService cadastroCervejaService, Estilos estilos, Cervejas cervejas) {
		this.cadastroCervejaService = cadastroCervejaService;
		this.estilosRepo = estilos;
		this.cervejasRepo = cervejas;
	}

	@RequestMapping("/novo")
	public ModelAndView novo(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView(Constantes.CADASTRO_CERVEJA_VIEW);
		mv.addObject(Constantes.SABORES, Sabor.values());
		mv.addObject(Constantes.ESTILOS, estilosRepo.findAll());
		mv.addObject(Constantes.ORIGENS, Origem.values());
		return mv;
	}
	
	@RequestMapping(value = {"/novo", "{\\d+}"}, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			return novo(cerveja);
		}
		
		cadastroCervejaService.salvar(cerveja);
		
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cerveja salva com sucesso");
		return new ModelAndView(Constantes.REDIRECT_CERVEJAS_NOVO_VIEW);
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, @PageableDefault(size = 2) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_CERVEJA_VIEW);
		mv.addObject(Constantes.SABORES, Sabor.values());
		mv.addObject(Constantes.ESTILOS, estilosRepo.findAll());
		mv.addObject(Constantes.ORIGENS, Origem.values());
		
		PageWrapper<Cerveja> paginaWrapper = new PageWrapper<>(cervejasRepo.filtrar(cervejaFilter, pageable), httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome){
		return cervejasRepo.porSkuOuNome(skuOuNome);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity excluir(@PathVariable("codigo") Cerveja cerveja) {
		try {
			cadastroCervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Cerveja cerveja){
		ModelAndView mv = novo(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
	
}
