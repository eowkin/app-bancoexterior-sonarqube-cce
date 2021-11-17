package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class DatosTransaccion implements Serializable{

	
	@JsonProperty("codigoOperacion")
	private String codigoOperacion;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("cuentaDesde")
	private String cuentaDesde;
	
	@JsonProperty("referencia")
	private String referencia;
	
	@JsonProperty("montoTransaccion")
	private String montoTransaccion;
	
	@JsonProperty("codigoTransaccion")
	private String codigoTransaccion;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
