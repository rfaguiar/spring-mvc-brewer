package com.brewer.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "grupo")
public class Grupo extends BaseEntity{	
	
	private static final long serialVersionUID = 1L;
	
	private String nome;

	@ManyToMany
	@JoinTable(name = "grupo_permissao", joinColumns = @JoinColumn(name = "codigo_grupo"),
			inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))
	private List<Permissao> permissoes;
		
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo)) return false;
        if (!super.equals(o)) return false;
        Grupo grupo = (Grupo) o;
        return Objects.equals(nome, grupo.nome);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), nome);
    }

    @Override
    public String toString() {
        return "GRUPOS{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
