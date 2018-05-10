package com.brewer.session;

import com.brewer.builder.CervejaBuilder;
import com.brewer.model.Cerveja;
import com.brewer.model.ItemVenda;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class TabelasItensSessionTest {

    private TabelasItensSession session;
    private Set<TabelaItensVenda> tabelas;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        tabelas = new HashSet<>();
        this.session = new TabelasItensSession(tabelas);
    }

    @Test
    public void testeMetodoAadicionarItemDeveAdicionarItenASessao() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        session.adicionarItem("123456789", cerveja, 10);
        assertEquals("123456789", tabelas.iterator().next().getUuid());
    }


    @Test
    public void testeMetodoAadicionarItem() {
        this.session = new TabelasItensSession();
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        session.adicionarItem("123456789", cerveja, 10);
        assertFalse(session.getItens("123456789").isEmpty());
    }

    @Test
    public void testeMetodoAlterarQuantidadeItens() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        session.adicionarItem("123456789", cerveja, 10);
        session.alterarQuantidadeItens("123456789", cerveja, 20);
        List<ItemVenda> result = session.getItens("123456789");
        assertEquals(new Integer(20), result.get(0).getQuantidade());
    }

    @Test
    public void testeMetodoExcluirItem() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        session.adicionarItem("123456789", cerveja, 10);
        session.excluirItem("123456789", cerveja);
        List<ItemVenda> result = session.getItens("123456789");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testeMetodoGetValorTotal() {
        Cerveja cerveja = CervejaBuilder.criarCerveja();
        session.adicionarItem("123456789", cerveja, 10);
        Object result = session.getValorTotal("123456789");
        assertEquals(new BigDecimal(1230), result);
    }
}