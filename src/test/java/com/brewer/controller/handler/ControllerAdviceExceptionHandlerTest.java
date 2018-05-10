package com.brewer.controller.handler;

import com.brewer.service.exception.NomeEstiloJaCadastradoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class ControllerAdviceExceptionHandlerTest {

    private ControllerAdviceExceptionHandler controllerAdvice;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.controllerAdvice = new ControllerAdviceExceptionHandler();
    }

    @Test
    public void testeMetodoHandleNomeEstiloJaCadastradoExceptionDeveEncapsularErroNaRequisicaoComCodigo400EMsgDaExcecao() {
        NomeEstiloJaCadastradoException exception = new NomeEstiloJaCadastradoException("excessao");
        ResponseEntity<String> result = controllerAdvice.handleNomeEstiloJaCadastradoException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("excessao", result.getBody());
    }
}