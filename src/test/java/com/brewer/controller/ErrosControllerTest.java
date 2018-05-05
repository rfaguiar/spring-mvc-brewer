package com.brewer.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class ErrosControllerTest {


    private ErrosController controller;

    @Before
    public void iniciarCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.controller = new ErrosController();

    }

    @Test
    public void testeMetodoPaginaNaoEncontradaDeveRetornarErro404() {
        String result = controller.paginaNaoEncontrada();
        assertEquals(HttpStatus.NOT_FOUND.toString(), result);
    }

    @Test
    public void testeMetodoErroServidorDeveRetornarErro500() {
        String result = controller.erroServidor();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(), result);
    }
}