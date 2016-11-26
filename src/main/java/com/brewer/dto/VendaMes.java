package com.brewer.dto;

public class VendaMes {

	private String mes;
	private Integer total;
	
	public VendaMes() {}

	public VendaMes(String mes, Integer total) {
		super();
		this.mes = mes;
		this.total = total;
	}
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
