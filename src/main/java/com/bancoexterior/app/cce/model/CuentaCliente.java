package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class CuentaCliente implements Serializable{

	@JsonProperty("numero")
	private String numero;
	
	@JsonProperty("tipo")
	private String tipo;
	
	@JsonProperty("codigoProducto")
	private String codigoProducto;
	
	@JsonProperty("nombreProducto")
	private String nombreProducto;
	
	@JsonProperty("estatus")
	private String estatus;
	
	@JsonProperty("moneda")
	private String moneda;
	
	@JsonProperty("saldo")
	private BigDecimal monto;
	
	@JsonProperty("signo")
	private String signo;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
