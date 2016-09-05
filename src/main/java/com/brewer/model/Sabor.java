package com.brewer.model;

public enum Sabor {

	ADOCICADA("Adocidada"),
	AMARGA("Amarga"),
	FORTE("Forte"),
	FRUTADA("Frutada"),
	SUAVE("Suave");
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	Sabor(String descricao){
		this.descricao = descricao;
	}
}
