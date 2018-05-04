package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.GrupoBuilder;
import com.brewer.builder.UsuarioBuilder;
import com.brewer.model.Grupo;
import com.brewer.model.Usuario;
import com.brewer.model.Venda;
import com.brewer.repository.Grupos;
import com.brewer.repository.Usuarios;
import com.brewer.repository.filter.UsuarioFilter;
import com.brewer.service.CadastroUsuarioService;
import com.brewer.service.StatusUsuario;
import com.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({UriComponentsBuilder.class})
public class UsuariosControllerTest {

    private UsuariosController controller;

    @Mock
    private Usuarios mockUsuariosRepo;
    @Mock
    private Grupos mockGruposRepo;
    @Mock
    private CadastroUsuarioService mockUsuarioService;
    @Mock
    private RedirectAttributes mockRedirectAttributtes;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Pageable mockPageable;
    @Mock
    private UsuarioFilter mockUsuarioFilter;
    @Mock
    private UriComponentsBuilder uriBuilder;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        this.controller = new UsuariosController(mockUsuarioService, mockGruposRepo, mockUsuariosRepo);
    }

    @Test
    public void testeMetodoNovoDeveRetornarListaComTodosGrupos() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        List<Grupo> grupos = GrupoBuilder.criarListaGrupos();
        Mockito.when(mockGruposRepo.findAll()).thenReturn(grupos);

        ModelAndView result = controller.novo(usuario);

        List<Grupo> gruposResult = (List<Grupo>) result.getModel().get(Constantes.GRUPOS);

        assertEquals(Constantes.CADASTRO_USUARIO_VIEW, result.getViewName());
        assertEquals(grupos, gruposResult);

    }

    @Test
    public void testeMetodoSalvarDeveRetornarUsuarioNovoViewComMsgAdequada() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doNothing().when(mockUsuarioService).salvar(usuario);

        ModelAndView result = controller.salvar(usuario, mockBindingResult, mockRedirectAttributtes);

        Mockito.verify(mockRedirectAttributtes).addFlashAttribute(Constantes.MENSAGEM_VIEW, "Usuário salvo com sucesso");
        assertEquals(Constantes.USUARIO_NOVO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodoSalvarQuandoUsuarioNaoValidoNaoDeveSalvarEDeveRetornarCadastroUsuarioViewComMsgAdequada() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        List<Grupo> listaGrupos = GrupoBuilder.criarListaGrupos();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);
        Mockito.doNothing().when(mockUsuarioService).salvar(usuario);
        Mockito.when(mockGruposRepo.findAll()).thenReturn(listaGrupos);

        ModelAndView result = controller.salvar(usuario, mockBindingResult, mockRedirectAttributtes);

        List<Grupo> listaGruposResult = (List<Grupo>) result.getModel().get(Constantes.GRUPOS);

        Mockito.verifyZeroInteractions(mockRedirectAttributtes);
        Mockito.verifyZeroInteractions(mockUsuarioService);
        assertEquals(Constantes.CADASTRO_USUARIO_VIEW, result.getViewName());
        assertEquals(listaGrupos, listaGruposResult);
    }

    @Test
    public void testeMetodoSalvarQuandoUsuarioEmailExisteDeveRetornarCadastroUsuarioViewComMsgAdequada() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        List<Grupo> listaGrupos = GrupoBuilder.criarListaGrupos();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doThrow(new EmailUsuarioJaCadastradoException("E-mail já cadastrado")).when(mockUsuarioService).salvar(usuario);
        Mockito.when(mockGruposRepo.findAll()).thenReturn(listaGrupos);

        ModelAndView result = controller.salvar(usuario, mockBindingResult, mockRedirectAttributtes);

        List<Grupo> listaGruposResult = (List<Grupo>) result.getModel().get(Constantes.GRUPOS);

        Mockito.verify(mockBindingResult).rejectValue(Constantes.EMAIL, "E-mail já cadastrado", "E-mail já cadastrado");
        Mockito.verifyZeroInteractions(mockRedirectAttributtes);
        assertEquals(Constantes.CADASTRO_USUARIO_VIEW, result.getViewName());
        assertEquals(listaGrupos, listaGruposResult);
    }


    @Test
    public void testeMetodoSalvarQuandoUsuarioNaoInformouSenhaDeveRetornarCadastroUsuarioViewComMsgAdequada() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        List<Grupo> listaGrupos = GrupoBuilder.criarListaGrupos();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doThrow(new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário"))
                .when(mockUsuarioService).salvar(usuario);
        Mockito.when(mockGruposRepo.findAll()).thenReturn(listaGrupos);

        ModelAndView result = controller.salvar(usuario, mockBindingResult, mockRedirectAttributtes);

        List<Grupo> listaGruposResult = (List<Grupo>) result.getModel().get(Constantes.GRUPOS);

        Mockito.verify(mockBindingResult).rejectValue(Constantes.SENHA, "Senha é obrigatória para novo usuário",
                "Senha é obrigatória para novo usuário");
        Mockito.verifyZeroInteractions(mockRedirectAttributtes);
        assertEquals(Constantes.CADASTRO_USUARIO_VIEW, result.getViewName());
        assertEquals(listaGrupos, listaGruposResult);
    }

    @Test
    public void tesMetodoPesquisarDeveRetornarListaPaginadaDeUsuarioParaPesquisaUsuariosView() {
        List<Grupo> listaGrupos = GrupoBuilder.criarListaGrupos();
        List<Usuario> listaUsuarios = UsuarioBuilder.criarListaUsuarios();
        PageImpl<Usuario> usuariosPage = new PageImpl<>(listaUsuarios, mockPageable, 1);
        Mockito.when(mockUsuariosRepo.filtrar(mockUsuarioFilter, mockPageable)).thenReturn(usuariosPage);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(uriBuilder);
        Mockito.when(mockGruposRepo.findAll()).thenReturn(listaGrupos);

        ModelAndView result = controller.pesquisar(mockUsuarioFilter, mockPageable, mockHttpRequest);

        List<Grupo> listaGruposResult = (List<Grupo>) result.getModel().get(Constantes.GRUPOS);

        assertEquals(Constantes.PESQUISA_USUARIOS_VIEW, result.getViewName());
        assertEquals(listaGrupos, listaGruposResult);

    }

    @Test
    public void testeMetodoAtualizarStatus() {
        Long[] codigos = new Long[] {new Long(1)};
        Mockito.doNothing().when(mockUsuarioService).alterarStatus(codigos, StatusUsuario.ATIVAR);

        controller.atualizarStatus(codigos, StatusUsuario.ATIVAR);

        Mockito.verify(mockUsuarioService).alterarStatus(codigos, StatusUsuario.ATIVAR);
    }

    @Test
    public void testeMetodoEditarDeveEditarERetornarUsuarioEditado() {
        Long codigo = new Long(1);
        Usuario usuario = UsuarioBuilder.criarUsuario();
        Mockito.when(mockUsuariosRepo.buscarComGrupos(codigo)).thenReturn(usuario);

        ModelAndView result = controller.editar(codigo);

        Mockito.verify(mockUsuariosRepo).buscarComGrupos(codigo);
        assertNotNull(result);

    }
}