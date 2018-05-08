package com.brewer.model;

import com.brewer.Constantes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CervejaTest {

    @Test
    public void testeMetodoGetFotoOuMockQuandoFotoCervejaEstiverNuloOuVazioDeveRetornarImagenFotoMock() {
        Cerveja cerveja = new Cerveja();
        assertEquals(Constantes.IMAGEN_CERVEJA_MOCK, cerveja.getFotoOuMock());
    }
}