package com.brewer.controller.converter;

import com.brewer.model.Estado;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class EstadoConverterTest {

    private EstadoConverter converter;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.converter = new EstadoConverter();
    }

    @Test
    public void testeMetodoConvertDeveRetornarEstadoComIdConvertido() {
        Estado result = converter.convert("1");
        assertEquals(new Long(1), result.getCodigo());
    }

    @Test
    public void testeMetodoConvertQuandoCodigoVazioDeveRetornarNull() {
        Estado result = converter.convert("");
        assertNull(result);
    }
}