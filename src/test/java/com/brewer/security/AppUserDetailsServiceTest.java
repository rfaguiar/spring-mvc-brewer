package com.brewer.security;

import com.brewer.builder.PermissaoBuilder;
import com.brewer.builder.UsuarioBuilder;
import com.brewer.model.Permissao;
import com.brewer.model.Usuario;
import com.brewer.repository.Usuarios;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class AppUserDetailsServiceTest {

    private AppUserDetailsService service;
    @Mock
    private Usuarios mockUsuariosRepo;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.service = new AppUserDetailsService(mockUsuariosRepo);
    }

    @Test
    public void testeMetodoLoadUserByUsernameDeveRetornarPermissoesDoUsuarioPorEmail() {
        Usuario usuario = UsuarioBuilder.criarUsuario();
        Optional<Usuario> usuarioOpt = Optional.of(usuario);
        Mockito.when(mockUsuariosRepo.porEmailEAtivo(Matchers.anyString())).thenReturn(usuarioOpt);
        List<String> permStr = PermissaoBuilder
                .criarListaPermissao()
                .stream()
                .map(Permissao::getNome)
                .collect(Collectors.toList());

        Mockito.when(mockUsuariosRepo.permissoes(usuario)).thenReturn(permStr);
        UserDetails result = this.service.loadUserByUsername("email@teste.com");

        assertNotNull(result);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testeMetodoLoadUserByUsernameQuandoUsuarioNaoEncontradoPeloEmailDeveLancarExcecaoComMsgAdequada() {
        try {
            Optional<Usuario> usuarioOpt = Optional.empty();
            Mockito.when(mockUsuariosRepo.porEmailEAtivo(Matchers.anyString())).thenReturn(usuarioOpt);

            this.service.loadUserByUsername("email@teste.com");

            Mockito.verifyZeroInteractions(mockUsuariosRepo.permissoes(Matchers.any(Usuario.class)));
        } catch (Exception e) {
            assertEquals("Usuário e/ou senha incorretos", e.getMessage());
            throw e;
        }
        fail();
    }
}