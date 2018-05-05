package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.ClienteBuilder;
import com.brewer.builder.EstadoBuilder;
import com.brewer.controller.page.PageWrapper;
import com.brewer.model.Cliente;
import com.brewer.model.Estado;
import com.brewer.model.TipoPessoa;
import com.brewer.repository.Clientes;
import com.brewer.repository.Estados;
import com.brewer.repository.filter.ClienteFilter;
import com.brewer.service.CadastroClienteService;
import com.brewer.service.exception.CpfCnpjClienteJaCadastradoException;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({UriComponentsBuilder.class})
public class ClientesControllerTest {

    private ClientesController controller;
    @Mock
    private Clientes mockClientesRepo;
    @Mock
    private CadastroClienteService mockClienteService;
    @Mock
    private Estados mockEstadosRepo;
    @Mock
    private RedirectAttributes mockRedirectAttributes;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockHttpRequest;
    @Mock
    private Pageable mockPageable;
    @Mock
    private ClienteFilter mockClienteFilter;
    @Mock
    private UriComponentsBuilder uriBuilder;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(UriComponentsBuilder.class);
        this.controller = new ClientesController(mockEstadosRepo, mockClienteService, mockClientesRepo);
    }

    @Test
    public void testeMetodoNovoDeveRetornarValoresERedirecionarParaCadastroClienteView() {
        TipoPessoa[] tipoPessoas = TipoPessoa.values();
        Cliente cliente = ClienteBuilder.criarCliente();
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);

        ModelAndView result = controller.novo(cliente);

        TipoPessoa[] tipoPessoasResult = (TipoPessoa[]) result.getModel().get(Constantes.TIPOS_PESSOA);
        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);
        assertEquals(Constantes.CADASTRO_CLIENTE_VIEW, result.getViewName());
        assertArrayEquals(tipoPessoas, tipoPessoasResult);
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void testeMetodoCadastrarDeveCadastrarClienteERedirecionarParaClienteNovoViewComMsgAdequada() {
        Cliente cliente = ClienteBuilder.criarCliente();
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doNothing().when(mockClienteService).salvar(cliente);

        ModelAndView result = controller.cadastrar(cliente, mockBindingResult, mockRedirectAttributes);

        Mockito.verify(mockRedirectAttributes).addFlashAttribute(Constantes.MENSAGEM_VIEW, "Cliente salvo com sucesso");
        assertEquals(Constantes.REDIRECT_CLIENTES_NOVO, result.getViewName());
    }

    @Test
    public void testeMetodoCadastrarNaoDeveCadastrarClienteQuandoCpfOuCpnjJaExistirERedirecionarParaClienteNovoViewComMsgAdequada() {
        Cliente cliente = ClienteBuilder.criarCliente();
        TipoPessoa[] tipoPessoas = TipoPessoa.values();
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
        Mockito.doThrow(new CpfCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado")).when(mockClienteService).salvar(cliente);

        ModelAndView result = controller.cadastrar(cliente, mockBindingResult, mockRedirectAttributes);

        TipoPessoa[] tipoPessoasResult = (TipoPessoa[]) result.getModel().get(Constantes.TIPOS_PESSOA);
        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);

        Mockito.verify(mockBindingResult).rejectValue(Constantes.CPF_OU_CNPJ, "CPF/CNPJ já cadastrado", "CPF/CNPJ já cadastrado");
        Mockito.verifyZeroInteractions(mockRedirectAttributes);
        assertEquals(Constantes.CADASTRO_CLIENTE_VIEW, result.getViewName());
        assertArrayEquals(tipoPessoas, tipoPessoasResult);
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void testeMetodoCadastrarComErrosNaoDeveCadastrarClienteERedirecionarParaClienteNovoViewComMsgAdequada() {
        TipoPessoa[] tipoPessoas = TipoPessoa.values();
        Cliente cliente = ClienteBuilder.criarCliente();
        List<Estado> listaEstados = EstadoBuilder.criarListaEstados();
        Mockito.when(mockEstadosRepo.findAll()).thenReturn(listaEstados);
        Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);

        ModelAndView result = controller.cadastrar(cliente, mockBindingResult, mockRedirectAttributes);

        TipoPessoa[] tipoPessoasResult = (TipoPessoa[]) result.getModel().get(Constantes.TIPOS_PESSOA);
        List<Estado> listaEstadosResult = (List<Estado>) result.getModel().get(Constantes.ESTADOS);

        Mockito.verifyZeroInteractions(mockRedirectAttributes);
        Mockito.verifyZeroInteractions(mockClienteService);
        assertEquals(Constantes.CADASTRO_CLIENTE_VIEW, result.getViewName());
        assertArrayEquals(tipoPessoas, tipoPessoasResult);
        assertEquals(listaEstados, listaEstadosResult);
    }

    @Test
    public void testeMetodoPesquisarDeveRetornarListaDeClientesPaginadaParaPesquisaClienteView() {
        List<Cliente> listaClientes = ClienteBuilder.criarListaCliente();
        Page<Cliente> clientePage = new PageImpl<>(listaClientes, mockPageable, 1);
        Mockito.when(mockClientesRepo.filtrar(mockClienteFilter, mockPageable)).thenReturn(clientePage);
        Mockito.when(mockHttpRequest.getRequestURL()).thenReturn(new StringBuffer("url"));
        Mockito.when(mockHttpRequest.getQueryString()).thenReturn("?");
        Mockito.when(UriComponentsBuilder.fromHttpUrl(Matchers.anyString())).thenReturn(uriBuilder);

        ModelAndView result = controller.pesquisar(mockClienteFilter, mockBindingResult, mockPageable, mockHttpRequest);

        PageWrapper<Cliente> paginaWrapperResult = (PageWrapper<Cliente>) result.getModel().get(Constantes.PAGINADOR_VIEW);

        assertEquals(Constantes.PESQUISA_CLIENTE_VIEW, result.getViewName());
        assertEquals(clientePage.getContent(), paginaWrapperResult.getConteudo());
    }

    @Test
    public void testeMetodoPesquisarPorNomeDeveRetornarListaDeClientesFiltradaPeloArgumentoNome() {
        List<Cliente> listaClientes = ClienteBuilder.criarListaCliente();
        Mockito.when(mockClientesRepo.findByNomeStartingWithIgnoreCase("teste")).thenReturn(listaClientes);

        List<Cliente> result = controller.pesquisar("teste");

        assertEquals(listaClientes, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testeMetodoPesquisarPorNomeQuandoArgumentoMenorQue3CaracteresDeveRetornarExcecao() {
        try {
            controller.pesquisar("te");
            Mockito.verifyZeroInteractions(mockClientesRepo);
        }catch (IllegalArgumentException e) {
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoTratarIllegalArgumentExceptionDeveRetornarHttpStatus400() {
        ResponseEntity<Void> result = controller.tratarIllegalArgumentException(new IllegalArgumentException());

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}