package com.brewer.repository.helper.venda;

import com.brewer.builder.ItemVendaBuilder;
import com.brewer.builder.VendaBuilder;
import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Cerveja;
import com.brewer.model.Estilo;
import com.brewer.model.ItemVenda;
import com.brewer.model.Venda;
import com.brewer.repository.filter.VendaFilter;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class VendasImplTest {

    private VendasImpl vendasImpl;
    private Venda venda1;
    private List<ItemVenda> itemVendas;
    @Mock
    private PaginacaoUtil mockPaginacaoUtil;
    @Mock
    private Pageable mockPageable;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        venda1 = VendaBuilder.criarVenda();
        venda1.setCodigo(null);
        venda1.setItens(null);
        venda1.setUsuario(null);
        Venda venda2 = VendaBuilder.criarVenda();
        venda2.setCodigo(null);
        venda2.setItens(null);
        venda2.setUsuario(null);

        //persistencia Cliente
        entityManager.getTransaction().begin();

        //Persistencia ItemVenda, Cerveja e Estilo
        itemVendas = ItemVendaBuilder
                .criarListaItenVenda();
                itemVendas.forEach(itemVenda -> {
                    Cerveja cerveja = itemVenda.getCerveja();
                    Estilo estiloPersistido = entityManager.merge(cerveja.getEstilo());
                    cerveja.setEstilo(estiloPersistido);
                    Cerveja cervejaPersistida = entityManager.merge(itemVenda.getCerveja());
                    itemVenda.setCerveja(cervejaPersistida);
                    itemVenda.setVenda(venda1);
                    ItemVenda itemPersistido = entityManager.merge(itemVenda);
                    itemVenda.setCodigo(itemPersistido.getCodigo());
                });
        venda1.setItens(itemVendas);

        //Persistencia Vendas
        entityManager.persist(venda1.getCliente().getEndereco().getCidade().getEstado());
        entityManager.persist(venda1.getCliente().getEndereco().getCidade());
        entityManager.persist(venda1.getCliente().getEndereco().getEstado());
        entityManager.persist(venda1.getCliente());
        Venda persistido = entityManager.merge(venda1);
        venda1.setCodigo(persistido.getCodigo());

        entityManager.persist(venda2.getCliente().getEndereco().getCidade().getEstado());
        entityManager.persist(venda2.getCliente().getEndereco().getCidade());
        entityManager.persist(venda2.getCliente().getEndereco().getEstado());
        entityManager.persist(venda2.getCliente());
        entityManager.persist(venda2);

        vendasImpl = new VendasImpl(entityManager, mockPaginacaoUtil);
    }

    @After
    public void end() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarSemFiltrosDeveRetornarTodosRegistros() {
        Page<Venda> result = vendasImpl.filtrar(new VendaFilter(), mockPageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testeMetodoFiltrarComFiltrosDeveRetornarUmRegistro() {
        VendaFilter filtro = new VendaFilter();
        filtro.setCodigo(venda1.getCodigo());
        filtro.setStatus(venda1.getStatus());
        filtro.setDesde(LocalDate.now().minusDays(1));
        filtro.setAte(LocalDate.now().plusDays(1));
        filtro.setValorMinimo(new BigDecimal(400));
        filtro.setValorMaximo(new BigDecimal(800));
        filtro.setNomeCliente(venda1.getCliente().getNome());
        filtro.setCpfOuCnpjCliente(venda1.getCliente().getCpfOuCnpjSemFormatacao());

        Page<Venda> result = vendasImpl.filtrar(filtro, mockPageable);

        assertEquals(1, result.getContent().size());
        assertEquals(venda1, result.getContent().get(0));
    }

    @Test
    public void buscarComItens() {
        Venda result = vendasImpl.buscarComItens(venda1.getCodigo());
        assertEquals(itemVendas.size(), result.getItens().size());
        assertEquals(itemVendas.get(0), result.getItens().get(0));
    }

    @Test
    public void valorTotalNoAno() {
        BigDecimal result = vendasImpl.valorTotalNoAno();
        assertEquals("912.00", result.toString());
    }

    @Test
    public void valorTotalNoMes() {
        BigDecimal result = vendasImpl.valorTotalNoMes();
        assertEquals("912.00", result.toString());
    }

    @Test
    public void valorTicketMedioNoAno() {
        BigDecimal result = vendasImpl.valorTicketMedioNoAno();
        assertEquals("456", result.toString());
    }

    @Test
    public void totalPorMes() {
    }

    @Test
    public void totalPorOrigem() {
    }
}