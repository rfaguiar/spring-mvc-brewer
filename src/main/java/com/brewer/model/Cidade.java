package com.brewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cidade")
public class Cidade extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_estado")
	@JsonIgnore
	private Estado estado;
	
	public boolean temEstado(){
		return estado != null;
	}	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Cidade)) return false;
		if (!super.equals(o)) return false;
		Cidade cidade = (Cidade) o;
		return Objects.equals(nome, cidade.nome);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), nome);
	}

    @Override
    public String toString() {
        return "Cidade{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
