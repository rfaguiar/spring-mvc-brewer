package com.brewer.controller;

import com.brewer.Constantes;
import com.brewer.builder.ValorItensEstoqueBuilder;
import com.brewer.dto.ValorItensEstoque;
import com.brewer.repository.Cervejas;
import com.brewer.repository.Clientes;
import com.brewer.repository.Vendas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class DashboardControllerTest {

    private DashboardController controller;
    @Mock
    private Clientes mockClientesRepo;
    @Mock
    private Cervejas mockCervejasRepo;
    @Mock
    private Vendas mockVendasRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.controller = new DashboardController(mockVendasRepo, mockCervejasRepo, mockClientesRepo);
    }

    @Test
    public void testeMetodoDashboardDeveRetornarVendasPorAnoEVendasPorMesETicketMedioNoAnoEItensDoEstoqueETotalDeClientesParaDashboardView() {
        BigDecimal totalVendasNoAno = new BigDecimal(50);
        BigDecimal totalVendasNoMes = new BigDecimal(10);
        BigDecimal totalTicketMedioNoAno = new BigDecimal(30);
        Long totalClientes = new Long(10);
        ValorItensEstoque totalItensEstoque = ValorItensEstoqueBuilder.criarValorItensEstoque();
        Mockito.when(mockVendasRepo.valorTotalNoAno()).thenReturn(totalVendasNoAno);
        Mockito.when(mockVendasRepo.valorTotalNoMes()).thenReturn(totalVendasNoMes);
        Mockito.when(mockVendasRepo.valorTicketMedioNoAno()).thenReturn(totalTicketMedioNoAno);
        Mockito.when(mockCervejasRepo.valorItensEstoque()).thenReturn(totalItensEstoque);
        Mockito.when(mockClientesRepo.count()).thenReturn(totalClientes);

        ModelAndView result = controller.dashboard();

        BigDecimal totalVendasNoAnoResult = (BigDecimal) result.getModel().get(Constantes.VENDAS_NO_ANO);
        BigDecimal totalVendasNoMesResult = (BigDecimal) result.getModel().get(Constantes.VENDAS_NO_MES);
        BigDecimal totalTicketMedioNoAnoResult = (BigDecimal) result.getModel().get(Constantes.TICKET_MEDIO);
        Long totalClientesResult = (Long) result.getModel().get(Constantes.TOTAL_CLIENTES);
        ValorItensEstoque totalItensEstoqueResult = (ValorItensEstoque) result.getModel().get(Constantes.VALOR_ITENS_ESTOQUE);

        assertEquals(Constantes.DASHBOARD_VIEW, result.getViewName());
        assertEquals(totalVendasNoAno, totalVendasNoAnoResult);
        assertEquals(totalVendasNoMes, totalVendasNoMesResult);
        assertEquals(totalTicketMedioNoAno, totalTicketMedioNoAnoResult);
        assertEquals(totalClientes, totalClientesResult);
        assertEquals(totalItensEstoque, totalItensEstoqueResult);
    }
}