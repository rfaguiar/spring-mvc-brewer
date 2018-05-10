package com.brewer.service.event.venda;

import com.brewer.builder.CervejaBuilder;
import com.brewer.builder.VendaBuilder;
import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class VendaListenerTest {

    private VendaListener listener;
    @Mock
    private Cervejas mockCervejasRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.listener = new VendaListener(mockCervejasRepo);
    }

    @Test
    public void testeVendaVendaEmitida() {
        VendaEvent vendaEvent = new VendaEvent(VendaBuilder.criarVenda());
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        Mockito.when(mockCervejasRepo.findOne(Matchers.anyLong())).thenReturn(cerveja);
        listener.vendaEmitida(vendaEvent);
    }
}