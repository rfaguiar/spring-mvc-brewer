package com.brewer.model;

import com.brewer.builder.UsuarioGrupoBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class UsuarioGrupoTest {

    @Test
    public void testeMetodoBuilder() {
        UsuarioGrupo usuarioGrupo = UsuarioGrupoBuilder.criarUsuarioGrupoId();
        UsuarioGrupo usuarioGrupo1 = UsuarioGrupoBuilder.criarUsuarioGrupoId();

        assertNotNull(usuarioGrupo.getId());
        assertNotNull(usuarioGrupo.getId().getGrupo());
        assertNotNull(usuarioGrupo.getId().getUsuario());
        assertTrue(usuarioGrupo.equals(usuarioGrupo1));
        assertEquals(usuarioGrupo.toString(), usuarioGrupo1.toString());
    }

}