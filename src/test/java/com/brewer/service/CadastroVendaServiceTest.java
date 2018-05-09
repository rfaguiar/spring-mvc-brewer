package com.brewer.service;

import com.brewer.builder.VendaBuilder;
import com.brewer.model.StatusVenda;
import com.brewer.model.Venda;
import com.brewer.repository.Vendas;
import com.brewer.service.event.venda.VendaEvent;
import com.brewer.service.exception.VendaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CadastroVendaServiceTest {

    private CadastroVendaService service;
    private Venda venda;
    @Mock
    private ApplicationEventPublisher mockApplicationPublisher;
    @Mock
    private Vendas mockVendasRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.venda = VendaBuilder.criarVenda();

        this.service = new CadastroVendaService(mockVendasRepo, mockApplicationPublisher);
    }

    @Test(expected = VendaException.class)
    public void testeMetodoSalvarQuandoStatusVendaCanceladaDeveLancarExcecaoComMsgEdequada() {
        try {
            venda.setStatus(StatusVenda.CANCELADA);
            service.salvar(venda);
            Mockito.verifyZeroInteractions(mockVendasRepo);
        } catch (Exception e) {
            assertEquals("Usúario tentando salvar uma venda proibida", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoSalvarQuandoVendaNovaDeveLancarSetarDataCriacao() {
        Mockito.when(mockVendasRepo.saveAndFlush(Matchers.any(Venda.class))).thenReturn(venda);
        venda.setCodigo(null);
        venda.setDataCriacao(null);
        Venda result = service.salvar(venda);
        assertNotNull(result.getDataCriacao());
    }

    @Test
    public void testeMetodoSalvarQuandoVendaNaoNovaDeveConsultarVendaPorIdEUsarDataCriacaoExistente() {
        Mockito.when(mockVendasRepo.saveAndFlush(Matchers.any(Venda.class))).thenReturn(venda);
        Venda venda1 = VendaBuilder.criarVenda();
        venda1.setDataCriacao(LocalDateTime.now());
        Mockito.when(mockVendasRepo.findOne(new Long(1))).thenReturn(venda1);
        venda.setCodigo(new Long(1));
        venda.setDataCriacao(null);
        venda.setDataEntrega(LocalDate.now());
        Venda result = service.salvar(venda);
        assertEquals(venda1.getDataCriacao().toString(), result.getDataCriacao().toString());
        assertNotNull(result.getDataHoraEntrega());
    }

    @Test
    public void testeMetodoEmitirDeveMudarStatusParaEmitidaEAtualiarVendaEPublicarAVenda() {
        venda.setStatus(StatusVenda.ORCAMENTO);
        Mockito.when(mockVendasRepo.saveAndFlush(Matchers.any(Venda.class))).thenReturn(venda);
        Mockito.when(mockVendasRepo.findOne(new Long(1))).thenReturn(venda);
        service.emitir(venda);
        Mockito.verify(mockApplicationPublisher).publishEvent(Matchers.any(VendaEvent.class));
        assertEquals(StatusVenda.EMITIDA, venda.getStatus());
    }

    @Test
    public void testeMetodoCancelarDeveMudarStatusParaCanceladaEAtualizarVenda() {
        venda.setStatus(StatusVenda.ORCAMENTO);
        Mockito.when(mockVendasRepo.saveAndFlush(Matchers.any(Venda.class))).thenReturn(venda);
        Mockito.when(mockVendasRepo.findOne(new Long(1))).thenReturn(venda);
        service.cancelar(venda);
        assertEquals(StatusVenda.CANCELADA, venda.getStatus());
    }
}