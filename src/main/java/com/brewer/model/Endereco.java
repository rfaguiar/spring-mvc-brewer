package com.brewer.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Embeddable
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String logradouro;
	private String numero;
	private String complemento;
	private String cep;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_cidade")
	private Cidade cidade;
	
	@Transient
	private Estado estado;
	
	
	public String getNomeCidadeSiglaEstado(){
		if(this.cidade != null){
			return this.cidade.getNome() + "/" + this.cidade.getEstado().getSigla();
		}
		return null;
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public Cidade getCidade() {
		return cidade;
	}
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
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
		if (!(o instanceof Endereco)) return false;
		Endereco endereco = (Endereco) o;
		return Objects.equals(logradouro, endereco.logradouro) &&
				Objects.equals(numero, endereco.numero) &&
				Objects.equals(complemento, endereco.complemento) &&
				Objects.equals(cep, endereco.cep);
	}

	@Override
	public int hashCode() {

		return Objects.hash(logradouro, numero, complemento, cep);
	}

	@Override
	public String toString() {
		return "Endereco{" +
				"logradouro='" + logradouro + '\'' +
				", numero='" + numero + '\'' +
				", complemento='" + complemento + '\'' +
				", cep='" + cep + '\'' +
				'}';
	}
}
