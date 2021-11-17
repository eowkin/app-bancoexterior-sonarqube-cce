package com.bancoexterior.app.cce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MontoMinComisionRequest {

	@JsonProperty("idSesion")
	@SerializedName("idSesion")
	private String idSesionDR;
	
	
	@JsonProperty("idUsuario")
	@SerializedName("idUsuario")
	private String idUsuarioDR;
	
	
	@JsonProperty("codUsuario")
	@SerializedName("codUsuario")
	private String codUsuarioDR;
	
	
	@JsonProperty("canal")
	@SerializedName("canal")
	private String canalDM;
	
	
	@JsonProperty("comision")
	@SerializedName("comision")
	private DatosMontoMinComision comision;
}
