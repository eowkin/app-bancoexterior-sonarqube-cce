package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.Comision;
import com.bancoexterior.app.cce.model.Datos;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class CuentasConsultaResponse implements Serializable{
	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	
	@JsonProperty("datos")
	private Datos datos;
	
	@JsonProperty("comision")
	private Comision comision;
	
	public CuentasConsultaResponse() {
		this.resultado = new Resultado();
		this.datos = new Datos();
		this.comision = new Comision();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
