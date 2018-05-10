package com.brewer.service;

import com.brewer.builder.CidadeBuilder;
import com.brewer.model.Cidade;
import com.brewer.repository.Cidades;
import com.brewer.service.exception.NomeCidadeJaCadastradaException;
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
public class CadastroCidadeServiceTest {

    private CadastroCidadeService service;
    @Mock
    private Cidades mockCidadesRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.service = new CadastroCidadeService(mockCidadesRepo);
    }

    @Test(expected = NomeCidadeJaCadastradaException.class)
    public void testeMetodoSalvarQuandoCidadeComMesmoNomeEEstadoDeveLancarExcepcaoComMsgAdequada() {
        try {
            Cidade cidade = CidadeBuilder.criarCidade();
            Optional<Cidade> cidadeOpt = Optional.of(cidade);
            Mockito.when(mockCidadesRepo.findByNomeAndEstado(cidade.getNome(), cidade.getEstado())).thenReturn(cidadeOpt);
            service.salvar(cidade);
        }catch (Exception e) {
            assertEquals("Nome de cidade já cadastrado", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoSalvarDeveSalvarCidadeNova() {
        Cidade cidade = CidadeBuilder.criarCidade();
        Optional<Cidade> cidadeOpt = Optional.empty();
        Mockito.when(mockCidadesRepo.findByNomeAndEstado(cidade.getNome(), cidade.getEstado())).thenReturn(cidadeOpt);
        service.salvar(cidade);
    }
}