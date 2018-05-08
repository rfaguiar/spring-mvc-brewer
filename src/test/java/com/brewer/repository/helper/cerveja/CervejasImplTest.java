package com.brewer.repository.helper.cerveja;

import com.brewer.builder.CervejaBuilder;
import com.brewer.dto.CervejaDTO;
import com.brewer.dto.ValorItensEstoque;
import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Cerveja;
import com.brewer.model.Estilo;
import com.brewer.model.Origem;
import com.brewer.model.Sabor;
import com.brewer.repository.filter.CervejaFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
import com.brewer.storage.FotoStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CervejasImplTest {

    private CervejasImpl cervejasImpl;
    private Cerveja cerveja1;
    @Mock
    private FotoStorage mockFotoStorage;
    @Mock
    private PaginacaoUtil mockPaginacao;
    @Mock
    private Pageable mockPageable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        cerveja1 = CervejaBuilder.criarCerveja();
        cerveja1.setNome("cerveja1");
        cerveja1.setSku("CE9999");
        entityManager.getTransaction().begin();
        entityManager.persist(cerveja1.getEstilo());
        entityManager.persist(cerveja1);

        Cerveja cerveja2 = CervejaBuilder.criarCerveja();
        cerveja2.getEstilo().setNome("Estilo2");
        entityManager.persist(cerveja2.getEstilo());
        entityManager.persist(cerveja2);

        this.cervejasImpl = new CervejasImpl(entityManager, mockPaginacao, mockFotoStorage);
    }

    @After
    public void end() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarQuandoSemFiltrosDeveRetornarTodasCervejasPaginada() {
        Page<Cerveja> result = cervejasImpl.filtrar(new CervejaFilter(), mockPageable);
        assertEquals(2, result.getContent().size());
        assertEquals(cerveja1, result.getContent().get(0));
    }

    @Test
    public void testeMetodoFiltrarQuandoComFiltrosDeveRetornarCervejasPaginada() {
        Cerveja cerveja3 = CervejaBuilder.criarCerveja();
        cerveja3.setCodigo(cerveja1.getCodigo());
        cerveja3.setNome("cerveja1");
        cerveja3.setSku("CE9999");

        CervejaFilter filtro = new CervejaFilter();
        filtro.setSku(cerveja1.getSku());
        filtro.setNome(cerveja1.getNome());
        filtro.setEstilo(cerveja1.getEstilo());
        filtro.setSabor(cerveja1.getSabor());
        filtro.setOrigem(cerveja1.getOrigem());
        filtro.setValorDe(new BigDecimal(100));
        filtro.setValorAte(new BigDecimal(150));

        Page<Cerveja> result = cervejasImpl.filtrar(filtro, mockPageable);
        assertEquals(1, result.getContent().size());
        assertTrue(cerveja3.equals(result.getContent().get(0)));
        assertEquals(cerveja3.toString(), result.getContent().get(0).toString());
    }

    @Test
    public void testeMetodoPorSkuOuNomeQuandoArgumentoNomeDeveRetornarConformeArgumento() {
        List<CervejaDTO> result = cervejasImpl.porSkuOuNome(cerveja1.getNome());
        assertEquals(1, result.size());
        assertEquals(cerveja1.getNome(), result.get(0).getNome());
    }

    @Test
    public void testeMetodoPorSkuOuNomeQuandoArgumentoSkuDeveRetornarConformeArgumento() {
        List<CervejaDTO> result = cervejasImpl.porSkuOuNome(cerveja1.getSku());
        assertEquals(1, result.size());
        assertEquals(cerveja1.getSku(), result.get(0).getSku());
    }

    @Test
    public void testeMetodoValorItensEstoqueDeveRetornarTotalDeItensDoEstoqueEValorTotalDosItensDoEstoque() {
        ValorItensEstoque result = cervejasImpl.valorItensEstoque();
        assertEquals(new Long(1000), result.getTotalItens());
        assertEquals("123000.00", result.getValor().toString());
    }
}