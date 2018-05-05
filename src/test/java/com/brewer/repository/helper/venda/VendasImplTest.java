package com.brewer.repository.helper.venda;

import com.brewer.repository.paginacao.PaginacaoUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class VendasImplTest {

    private VendasImpl vendasImpl;
    @Mock
    private PaginacaoUtil mockPaginacaoUtil;

    @After
    public void end() {
    }

    @Test
    public void filtrar() {
    }

    @Test
    public void buscarComItens() {
    }

    @Test
    public void valorTotalNoAno() {
    }

    @Test
    public void valorTotalNoMes() {
    }

    @Test
    public void valorTicketMedioNoAno() {
    }

    @Test
    public void totalPorMes() {
    }

    @Test
    public void totalPorOrigem() {
    }
}