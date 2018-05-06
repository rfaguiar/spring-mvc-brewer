package com.brewer.repository.helper.estilo;

import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Estilo;
import com.brewer.repository.filter.EstiloFilter;
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
import javax.persistence.EntityTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class EstilosImplTest {

    private EstilosImpl estilosImpl;
    @Mock
    private PaginacaoUtil mockPaginacaoUtil;
    @Mock
    private Pageable mockPageable;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        Estilo e1 = new Estilo();
        e1.setNome("Amber Lager");
        Estilo e2 = new Estilo();
        e2.setNome("Dark Lager");
        Estilo e3 = new Estilo();
        e3.setNome("Pale Lager");
        Estilo e4 = new Estilo();
        e4.setNome("Pilsner");

        entityManager.getTransaction().begin();
        entityManager.persist(e1);
        entityManager.persist(e2);
        entityManager.persist(e3);
        entityManager.persist(e4);

        estilosImpl = new EstilosImpl(entityManager, mockPaginacaoUtil);
    }

    @After
    public void end() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarQuandoNaoContemFiltrosDeveRetornarTodosRegistros() {
        EstiloFilter filtro = new EstiloFilter();
        Page<Estilo> result = estilosImpl.filtrar(filtro, mockPageable);
        assertNotNull(result);
        assertEquals(4, result.getContent().size());
        assertEquals("Amber Lager", result.getContent().get(0).getNome());
    }

    @Test
    public void testeMetodoFiltrarQuandoFiltroPorNomeDeveRetornarCompativeisComFiltro() {
        EstiloFilter filtro = new EstiloFilter();
        filtro.setNome("Amber Lager");
        Page<Estilo> result = estilosImpl.filtrar(filtro, mockPageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Amber Lager", result.getContent().get(0).getNome());
    }
}