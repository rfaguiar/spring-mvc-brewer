package com.brewer.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "venda")
@DynamicUpdate
public class Venda extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@Column(name = "valor_frete")
	private BigDecimal valorFrete;

	@Column(name = "valor_desconto")
	private BigDecimal valorDesconto;

	@Column(name = "valor_total")
	private BigDecimal valorTotal = BigDecimal.ZERO;

	private String observacao;

	@Column(name = "data_hora_entrega")
	private LocalDateTime dataHoraEntrega;

	@ManyToOne
	@JoinColumn(name = "codigo_cliente")
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(name = "codigo_usuario")
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	private StatusVenda status = StatusVenda.ORCAMENTO;

	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemVenda> itens = new ArrayList<>();

	@Transient
	private String uuid;

	@Transient
	private LocalDate dataEntrega;

	@Transient
	private LocalTime horarioEntrega;

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public LocalDateTime getDataHoraEntrega() {
		return dataHoraEntrega;
	}

	public void setDataHoraEntrega(LocalDateTime dataHoraEntrega) {
		this.dataHoraEntrega = dataHoraEntrega;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public LocalTime getHorarioEntrega() {
		return horarioEntrega;
	}

	public void setHorarioEntrega(LocalTime horarioEntrega) {
		this.horarioEntrega = horarioEntrega;
	}

	public boolean isNova() {
		return getCodigo() == null;
	}
	
	public void adicionarItens(List<ItemVenda> itens) {
		this.itens = itens;
		this.itens.forEach(i -> i.setVenda(this));
	}
	
	public BigDecimal getValorTotalItens(){
		return getItens().stream()
				.map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void calcularValorTotal() {
		
		this.valorTotal = calcularValorTotal(getValorTotalItens(), getValorFrete(), getValorDesconto());
	}
	
	public Long getDiasCriacao(){
		LocalDate inicio = dataCriacao != null ? dataCriacao.toLocalDate() : LocalDate.now();
		return ChronoUnit.DAYS.between(inicio, LocalDate.now());
	}
	
	public boolean isSalvarProibido(){
		return !isSalvarPermitido();
	}
	
	public boolean isSalvarPermitido(){
		return !StatusVenda.CANCELADA.equals(status);
	}
	
	private BigDecimal calcularValorTotal(BigDecimal valorTotalItens, BigDecimal valorFrete, BigDecimal valorDesconto) {
        return valorTotalItens
				.add(Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO));
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venda)) return false;
        if (!super.equals(o)) return false;
        Venda venda = (Venda) o;
        return Objects.equals(dataCriacao, venda.dataCriacao) &&
                Objects.equals(valorFrete, venda.valorFrete) &&
                Objects.equals(valorDesconto, venda.valorDesconto) &&
                Objects.equals(valorTotal, venda.valorTotal) &&
                Objects.equals(observacao, venda.observacao) &&
                Objects.equals(dataHoraEntrega, venda.dataHoraEntrega) &&
                status == venda.status &&
                Objects.equals(uuid, venda.uuid) &&
                Objects.equals(dataEntrega, venda.dataEntrega) &&
                Objects.equals(horarioEntrega, venda.horarioEntrega);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), dataCriacao, valorFrete, valorDesconto, valorTotal, observacao, dataHoraEntrega, status, uuid, dataEntrega, horarioEntrega);
    }

    @Override
    public String toString() {
        return "Venda{" +
                "dataCriacao=" + dataCriacao +
                ", valorFrete=" + valorFrete +
                ", valorDesconto=" + valorDesconto +
                ", valorTotal=" + valorTotal +
                ", observacao='" + observacao + '\'' +
                ", dataHoraEntrega=" + dataHoraEntrega +
                ", status=" + status +
                ", uuid='" + uuid + '\'' +
                ", dataEntrega=" + dataEntrega +
                ", horarioEntrega=" + horarioEntrega +
                '}';
    }
}
