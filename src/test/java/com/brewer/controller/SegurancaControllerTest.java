package com.brewer.controller;

import com.brewer.Constantes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class SegurancaControllerTest {

    private SegurancaController controller;
    @Mock
    private User mockUser;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.controller = new SegurancaController();
    }

    @Test
    public void testeMetodoLoginDeveRedirecionarParaCervejasView() {
        String result = controller.login(mockUser);
        assertEquals(Constantes.REDIRECT_CRVEJAS_VIEW, result);
    }

    @Test
    public void testeMetodoLoginQuandoUsuarioNuloDeveRetornarLoginView() {
        String result = controller.login(null);
        assertEquals(Constantes.LOGIN_VIEW, result);
    }

    @Test
    public void testeMetodoAcessoNegadoDeveRetornarHttpCodigo401() {
        String result = controller.acessoNegado();
        assertEquals(HttpStatus.UNAUTHORIZED.toString(), result);
    }
}