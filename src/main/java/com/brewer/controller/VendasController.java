package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.controller.page.PageWrapper;
import com.brewer.controller.validator.VendaValidator;
import com.brewer.dto.VendaMes;
import com.brewer.dto.VendaOrigem;
import com.brewer.mail.Mailer;
import com.brewer.model.*;
import com.brewer.repository.Cervejas;
import com.brewer.repository.Vendas;
import com.brewer.repository.filter.VendaFilter;
import com.brewer.security.UsuarioSistema;
import com.brewer.service.CadastroVendaService;
import com.brewer.session.TabelasItensSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendasController {

    private static final Logger logger = LoggerFactory.getLogger(VendasController.class);

	private Cervejas cervejasRepo;
	private TabelasItensSession tabelaItens;
	private CadastroVendaService cadastroVendaService;
	private VendaValidator vendaValidator;
	private Vendas vendasRepo;
	private Mailer mailer;

	@Autowired
	public VendasController(Cervejas cervejasRepo, TabelasItensSession tabelaItens, CadastroVendaService cadastroVendaService, VendaValidator vendaValidator, Vendas vendasRepo, Mailer mailer) {
		this.cervejasRepo = cervejasRepo;
		this.tabelaItens = tabelaItens;
		this.cadastroVendaService = cadastroVendaService;
		this.vendaValidator = vendaValidator;
		this.vendasRepo = vendasRepo;
		this.mailer = mailer;
	}

	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView(Constantes.CADASTRO_VENDA_VIEW);
		
		setUuid(venda);
		
		mv.addObject(Constantes.ITENS, venda.getItens());
		mv.addObject(Constantes.VALOR_FRETE, venda.getValorFrete());
		mv.addObject(Constantes.VALOR_DESCONTO, venda.getValorDesconto());
		mv.addObject(Constantes.VALOR_TOTAL_ITENS, tabelaItens.getValorTotal(venda.getUuid()));
		
		return mv;
	}
	
	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Venda salva com sucesso");
		return new ModelAndView(Constantes.REDIRECT_VENDAS_NOVA_VIEW);
	}

	@PostMapping(value = "/nova", params = "emitir")
	public ModelAndView emitir(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Venda emitida com sucesso");
		return new ModelAndView(Constantes.REDIRECT_VENDAS_NOVA_VIEW);
	}
	
	@PostMapping(value = "/nova", params = "enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		validarVenda(venda, result);
		if (result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		venda = cadastroVendaService.salvar(venda);
		
		mailer.enviar(venda);
        logger.debug("####### Logo depois da chama do metodo enviar.");
		
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, String.format("Venda n° %d salva e e-mail enviado", venda.getCodigo()));
		return new ModelAndView(Constantes.REDIRECT_VENDAS_NOVA_VIEW);
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja, String uuid) {
		Cerveja cerveja = cervejasRepo.findOne(codigoCerveja);
		tabelaItens.adicionarItem(uuid, cerveja, 1);
		return mvTabelaItensVenda(uuid);
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja
			, Integer quantidade, String uuid) {
		tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
		return mvTabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja
			, @PathVariable String uuid) {
		tabelaItens.excluirItem(uuid, cerveja);
		return mvTabelaItensVenda(uuid);
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter,
			@PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_VENDAS_VIEW);
		mv.addObject(Constantes.TODOS_STATUS, StatusVenda.values());
		mv.addObject(Constantes.TIPOS_PESSOA, TipoPessoa.values());
		
		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendasRepo.filtrar(vendaFilter, pageable)
				, httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo){
		Venda venda = vendasRepo.buscarComItens(codigo);
		
		setUuid(venda);
		for (ItemVenda item : venda.getItens()) {
			tabelaItens.adicionarItem(venda.getUuid(), item.getCerveja(), item.getQuantidade());
		}
		
		ModelAndView mv = nova(venda);
		mv.addObject(venda);
		return mv;
	}
	
	@PostMapping(value = "/nova", params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			cadastroVendaService.cancelar(venda);
		} catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}
		
		attributes.addFlashAttribute("mensgaem", "Venda cancelada com sucesso");
		return new ModelAndView(Constantes.REDIRECT_VENDAS_VIEW + venda.getCodigo());
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalTotalVendaPorMes(){
		return vendasRepo.totalPorMes();
	}
	
	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendasRepo.totalPorOrigem();
	}
	
	private void setUuid(Venda venda) {
		if (StringUtils.isEmpty(venda.getUuid())) {
			venda.setUuid(UUID.randomUUID().toString());
		}		
	}

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView(Constantes.TABELA_ITENS_VENDA_VIEW);
		mv.addObject(Constantes.ITENS, tabelaItens.getItens(uuid));
		mv.addObject(Constantes.VALOR_TOTAL, tabelaItens.getValorTotal(uuid));
		return mv;
	}
	
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result);
	}

}
