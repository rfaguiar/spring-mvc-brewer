package com.brewer.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "estilo")
public class Estilo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;


	@NotBlank(message = "Nome é obrigatorio")
	private String nome;
	
	@OneToMany(mappedBy = "estilo")
	private List<Cerveja> cervejas;
	
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
		return Objects.equals(nome, estilo.nome) &&
				Objects.equals(cervejas, estilo.cervejas);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), nome, cervejas);
	}

	@Override
	public String toString() {
		return "Estilo{" +
				"nome='" + nome + '\'' +
				", cervejas=" + cervejas +
				'}';
	}
}
