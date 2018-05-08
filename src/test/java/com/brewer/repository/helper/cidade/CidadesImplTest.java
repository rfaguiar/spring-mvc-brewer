package com.brewer.repository.helper.cidade;

import com.brewer.builder.CidadeBuilder;
import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Cidade;
import com.brewer.repository.filter.CidadeFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CidadesImplTest {

    private CidadesImpl cidadesImpl;
    private Cidade cidade1;
    @Mock
    private PaginacaoUtil mockPaginacao;
    @Mock
    private Pageable mockPageable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        entityManager.getTransaction().begin();
        this.cidade1 = CidadeBuilder.criarCidade();
        cidade1.setNome("cidade1");
        entityManager.persist(cidade1.getEstado());
        entityManager.persist(cidade1);

        Cidade cidade2 = CidadeBuilder.criarCidade();
        entityManager.persist(cidade2.getEstado());
        entityManager.persist(cidade2);

        this.cidadesImpl = new CidadesImpl(entityManager, mockPaginacao);
    }

    @After
    public void tearDown() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarSemFiltrosDeveRetornarTodosRegistros() {
        Page<Cidade> result = cidadesImpl.filtrar(new CidadeFilter(), mockPageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testeMetodoFiltrarComFiltrosDeveRetornarRegistro() {
        Cidade cidade3 = CidadeBuilder.criarCidade();
        cidade3.setNome("cidade1");
        cidade3.setCodigo(cidade1.getCodigo());
        cidade3.getEstado().setCodigo(cidade1.getEstado().getCodigo());
        CidadeFilter filtro = new CidadeFilter();
        filtro.setEstado(cidade3.getEstado());
        filtro.setNome(cidade3.getNome());
        Page<Cidade> result = cidadesImpl.filtrar(filtro, mockPageable);
        assertEquals(1, result.getContent().size());
        assertTrue(cidade3.getEstado().equals(result.getContent().get(0).getEstado()));
        assertEquals(cidade3.getEstado().toString(), result.getContent().get(0).getEstado().toString());
        assertTrue(cidade3.equals(result.getContent().get(0)));
        assertEquals(cidade3.toString(), result.getContent().get(0).toString());
    }
}