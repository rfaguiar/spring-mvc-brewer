package com.brewer.repository.helper.cliente;

import com.brewer.builder.ClienteBuilder;
import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Cliente;
import com.brewer.repository.filter.ClienteFilter;
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

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class ClientesImplTest {

    private ClientesImpl clientesImpl;
    private Cliente cliente1;
    @Mock
    private PaginacaoUtil mockPaginacao;
    @Mock
    private Pageable mockPageable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        entityManager.getTransaction().begin();
        this.cliente1 = ClienteBuilder.criarCliente();
        cliente1.setNome("cliente1");
        entityManager.persist(cliente1.getEndereco().getCidade().getEstado());
        entityManager.persist(cliente1.getEndereco().getCidade());
        entityManager.persist(cliente1.getEndereco().getEstado());
        entityManager.persist(cliente1);

        Cliente cliente2 = ClienteBuilder.criarCliente();
        entityManager.persist(cliente2.getEndereco().getCidade().getEstado());
        entityManager.persist(cliente2.getEndereco().getCidade());
        entityManager.persist(cliente2.getEndereco().getEstado());
        entityManager.persist(cliente2);

        this.clientesImpl = new ClientesImpl(entityManager, mockPaginacao);
    }

    @After
    public void tearDown() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarSemFiltrosDeveRetornarTodosClientes() {
        Page<Cliente> result = clientesImpl.filtrar(new ClienteFilter(), mockPageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testeMetodoFiltrarComFiltrosDeveRetornarCliente() {
        Cliente cliente3 = ClienteBuilder.criarCliente();
        cliente3.setCodigo(cliente1.getCodigo());
        cliente3.setNome("cliente1");

        ClienteFilter filtro = new ClienteFilter();
        filtro.setNome(cliente3.getNome());
        filtro.setCpfOuCnpj(cliente3.getCpfOuCnpjSemFormatacao());

        Page<Cliente> result = clientesImpl.filtrar(filtro, mockPageable);

        assertEquals(1, result.getContent().size());
        assertTrue(cliente3.equals(result.getContent().get(0)));
        assertTrue(cliente3.getEndereco().equals(result.getContent().get(0).getEndereco()));
        assertEquals(cliente3.toString(), result.getContent().get(0).toString());
        assertEquals(cliente3.getEndereco().toString(), result.getContent().get(0).getEndereco().toString());
    }
}