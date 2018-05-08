package com.brewer.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class EstadoTest {

    @Test
    public void testeMetodosTransientAoBanco() {
        Estado estado = new Estado();
        estado.setNome("nomeTeste");
        estado.setSigla("NT");

        assertEquals("nomeTeste", estado.getNome());
        assertEquals("NT", estado.getSigla());
        assertNotNull(estado.hashCode());
    }

}