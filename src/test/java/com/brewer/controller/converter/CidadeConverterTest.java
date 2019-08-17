package com.brewer.controller.converter;

import com.brewer.model.Cidade;
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
public class CidadeConverterTest {

    private CidadeConverter converter;

    @Before
    public void metodoInicializaCenarioDeTeste() {
        MockitoAnnotations.initMocks(this);
        this.converter = new CidadeConverter();
    }

    @Test
    public void testeMetodoConvertDeveRetornarCidadeComIdConvertido() {
        Cidade result = converter.convert("1");
        assertEquals(new Long(1), result.getCodigo());
    }

    @Test
    public void testeMetodoConvertQuandoCodigoVazioDeveRetornarNull() {
        Cidade result = converter.convert("");
        assertNull(result);
    }
}