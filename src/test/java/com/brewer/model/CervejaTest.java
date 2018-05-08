package com.brewer.model;

import com.brewer.Constantes;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CervejaTest {

    @Test
    public void testeMetodoGetFotoOuMockQuandoFotoCervejaEstiverNuloOuVazioDeveRetornarImagenFotoMock() {
        Cerveja cerveja = new Cerveja();
        assertEquals(Constantes.IMAGEN_CERVEJA_MOCK, cerveja.getFotoOuMock());
    }

    @Test
    public void testeMetodosTransientAPersistencia() {
        Cerveja cerveja = new Cerveja();
        cerveja.setCodigo(new Long(1));
        cerveja.setFoto("fotoTeste");
        cerveja.setDescricao("descTeste");
        cerveja.setTeorAlcoolico(new BigDecimal(10));
        cerveja.setComissao(new BigDecimal(20));
        cerveja.setQuantidadeEstoque(new Integer(10));
        cerveja.setContentType("png");
        cerveja.setUrlFoto("urlFotoTeste");
        cerveja.setUrlThumbnailFoto("urlThumbNailTeste");
        cerveja.setNovaFoto(true);

        assertFalse(cerveja.isNova());
        assertTrue(cerveja.temFoto());
        assertEquals("fotoTeste", cerveja.getFoto());
        assertEquals("descTeste", cerveja.getDescricao());
        assertEquals("10", cerveja.getTeorAlcoolico().toString());
        assertEquals("20", cerveja.getComissao().toString());
        assertEquals("10", cerveja.getQuantidadeEstoque().toString());
        assertEquals("png", cerveja.getContentType());
        assertEquals("urlFotoTeste", cerveja.getUrlFoto());
        assertEquals("urlThumbNailTeste", cerveja.getUrlThumbnailFoto());
        assertTrue(cerveja.isNovaFoto());
    }
}