package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.CervejaBuilder;
import com.brewer.builder.CervejaDTOBuilder;
import com.brewer.builder.EstiloBuilder;
import com.brewer.controller.page.PageWrapper;
import com.brewer.dto.CervejaDTO;
import com.brewer.model.Cerveja;
import com.brewer.model.Estilo;
import com.brewer.model.Origem;
import com.brewer.model.Sabor;
import com.brewer.repository.Cervejas;
import com.brewer.repository.Estilos;
import com.brewer.repository.filter.CervejaFilter;
import com.brewer.service.CadastroCervejaService;
import com.brewer.service.exception.ImpossivelExcluirEntidadeException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({Cervejas.class, Estilos.class, CadastroCervejaService.class, BindingResult.class, RedirectAttributes.class,
        HttpServletRequest.class, Pageable.class, CervejaFilter.class, UriComponentsBuilder.class})
public class CervejasControllerTest {

    private CervejasController controller;
    private List<Estilo> listaEstilos;
    @Mock
    private Cervejas mockCervejasRepo;
    @Mock
    private Estilos mockEstilosRepo;
    @Mock
    private CadastroCervejaService mockCervejaService;
    @Mock
    private BindingResult mockBindResult;
    @Mock
    private RedirectAttributes mockRedirectAttributes;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Pageable mockPegeable;
    @Mock
    private CervejaFilter mockCervejaFilter;
    @Mock
    private UriComponentsBuilder uriBuilder;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        controller = new CervejasController(mockCervejaService, mockEstilosRepo, mockCervejasRepo);
        listaEstilos = EstiloBuilder.criarListaEstilos();
        Mockito.when(mockEstilosRepo.findAll()).thenReturn(listaEstilos);
    }

    @Test
    public void tesMetodoNovoDevePrepararDadosDaView() {
        ModelAndView result = controller.novo(CervejaBuilder.criarCerveja());

        Sabor[] sabores = (Sabor[]) result.getModel().get(Constantes.SABORES);
        Origem[] origens = (Origem[]) result.getModel().get(Constantes.ORIGENS);
        List<Estilo> estilos = (List<Estilo>) result.getModel().get(Constantes.ESTILOS);

        assertEquals(Constantes.CADASTRO_CERVEJA_VIEW, result.getViewName());
        assertArrayEquals(Sabor.values(), sabores);
        assertArrayEquals(Origem.values(), origens);
        assertTrue(EstiloBuilder.validarListaEstilo(listaEstilos, estilos));
    }

    @Test
    public void testeMetodoSalvarQuandoRecebeUmaCervejaSalvarCervejaEDeveRedirecionarParaTelaDenovoComMensagemDeSucesso() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.doNothing().when(mockCervejaService).salvar(cerveja);
        Mockito.when(mockBindResult.hasErrors()).thenReturn(true);

        ModelAndView result = controller.salvar(cerveja, mockBindResult, mockRedirectAttributes);

        Sabor[] sabores = (Sabor[]) result.getModel().get(Constantes.SABORES);
        Origem[] origens = (Origem[]) result.getModel().get(Constantes.ORIGENS);
        List<Estilo> estilos = (List<Estilo>) result.getModel().get(Constantes.ESTILOS);

        Mockito.verifyZeroInteractions(mockCervejaService);
        assertEquals(Constantes.CADASTRO_CERVEJA_VIEW, result.getViewName());
        assertArrayEquals(Sabor.values(), sabores);
        assertArrayEquals(Origem.values(), origens);
        assertTrue(EstiloBuilder.validarListaEstilo(listaEstilos, estilos));
    }

    @Test
    public void testeMetodoSalvarQuandoRecebeUmaCervejaComDadosIncorretosDeveRedirecionarParaTelaDeNovo() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.doNothing().when(mockCervejaService).salvar(cerveja);
        Mockito.when(mockBindResult.hasErrors()).thenReturn(false);
        ModelAndView result = controller.salvar(cerveja, mockBindResult, mockRedirectAttributes);
        Mockito.verify(mockRedirectAttributes).addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cerveja salva com sucesso");
        assertEquals(Constantes.REDIRECT_CERVEJAS_NOVO_VIEW, result.getViewName());
    }

    @Test
    public void testeMetodopesquisarQuandoSolicitaUmaPesquisaPaginada() {
        List<Cerveja> listaCervejas = CervejaBuilder.criarListacervejas();
        PageImpl<Cerveja> cervejasPage = new PageImpl<>(listaCervejas, mockPegeable, 1);
        Mockito.when(mockCervejasRepo.filtrar(mockCervejaFilter, mockPegeable)).thenReturn(cervejasPage);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(uriBuilder);

        ModelAndView result = controller.pesquisar(mockCervejaFilter, mockPegeable, mockHttpRequest);

        Sabor[] sabores = (Sabor[]) result.getModel().get(Constantes.SABORES);
        Origem[] origens = (Origem[]) result.getModel().get(Constantes.ORIGENS);
        List<Estilo> estilos = (List<Estilo>) result.getModel().get(Constantes.ESTILOS);
        PageWrapper<Cerveja> paginaWrapper = (PageWrapper<Cerveja>) result.getModel().get(Constantes.PAGINADOR_VIEW);

        assertEquals(Constantes.PESQUISA_CERVEJA_VIEW, result.getViewName());
        assertArrayEquals(Sabor.values(), sabores);
        assertArrayEquals(Origem.values(), origens);
        assertTrue(EstiloBuilder.validarListaEstilo(listaEstilos, estilos));
        assertTrue(CervejaBuilder.validarListaCerveja(listaCervejas, paginaWrapper.getConteudo()));
    }

    @Test
    public void testeMetodoPesquisarPorSkuOuNome() {
        List<CervejaDTO> cervejasDto = CervejaDTOBuilder.criarListaCervejaDto();
        Mockito.when(mockCervejasRepo.porSkuOuNome("sku")).thenReturn(cervejasDto);

        List<CervejaDTO> result = controller.pesquisar("sku");

        assertTrue(CervejaDTOBuilder.validarListaCervejaDto(cervejasDto, result));
    }

    @Test
    public void testeMetodoExcluirPorCodigoDaCervejaDeveRetornarHttpStatus200() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.doNothing().when(mockCervejaService).excluir(cerveja);

        ResponseEntity result = controller.excluir(cerveja);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testeMetodoExcluirPorCodigoDaCervejaQuandoLancaExcecaoDeveRetornarHttpStatus400() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.doThrow(new ImpossivelExcluirEntidadeException("msg erro")).when(mockCervejaService).excluir(cerveja);

        ResponseEntity result = controller.excluir(cerveja);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("msg erro", result.getBody().toString());
    }

    @Test
    public void testeMetodoEditarQuandoEditaPorCodigoDaCerveja() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        ModelAndView result = controller.editar(cerveja);

        Sabor[] sabores = (Sabor[]) result.getModel().get(Constantes.SABORES);
        Origem[] origens = (Origem[]) result.getModel().get(Constantes.ORIGENS);
        List<Estilo> estilos = (List<Estilo>) result.getModel().get(Constantes.ESTILOS);

        assertEquals(Constantes.CADASTRO_CERVEJA_VIEW, result.getViewName());
        assertArrayEquals(Sabor.values(), sabores);
        assertArrayEquals(Origem.values(), origens);
        assertTrue(EstiloBuilder.validarListaEstilo(listaEstilos, estilos));
    }

}