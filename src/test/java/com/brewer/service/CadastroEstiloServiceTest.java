package com.brewer.service;

import com.brewer.builder.EstiloBuilder;
import com.brewer.model.Estilo;
import com.brewer.repository.Estilos;
import com.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CadastroEstiloServiceTest {

    private CadastroEstiloService service;
    @Mock
    private Estilos mockEstilosRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.service = new CadastroEstiloService(mockEstilosRepo);
    }

    @Test(expected = NomeEstiloJaCadastradoException.class)
    public void testeMetodoSalvarQuandoExisteEstiloComMesmoNomeDeveLancaExcecaoComMsgAdequada() {
        try {
            Estilo estilo = EstiloBuilder.criarEstilo();
            Optional<Estilo> estiloOpt = Optional.of(estilo);
            Mockito.when(mockEstilosRepo.findByNomeIgnoreCase(estilo.getNome())).thenReturn(estiloOpt);
            service.salvar(estilo);
        } catch (Exception e) {
            assertEquals("Nome do estilo já cadastrado", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoSalvarDeveSalvarEstilo() {
        Estilo estilo = EstiloBuilder.criarEstilo();
        Optional<Estilo> estiloOpt = Optional.empty();
        Mockito.when(mockEstilosRepo.findByNomeIgnoreCase(estilo.getNome())).thenReturn(estiloOpt);
        service.salvar(estilo);
    }
}