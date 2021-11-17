package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class MontoMinComisionConsultaRequest implements Serializable{

	
	@JsonProperty("idSesion")
	@SerializedName("idSesion")
	private String idSesionMR;
	
	@JsonProperty("idUsuario")
	@SerializedName("idUsuario")
	private String idUsuarioMR;
	
	@JsonProperty("codUsuario")
	@SerializedName("codUsuario")
	private String codUsuarioMR;
	
	@JsonProperty("canal")
	@SerializedName("canal")
	private String canalCM;
	
	@JsonProperty("comision")
	@SerializedName("comision")
	private Filtros comision;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public MontoMinComisionConsultaRequest() {
		this.comision = new Filtros();
	}
}
