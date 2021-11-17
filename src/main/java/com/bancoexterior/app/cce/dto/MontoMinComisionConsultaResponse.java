package com.bancoexterior.app.cce.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.cce.model.NexoMontoMinComision;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class MontoMinComisionConsultaResponse implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("comisiones")
	private List<NexoMontoMinComision> comisiones;
	
	
	public MontoMinComisionConsultaResponse() {
		this.resultado = new Resultado();
		this.comisiones = new ArrayList<>();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
