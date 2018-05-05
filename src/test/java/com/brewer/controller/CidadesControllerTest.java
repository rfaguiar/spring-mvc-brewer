package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.CidadeBuilder;
import com.brewer.builder.EstadoBuilder;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Cidade;
import com.brewer.model.Estado;
import com.brewer.repository.Cidades;
import com.brewer.repository.Estados;
import com.brewer.repository.filter.CidadeFilter;
import com.brewer.service.CadastroCidadeService;
import com.brewer.service.exception.NomeCidadeJaCadastradaException;
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
public class CidadesControllerTest {

    private CidadesController controller;
    @Mock
    private CadastroCidadeService mockCidadeService;
    @Mock
    private Estados mockEstadosRepo;
    @Mock
    private Cidades mockCidadesRepo;
    @Mock
    private RedirectAttributes mockRedirectAttrbitues;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Pageable mockPageable;
    @Mock
    private CidadeFilter mockCidadeFilter;
    @Mock
    private UriComponentsBuilder uriBuilder;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        this.controller = new CidadesController(mockCidadesRepo, mockEstadosRepo, mockCidadeService);
    }

    @Test
    public void testeMetodoNovaDeveMostrarCadastroDeCidadeViewComListaDeEstados() {
        Cidade cidade = CidadeBuilder.criarCidade();
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);

        ModelAndView result = controller.nova(cidade);

        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);

        assertEquals(Constantes.CADASTRO_CIDADE_VIEW, result.getViewName());
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void testeMetodoPesquisarPorCodigoEstadoDeveRetornarListaDeCidadePeloCodigo() {
        Long codigoEstado = new Long(1);
        List<Cidade> listaCidades = CidadeBuilder.criarListaCidade();
        Mockito.when(mockCidadesRepo.findByEstadoCodigo(codigoEstado)).thenReturn(listaCidades);

        List<Cidade> result = controller.pesquisarPorCodigoEstado(codigoEstado);

        assertEquals(listaCidades, result);
    }

    @Test
    public void TesteMetodoSalvarDeveSalvarCidadeERedirecionarParaCidadeNovoViewComMsgAdequada() {
        Cidade cidade = CidadeBuilder.criarCidade();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doNothing().when(mockCidadeService).salvar(cidade);

        ModelAndView result = controller.salvar(cidade, mockBindingResult, mockRedirectAttrbitues);

        Mockito.verify(mockRedirectAttrbitues).addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cidade salva com sucesso!");
        assertEquals(Constantes.REDIRECT_CIDADES_NOVO, result.getViewName());
    }

    @Test
    public void TesteMetodoSalvarNaoDeveSalvarCidadeComMesmoNomeExistenteERedirecionarParaCidadeNovoViewComMsgAdequada() {
        Cidade cidade = CidadeBuilder.criarCidade();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doThrow(new NomeCidadeJaCadastradaException("Nome de cidade já cadastrado"))
                .when(mockCidadeService).salvar(cidade);
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);

        ModelAndView result = controller.salvar(cidade, mockBindingResult, mockRedirectAttrbitues);

        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);

        Mockito.verifyZeroInteractions(mockRedirectAttrbitues);
        Mockito.verify(mockBindingResult).rejectValue(Constantes.NOME, "Nome de cidade já cadastrado", "Nome de cidade já cadastrado");
        assertEquals(Constantes.CADASTRO_CIDADE_VIEW, result.getViewName());
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void TesteMetodoSalvarQuandoNaoTiverDadosObrigatorioNaoDeveSalvarCidadeERedirecionarParaCidadeNovoView() {
        Cidade cidade = CidadeBuilder.criarCidade();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);

        ModelAndView result = controller.salvar(cidade, mockBindingResult, mockRedirectAttrbitues);

        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);

        Mockito.verifyZeroInteractions(mockRedirectAttrbitues);
        Mockito.verifyZeroInteractions(mockCidadeService);
        assertEquals(Constantes.CADASTRO_CIDADE_VIEW, result.getViewName());
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void testeMetodoPesquisarDeveRetornarListaPaginadaDeCidadesParaPesquisaCidadeView() {
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        List<Cidade> listaCidades = CidadeBuilder.criarListaCidade();
        Page<Cidade> cidadePage = new PageImpl<>(listaCidades, mockPageable, 1);
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);
        Mockito.when(mockCidadesRepo.filtrar(mockCidadeFilter, mockPageable)).thenReturn(cidadePage);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(uriBuilder);

        ModelAndView result = controller.pesquisar(mockCidadeFilter, mockPageable, mockHttpRequest);

        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);
        PageWrapper<Cidade> paginaWrapperResult = (PageWrapper<Cidade>) result.getModel().get(Constantes.PAGINADOR_VIEW);

        assertEquals(Constantes.PESQUISA_CIDADE_VIEW, result.getViewName());
        assertEquals(listaEstados, listaEstadosResult);
        assertEquals(cidadePage.getContent(), paginaWrapperResult.getConteudo());
    }
}