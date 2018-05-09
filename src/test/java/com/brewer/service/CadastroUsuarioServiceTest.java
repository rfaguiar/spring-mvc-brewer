package com.brewer.service;

import com.brewer.builder.UsuarioBuilder;
import com.brewer.model.Usuario;
import com.brewer.repository.Usuarios;
import com.brewer.service.exception.EmailUsuarioJaCadastradoException;
import com.brewer.service.exception.SenhaObrigatoriaUsuarioException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class CadastroUsuarioServiceTest {

    private CadastroUsuarioService service;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private Usuarios mockUsuarioRepo;
    private Usuario usuario;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        usuario = UsuarioBuilder.criarUsuario();
        this.service = new CadastroUsuarioService(mockUsuarioRepo, mockPasswordEncoder);
    }

    @Test(expected = EmailUsuarioJaCadastradoException.class)
    public void testeMetodoSalvarQuandoUsuarioExistenteDeveLancarExcecaoComMsgAdequada() {
        try {
            Optional<Usuario> usuarioOpt = Optional.of(usuario);
            Mockito.when(mockUsuarioRepo.findByEmail(usuario.getEmail())).thenReturn(usuarioOpt);
            service.salvar(this.usuario);
        }catch (Exception e) {
            assertEquals("E-mail já cadastrado", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test(expected = SenhaObrigatoriaUsuarioException.class)
    public void testeMetodoSalvarQuandoNovoUsuarioESenhaVaziaDeveLancarExcecaoComMsgAdequada() {
        try {
            usuario.setCodigo(null);
            usuario.setSenha("");
            Optional<Usuario> usuarioOpt = Optional.empty();
            Mockito.when(mockUsuarioRepo.findByEmail(usuario.getEmail())).thenReturn(usuarioOpt);
            service.salvar(this.usuario);
        }catch (Exception e) {
            assertEquals("Senha é obrigatória para novo usuário", e.getMessage());
            throw e;
        }
        fail();
    }

    @Test
    public void testeMetodoSalvarQuandoUsuarioNovoDeveCodificarSenha() {
        usuario.setCodigo(null);
        Optional<Usuario> usuarioOpt = Optional.empty();
        Mockito.when(mockUsuarioRepo.findByEmail(usuario.getEmail())).thenReturn(usuarioOpt);
        Mockito.when(mockUsuarioRepo.save(usuario)).thenReturn(usuario);
        Mockito.when(mockPasswordEncoder.encode(Matchers.anyString())).thenReturn("senhaCodificada");
        service.salvar(this.usuario);
        assertEquals("senhaCodificada", usuario.getSenha());
        assertEquals("senhaCodificada", usuario.getConfirmacaoSenha());
    }

    @Test
    public void testeMetodoSalvarQuandoAtualizarUsuarioDeveSetarSenhaEAtivoDoExistente() {
        usuario.setCodigo(new Long(1));
        usuario.setSenha(null);
        usuario.setAtivo(null);
        Usuario usuario1 = UsuarioBuilder.criarUsuario();
        usuario1.setSenha("existente");
        usuario1.setAtivo(Boolean.TRUE);
        Optional<Usuario> usuarioOpt = Optional.of(usuario1);
        usuario1.setEmail("outroEmail@teste.com");
        Mockito.when(mockUsuarioRepo.findByEmail(usuario.getEmail())).thenReturn(usuarioOpt);
        Mockito.when(mockUsuarioRepo.save(usuario)).thenReturn(usuario);
        Mockito.when(mockPasswordEncoder.encode(Matchers.anyString())).thenReturn("senhaCodificada");
        service.salvar(this.usuario);
        assertEquals("existente", usuario.getSenha());
        assertEquals("existente", usuario.getConfirmacaoSenha());
        assertTrue(usuario.getAtivo());
    }

    @Test
    public void testeMetodoAlterarStatusDeveAtivarStatus() {
        Long[] codigos = {1l};
        List<Usuario> listaUsuario = UsuarioBuilder.criarListaUsuarios();
        listaUsuario.get(0).setCodigo(1l);
        listaUsuario.get(0).setAtivo(false);
        Mockito.when(mockUsuarioRepo.findByCodigoIn(codigos)).thenReturn(listaUsuario);
        service.alterarStatus(codigos, StatusUsuario.ATIVAR);
        assertTrue(listaUsuario.get(0).getAtivo());
    }

    @Test
    public void testeMetodoAlterarStatusDeveDesativarStatus() {
        Long[] codigos = {1l};
        List<Usuario> listaUsuario = UsuarioBuilder.criarListaUsuarios();
        listaUsuario.get(0).setCodigo(1l);
        listaUsuario.get(0).setAtivo(true);
        Mockito.when(mockUsuarioRepo.findByCodigoIn(codigos)).thenReturn(listaUsuario);
        service.alterarStatus(codigos, StatusUsuario.DESATIVAR);
        assertFalse(listaUsuario.get(0).getAtivo());
    }
}