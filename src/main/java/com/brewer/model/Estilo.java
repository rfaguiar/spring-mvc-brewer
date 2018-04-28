package com.brewer.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "estilo")
public class Estilo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;


	@NotBlank(message = "Nome é obrigatorio")
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
		if (!(o instanceof Estilo)) return false;
		if (!super.equals(o)) return false;
		Estilo estilo = (Estilo) o;
		return Objects.equals(nome, estilo.nome);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), nome);
	}

    @Override
    public String toString() {
        return "Estilo{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
