package com.brewer.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "estado")
public class Estado extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String sigla;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estado)) return false;
        if (!super.equals(o)) return false;
        Estado estado = (Estado) o;
        return Objects.equals(nome, estado.nome) &&
                Objects.equals(sigla, estado.sigla);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), nome, sigla);
    }

    @Override
    public String toString() {
        return "Estado{" +
                "nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                '}';
    }
}
