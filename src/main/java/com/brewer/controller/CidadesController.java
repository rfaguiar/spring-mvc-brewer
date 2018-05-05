package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Cidade;
import com.brewer.repository.Cidades;
import com.brewer.repository.Estados;
import com.brewer.repository.filter.CidadeFilter;
import com.brewer.service.CadastroCidadeService;
import com.brewer.service.exception.NomeCidadeJaCadastradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cidades")
public class CidadesController {

	private Cidades cidadesRepo;
	private Estados estadosRepo;
	private CadastroCidadeService cadastroCidadeService;

    @Autowired
	public CidadesController(Cidades cidadesRepo, Estados estadosRepo, CadastroCidadeService cadastroCidadeService) {
		this.cidadesRepo = cidadesRepo;
		this.estadosRepo = estadosRepo;
		this.cadastroCidadeService = cadastroCidadeService;
	}

	@RequestMapping("/novo")
	public ModelAndView nova(Cidade cidade) {
		ModelAndView mv = new ModelAndView(Constantes.CADASTRO_CIDADE_VIEW);
		mv.addObject(Constantes.ESTADOS, estadosRepo.findAll());
		return mv;
	}
	
	@Cacheable(value = "cidades", key = "#codigoEstado")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(@RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado){
		return cidadesRepo.findByEstadoCodigo(codigoEstado);
	}
	
	
	@PostMapping("/novo")
	@CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()")
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return nova(cidade);
		}
		
		try {
			cadastroCidadeService.salvar(cidade);
		} catch (NomeCidadeJaCadastradaException e) {
			result.rejectValue(Constantes.NOME, e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cidade salva com sucesso!");
		return new ModelAndView(Constantes.REDIRECT_CIDADES_NOVO);
	}
	
	@GetMapping
	public ModelAndView pesquisar(CidadeFilter cidadeFilter, @PageableDefault(size = 10) Pageable pageable,
                                  HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_CIDADE_VIEW);
		mv.addObject(Constantes.ESTADOS, estadosRepo.findAll());
		
		PageWrapper<Cidade> paginaWrapper = new PageWrapper<>(cidadesRepo.filtrar(cidadeFilter, pageable)
				, httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		return mv;
	}
}
