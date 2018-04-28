package com.brewer.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.brewer.model.validation.ClienteGroupSequenceProvider;
import com.brewer.model.validation.group.CnpjGroup;
import com.brewer.model.validation.group.CpfGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGroupSequenceProvider.class)
public class Cliente extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotNull(message = "Tipo pessoa é obrigatório")
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@CPF(groups = CpfGroup.class)
	@CNPJ(groups = CnpjGroup.class)
	@Column(name = "cpf_cnpj")
	private String cpfOuCnpj;

	private String telefone;

	@Email(message = "E-mail inválido")
	private String email;

	@JsonIgnore
	@Embedded
	private Endereco endereco;
	
	@PrePersist
	@PreUpdate
	private void preInsertPreUpdate(){
		//remove os pontos traços e barras do cpf e cnpj
		this.cpfOuCnpj = TipoPessoa.removerFormatacao(this.cpfOuCnpj);
	}
	
	@PostLoad
	private void postLoad(){
		this.cpfOuCnpj = this.tipoPessoa.formatar(this.cpfOuCnpj);
	}
	
	public String getCpfOuCnpjSemFormatacao(){
		return TipoPessoa.removerFormatacao(this.cpfOuCnpj);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Cliente)) return false;
		if (!super.equals(o)) return false;
		Cliente cliente = (Cliente) o;
		return Objects.equals(nome, cliente.nome) &&
				tipoPessoa == cliente.tipoPessoa &&
				Objects.equals(cpfOuCnpj, cliente.cpfOuCnpj) &&
				Objects.equals(telefone, cliente.telefone) &&
				Objects.equals(email, cliente.email);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), nome, tipoPessoa, cpfOuCnpj, telefone, email);
	}

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cpfOuCnpj='" + cpfOuCnpj + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
