package com.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.brewer.Constantes;
import com.brewer.repository.listener.CervejaEntityListener;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import com.brewer.validation.SKU;

@EntityListeners(CervejaEntityListener.class)
@Entity
@Table(name = "cerveja")
public class Cerveja extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@SKU
	@NotBlank
	private String sku;
	
	@NotBlank(message = "Nome é obrigatorio")
	private String nome;
	
	@NotBlank(message = "A descrição é obrigatória")
	@Size(min = 1, max = 500, message = "O tamanho da descrição deve estar entre 1 e 500")
	private String descricao;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin("0.01")
	@DecimalMax(value = "9999999.99", message = "O valor da cerveja deve ser meno que R$9.999.999,99")
	private BigDecimal valor;
	
	@NotNull(message = "O teor alcóolico é obrigatório")
	@DecimalMax(value = "100.0", message = "O valor do teor alcóolico deve ser menor que 100")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAlcoolico;
	
	@NotNull(message = "A comissão é obrigatória")
	@DecimalMax(value = "100.0", message = "A comissão deve ser igual ou menor que 100")
	private BigDecimal comissao;
	
	@NotNull(message = "A quantidade em estoque é obrigatória")
	@Max(value = 9999, message = "A quantidade em estoque deve ser menor que 9.999")
	@Column(name = "quantidade_estoque")
	private Integer quantidadeEstoque;
	
	@NotNull(message = "A origem é obrigatória")
	@Enumerated(EnumType.STRING)
	private Origem origem;
	
	@NotNull(message = "O sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;
	
	@NotNull(message = "O estilo é obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_estilo")
	private Estilo estilo;
	
	private String foto;
	
	@Column(name = "content_type")
	private String contentType;
	
	@Transient
	private boolean novaFoto;

    @Transient
    private String urlFoto;

    @Transient
    private String urlThumbnailFoto;
	
	@PrePersist
	@PreUpdate
	private void prePersisteUpdate(){
		sku = sku.toUpperCase();
	}
	
	public boolean isNova(){
		return getCodigo() == null;
	}
	
	public boolean temFoto(){
		return !StringUtils.isEmpty(this.foto);
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlThumbnailFoto() {
        return urlThumbnailFoto;
    }

    public void setUrlThumbnailFoto(String urlThumbnailFoto) {
        this.urlThumbnailFoto = urlThumbnailFoto;
    }

    public String getFotoOuMock() {
		return !StringUtils.isEmpty(foto) ? foto : Constantes.IMAGEN_CERVEJA_MOCK;
	}

	public boolean isNovaFoto() {
		return novaFoto;
	}

	public void setNovaFoto(boolean novaFoto) {
		this.novaFoto = novaFoto;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cerveja)) return false;
        if (!super.equals(o)) return false;
        Cerveja cerveja = (Cerveja) o;
        return novaFoto == cerveja.novaFoto &&
                Objects.equals(sku, cerveja.sku) &&
                Objects.equals(nome, cerveja.nome) &&
                Objects.equals(descricao, cerveja.descricao) &&
                Objects.equals(valor, cerveja.valor) &&
                Objects.equals(teorAlcoolico, cerveja.teorAlcoolico) &&
                Objects.equals(comissao, cerveja.comissao) &&
                Objects.equals(quantidadeEstoque, cerveja.quantidadeEstoque) &&
                Objects.equals(foto, cerveja.foto) &&
                Objects.equals(contentType, cerveja.contentType) &&
                Objects.equals(urlFoto, cerveja.urlFoto) &&
                Objects.equals(urlThumbnailFoto, cerveja.urlThumbnailFoto);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), sku, nome, descricao, valor, teorAlcoolico, comissao, quantidadeEstoque, foto, contentType, novaFoto, urlFoto, urlThumbnailFoto);
    }

    @Override
    public String toString() {
        return "Cerveja{" +
                "sku='" + sku + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", teorAlcoolico=" + teorAlcoolico +
                ", comissao=" + comissao +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", foto='" + foto + '\'' +
                ", contentType='" + contentType + '\'' +
                ", novaFoto=" + novaFoto +
                ", urlFoto='" + urlFoto + '\'' +
                ", urlThumbnailFoto='" + urlThumbnailFoto + '\'' +
                '}';
    }
}
