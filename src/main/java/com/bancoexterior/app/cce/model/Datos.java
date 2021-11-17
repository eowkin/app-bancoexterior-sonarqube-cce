package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Datos implements Serializable{
	
	

	@JsonProperty("total_cuentas")
	private Integer totalCuentas;

	@JsonProperty("cuentas")
	private List<CuentaCliente> cuentas;
	
	
	
	public Datos() {
		this.cuentas = new ArrayList<>();
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
