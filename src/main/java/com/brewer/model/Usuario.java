package com.brewer.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.brewer.validation.AtributoConfirmacao;

@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmacaoSenha", message = "Confirmação da senha não confere")
@Entity
@Table(name = "usuario")
@DynamicUpdate
public class Usuario extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
		
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	
	@NotBlank(message = "E-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;
		
	private String senha;
	
	@Transient
	private String confirmacaoSenha;
	
	private Boolean ativo;
	
	@Size(min = 1, message = "Selecione pelo menos um grupo")
	@ManyToMany
	@JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "codigo_usuario"),
			inverseJoinColumns = @JoinColumn(name = "codigo_grupo"))
	private List<Grupo> grupos;
	
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	@PreUpdate
	private void preUpdate(){
		this.confirmacaoSenha = senha;
	}
	
	public boolean isNovo(){
		return getCodigo() == null;
	}
		
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public List<Grupo> getGrupos() {
		return grupos;
	}
	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}
	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        if (!super.equals(o)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nome, usuario.nome) &&
                Objects.equals(email, usuario.email) &&
                Objects.equals(senha, usuario.senha) &&
                Objects.equals(confirmacaoSenha, usuario.confirmacaoSenha) &&
                Objects.equals(ativo, usuario.ativo) &&
                Objects.equals(dataNascimento, usuario.dataNascimento);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), nome, email, senha, confirmacaoSenha, ativo, dataNascimento);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", confirmacaoSenha='" + confirmacaoSenha + '\'' +
                ", ativo=" + ativo +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
