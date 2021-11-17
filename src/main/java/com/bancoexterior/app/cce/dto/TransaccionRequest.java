package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransaccionRequest implements Serializable{

	

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codigoOperacion")
	private String codigoOperacion;
	
	@JsonProperty("cuentaDesde")
	private String cuentaDesde;
	
	@JsonProperty("cuentaHasta")
	private String cuentaHasta;
	
	@JsonProperty("referencia")
	private String referencia;
	
	@JsonProperty("montoTransaccion")
	private String montoTransaccion;
	
	@JsonProperty("cedulaRif")
	private String cedulaRif;
	
	@JsonProperty("nombreBeneneficiario")
	private String nombreBeneneficiario;
	
	@JsonProperty("cedulaRifBeneficiario")
	private String cedulaRifBeneficiario;
	
	@JsonProperty("usuarioCanal")
	private String usuarioCanal;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("codigoTransaccion")
	private String codigoTransaccion;

	
	@JsonProperty("canal")
	private String canal;	
	
	@JsonProperty("nombreBancoBeneneficiario")
	private String nombreBancoBeneneficiario;
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
}
