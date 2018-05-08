package com.brewer.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UsuarioTest {

    @Test
    public void testeMetodosTransientAoBanco() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setAtivo(true);
        LocalDate dataNascimento = LocalDate.now();
        usuario.setDataNascimento(dataNascimento);


        assertTrue(usuario.isNovo());
        assertTrue(usuario.getAtivo());
        assertEquals(dataNascimento.toString(), usuario.getDataNascimento().toString());

    }
}