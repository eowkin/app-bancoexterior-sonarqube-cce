package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.DatosAprobacion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class AprobacionRequest implements Serializable{
	
	

	@JsonProperty("idUsuario")
	@SerializedName("idUsuario")
	private String idUsuario;
	
	@JsonProperty("idSesion")
	@SerializedName("idSesion")
	private String idSesion;
	
	@JsonProperty("idCanal")
	@SerializedName("idCanal")
	private String idCanal;
	
	@JsonProperty("ip")
	@SerializedName("ip")
	private String ip;
	
	@JsonProperty("origen")
	@SerializedName("origen")
	private String origen;
	
	@JsonProperty("origen")
	private DatosAprobacion datosAprobacion;
	
	public AprobacionRequest() {
		super();
		this.datosAprobacion = new DatosAprobacion();
	}


	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
