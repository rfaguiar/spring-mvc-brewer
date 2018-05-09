package com.brewer.repository.helper.usuario;

import com.brewer.builder.GrupoBuilder;
import com.brewer.builder.PermissaoBuilder;
import com.brewer.builder.UsuarioBuilder;
import com.brewer.helper.JPAHibernateTest;
import com.brewer.model.Grupo;
import com.brewer.model.Permissao;
import com.brewer.model.Usuario;
import com.brewer.repository.filter.UsuarioFilter;
import com.brewer.repository.paginacao.PaginacaoUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class UsuariosImplTest {

    private UsuariosImpl usuariosImpl;
    private Usuario usuario1;
    @Mock
    private Pageable mockPageable;
    @Mock
    private Sort mockSort;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        EntityManager entityManager = JPAHibernateTest.getEntityManager();

        entityManager.getTransaction().begin();
        usuario1 = UsuarioBuilder.criarUsuario();
        List<Grupo> grupos = GrupoBuilder.criarListaGrupos();
        grupos.forEach(grupo -> {
            List<Permissao> permissaos = PermissaoBuilder.criarListaPermissao();
            permissaos.forEach(permissao -> {
                Permissao permissaoP = entityManager.merge(permissao);
                permissao.setCodigo(permissaoP.getCodigo());
            });
            grupo.setPermissoes(permissaos);
            entityManager.persist(grupo);
        });
        entityManager.persist(usuario1);
        usuario1.setGrupos(grupos);

        Usuario usuario2 = UsuarioBuilder.criarUsuario();
        usuario2.setGrupos(null);
        usuario2.setConfirmacaoSenha(usuario2.getSenha());
        entityManager.persist(usuario2);

        this.usuariosImpl = new UsuariosImpl(entityManager, new PaginacaoUtil());
        Mockito.when(mockPageable.getPageSize()).thenReturn(2);
        Mockito.when(mockPageable.getPageNumber()).thenReturn(0);
        Mockito.when(mockPageable.getSort()).thenReturn(mockSort);
        List<Sort.Order> order = new ArrayList<>();
        order.add(new Sort.Order(Sort.Direction.ASC, "codigo"));
        Mockito.when(mockSort.iterator()).thenReturn(order.iterator());
    }

    @After
    public void end() {
        JPAHibernateTest.roolbackEcloseEntityManager();
    }

    @Test
    public void testeMetodoFiltrarQuandoSemFiltrosDeveRetornarTodosUsuarios() {
        Page<Usuario> result = usuariosImpl.filtrar(new UsuarioFilter(), mockPageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testeMetodoFiltrarQuandoFiltrosInformadoDeveRetornarUsuario() {
        Usuario usuario3 = UsuarioBuilder.criarUsuario();
        usuario3.setCodigo(usuario1.getCodigo());
        usuario3.setGrupos(usuario1.getGrupos());
        UsuarioFilter filtro = new UsuarioFilter();
        filtro.setNome(usuario3.getNome());
        filtro.setEmail(usuario3.getEmail());
        List<Grupo> filtroGrupos = new ArrayList<>();
        Grupo grupo = new Grupo();
        grupo.setCodigo(usuario3.getGrupos().get(0).getCodigo());
        filtroGrupos.add(grupo);
        filtro.setGrupos(filtroGrupos);

        Page<Usuario> result = usuariosImpl.filtrar(filtro, mockPageable);
        assertEquals(1, result.getContent().size());
        assertTrue(usuario3.equals(result.getContent().get(0)));
        assertEquals(usuario3.toString(), result.getContent().get(0).toString());
        assertTrue(usuario3.getGrupos().get(0).getPermissoes().get(0)
                .equals(result.getContent().get(0).getGrupos().get(0).getPermissoes().get(0)));
        assertEquals(usuario3.getGrupos().get(0).getPermissoes().get(0).toString(),
                result.getContent().get(0).getGrupos().get(0).getPermissoes().get(0).toString());
    }

    @Test
    public void testeMetodoPorEmailEAtivoDeveRetornarUsuario() {
        Optional<Usuario> result = usuariosImpl.porEmailEAtivo("emailteste@teste.com");
        assertTrue(result.isPresent());
    }

    @Test
    public void testeMetodoPermissoesDeveRetornarPermissoesDoUsuarioInformado() {
        List<String> result = usuariosImpl.permissoes(usuario1);
        assertEquals(1, result.size());
        assertEquals("nomePermissao", result.get(0));
    }

    @Test
    public void testeMetodoBuscarComGruposDeveRetornarUsuarioPeloIdComListaDeGrupos() {
        Usuario result = usuariosImpl.buscarComGrupos(usuario1.getCodigo());
        assertEquals(usuario1, result);
        assertEquals(1, result.getGrupos().size());
    }
}