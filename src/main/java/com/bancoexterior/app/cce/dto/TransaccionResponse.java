package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.DatosTransaccion;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class TransaccionResponse implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("datosTransaccion")
	private DatosTransaccion datosTransaccion;
	
	public TransaccionResponse() {
		this.resultado = new Resultado();
		this.datosTransaccion = new DatosTransaccion();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
