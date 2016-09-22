package com.brewer.model;

import javax.persistence.Entity;
import javax.persistence.Table;

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

}
