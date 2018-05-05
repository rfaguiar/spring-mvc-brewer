package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.EstiloBuilder;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Estilo;
import com.brewer.repository.Estilos;
import com.brewer.repository.filter.EstiloFilter;
import com.brewer.service.CadastroEstiloService;
import com.brewer.service.exception.NomeEstiloJaCadastradoException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({UriComponentsBuilder.class})
public class EstilosControllerTest {

    private EstilosController controller;
    @Mock
    private CadastroEstiloService mockEstiloService;
    @Mock
    private Estilos mockEstilosRepo;
    @Mock
    private RedirectAttributes mockRedirectAttributes;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private FieldError mockFieldError;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Pageable mockPageable;
    @Mock
    private EstiloFilter mockEstiloFilter;
    @Mock
    private UriComponentsBuilder uriBuilder;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        this.controller = new EstilosController(mockEstiloService, mockEstilosRepo);
    }

    @Test
    public void testeMetodoNovoDeveRetornarParaCadastroEstiloView() {
        Estilo estilo = EstiloBuilder.criarEstilo();

        ModelAndView result = controller.novo(estilo);

        assertEquals(Constantes.CADASTRO_ESTILO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodoCadastrarDeveSalvarEstiloERedirecionarParaEstilosNovoViewComMsgAdequada() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.when(mockEstiloService.salvar(estilo)).thenReturn(estilo);

        ModelAndView result = controller.cadastrar(estilo, mockBindingResult, mockRedirectAttributes);

        Mockito.verify(mockRedirectAttributes).addFlashAttribute(Constantes.MENSAGEM_VIEW, "Estilo salvo com sucesso");
        assertEquals(Constantes.REDIRECT_ESTILOS_NOVO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodoCadastrarQuandoEstiloComMesmoNomeNaoDeveSalvarEstiloERedirecionarParaEstilosNovoViewComMsgAdequada() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.when(mockEstiloService.salvar(estilo)).thenThrow(new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado"));

        ModelAndView result = controller.cadastrar(estilo, mockBindingResult, mockRedirectAttributes);

        Mockito.verify(mockBindingResult).rejectValue(Constantes.NOME, "Nome do estilo já cadastrado", "Nome do estilo já cadastrado");
        Mockito.verifyZeroInteractions(mockRedirectAttributes);
        assertEquals(Constantes.CADASTRO_ESTILO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodoCadastrarQuandoEstiloNaoValidoNaoDeveSalvarEstiloERedirecionarParaEstilosNovoView() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = controller.cadastrar(estilo, mockBindingResult, mockRedirectAttributes);

        Mockito.verifyZeroInteractions(mockRedirectAttributes);
        Mockito.verifyZeroInteractions(mockEstiloService);
        assertEquals(Constantes.CADASTRO_ESTILO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodoSalvarQuandoEstiloComtemErrosNaoDeveSalvarEstiloERetornarStatus400() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(mockBindingResult.getFieldError(Constantes.NOME)).thenReturn(mockFieldError);
        Mockito.when(mockFieldError.getDefaultMessage()).thenReturn("Erro nome");

        ResponseEntity result = controller.salvar(estilo, mockBindingResult);

        Mockito.verifyZeroInteractions(mockEstiloService);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Erro nome", result.getBody());
    }

    @Test
    public void testeMetodoSalvarDeveSalvarEstiloERetornarStatus200() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.when(mockEstiloService.salvar(estilo)).thenReturn(estilo);

        ResponseEntity result = controller.salvar(estilo, mockBindingResult);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testeMetodoPesquisarDeveRetornarListaPaginadaDeEstilos() {
        List<Estilo> listaEstilos = EstiloBuilder.criarListaEstilos();
        Page<Estilo> clientePage = new PageImpl<>(listaEstilos, mockPageable, 1);
        Mockito.when(mockEstilosRepo.filtrar(mockEstiloFilter, mockPageable)).thenReturn(clientePage);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(uriBuilder);

        ModelAndView result = controller.pesquisar(mockEstiloFilter, mockPageable, mockHttpRequest);

        PageWrapper<Estilo> paginaWrapperResult = (PageWrapper<Estilo>) result.getModel().get(Constantes.PAGINADOR_VIEW);

        assertEquals(Constantes.PESQUISA_ESTILOS_VIEW, result.getViewName());
        assertEquals(clientePage.getContent(), paginaWrapperResult.getConteudo());
    }
}