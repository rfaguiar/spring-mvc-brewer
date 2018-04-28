package com.brewer.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "permissao")
public class Permissao extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Permissao)) return false;
		if (!super.equals(o)) return false;
		Permissao permissao = (Permissao) o;
		return Objects.equals(nome, permissao.nome);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), nome);
	}

    @Override
    public String toString() {
        return "Permissao{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
