package com.brewer.service;

import com.brewer.builder.CervejaBuilder;
import com.brewer.model.Cerveja;
import com.brewer.repository.Cervejas;
import com.brewer.service.exception.ImpossivelExcluirEntidadeException;
import com.brewer.storage.FotoStorage;
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

import javax.persistence.PersistenceException;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CadastroCervejaServiceTest {

    private CadastroCervejaService service;
    private Cerveja cerveja;
    @Mock
    private FotoStorage mockFotoStorage;
    @Mock
    private Cervejas mockCervejasRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        cerveja = CervejaBuilder.criarCerveja();
        this.service = new CadastroCervejaService(mockCervejasRepo, mockFotoStorage);
    }

    @Test
    public void testeMetodoSalvarDeveSalvarCerveja() {
        Mockito.when(mockCervejasRepo.save(cerveja)).thenReturn(cerveja);
        service.salvar(this.cerveja);
    }

    @Test
    public void testeMetodoExcluirDeveExcluirCervejaEFoto() {
        service.excluir(cerveja);
        Mockito.verify(mockCervejasRepo).delete(cerveja);
        Mockito.verify(mockFotoStorage).excluir(cerveja.getFoto());
    }

    @Test(expected = ImpossivelExcluirEntidadeException.class)
    public void testeMetodoExcluirQuandoCervejaVendidaDeveLancarExcecaoComMsgAdequada() {
        try {
            Mockito.doThrow(new PersistenceException()).when(mockCervejasRepo).delete(cerveja);
            service.excluir(cerveja);
        }catch (Exception e) {
            assertEquals("Imposivel apagar cerveja. Já foi usada em alguma venda.", e.getMessage());
            throw e;
        }
        fail();
    }
}