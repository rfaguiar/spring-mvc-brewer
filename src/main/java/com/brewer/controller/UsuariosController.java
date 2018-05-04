package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Usuario;
import com.brewer.repository.Grupos;
import com.brewer.repository.Usuarios;
import com.brewer.repository.filter.UsuarioFilter;
import com.brewer.service.CadastroUsuarioService;
import com.brewer.service.StatusUsuario;
import com.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	private CadastroUsuarioService cadastroUsuarioService;
	private Grupos gruposRepo;
	private Usuarios usuariosRepo;

    @Autowired
    public UsuariosController(CadastroUsuarioService cadastroUsuarioService, Grupos gruposRepo, Usuarios usuariosRepo) {
        this.cadastroUsuarioService = cadastroUsuarioService;
        this.gruposRepo = gruposRepo;
        this.usuariosRepo = usuariosRepo;
    }

    @RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView(Constantes.CADASTRO_USUARIO_VIEW);
		mv.addObject(Constantes.GRUPOS, gruposRepo.findAll());
		return mv;
	}
	
	@PostMapping({"/novo", "{\\+d}"})
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}
		
		try {
			cadastroUsuarioService.salvar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			result.rejectValue(Constantes.EMAIL, e.getMessage(), e.getMessage());
			return novo(usuario);
		} catch (SenhaObrigatoriaUsuarioException e) {
			result.rejectValue(Constantes.SENHA, e.getMessage(), e.getMessage());
			return novo(usuario);
		}
		
		attributes.addFlashAttribute(Constantes.MENSAGEM_VIEW, "Usuário salvo com sucesso");
		return new ModelAndView(Constantes.USUARIO_NOVO_VIEW);
	}
	
	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView(Constantes.PESQUISA_USUARIOS_VIEW);
		mv.addObject(Constantes.GRUPOS, gruposRepo.findAll());
		
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuariosRepo.filtrar(usuarioFilter, pageable), httpServletRequest);
		mv.addObject(Constantes.PAGINADOR_VIEW, paginaWrapper);
		
		return mv;
	}
	
	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos, @RequestParam("status") StatusUsuario statusUsuario){
		cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo){
		Usuario usuario = usuariosRepo.buscarComGrupos(codigo);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}
	
}
