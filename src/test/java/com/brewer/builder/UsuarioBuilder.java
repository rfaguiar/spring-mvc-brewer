package com.brewer.builder;

import com.brewer.model.Grupo;
import com.brewer.model.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class UsuarioBuilder {
    private Usuario usuario;

    private UsuarioBuilder() {
        usuario = new Usuario();
    }

    public static UsuarioBuilder get() {
        return new UsuarioBuilder();
    }

    public static Usuario criarUsuario() {
        return UsuarioBuilder.get()
                .nome("usuarioTeste")
                .senha("senhaTeste")
                .confirmacaoSenha("senhaTeste")
                .email("emailteste@teste.com")
                .ativo(true)
                .grupos(GrupoBuilder.criarListaGrupos())
                .dataNascimento(LocalDate.now())
                .build();
    }

    public static List<Usuario> criarListaUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        lista.add(UsuarioBuilder.criarUsuario());
        return lista;
    }

    public UsuarioBuilder codigo(Long codigo) {
        usuario.setCodigo(codigo);
        return this;
    }

    public UsuarioBuilder nome(String nome) {
        usuario.setNome(nome);
        return this;
    }

    public UsuarioBuilder email(String email) {
        usuario.setEmail(email);
        return this;
    }

    public UsuarioBuilder senha(String senha) {
        usuario.setSenha(senha);
        return this;
    }

    public UsuarioBuilder confirmacaoSenha(String confirmacaoSenha) {
        usuario.setConfirmacaoSenha(confirmacaoSenha);
        return this;
    }

    public UsuarioBuilder ativo(Boolean ativo) {
        usuario.setAtivo(ativo);
        return this;
    }

    public UsuarioBuilder grupos(List<Grupo> grupos) {
        usuario.setGrupos(grupos);
        return this;
    }

    public UsuarioBuilder dataNascimento(LocalDate dataNascimento) {
        usuario.setDataNascimento(dataNascimento);
        return this;
    }

    public UsuarioBuilder but() {
        return get().codigo(usuario.getCodigo()).nome(usuario.getNome()).email(usuario.getEmail()).senha(usuario.getSenha()).confirmacaoSenha(usuario.getConfirmacaoSenha()).ativo(usuario.getAtivo()).grupos(usuario.getGrupos()).dataNascimento(usuario.getDataNascimento());
    }

    public Usuario build() {
        return usuario;
    }
}
