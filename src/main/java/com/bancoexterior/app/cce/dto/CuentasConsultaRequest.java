package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class CuentasConsultaRequest implements Serializable{

	
	@JsonProperty("id_cliente")
	@SerializedName("id_cliente")
	private String idCliente;
	
	@JsonProperty("id_usuario")
	@SerializedName("id_usuario")
	private String idUsuario;
	
	@JsonProperty("id_terminal")
	@SerializedName("id_terminal")
	private String idTerminal;
	
	@JsonProperty("ip_origen")
	@SerializedName("ip_origen")
	private String ipOrigen;
	
	@JsonProperty("id_consumidor")
	@SerializedName("id_consumidor")
	private String idConsumidor;
	
	
	@JsonProperty("id_canal")
	@SerializedName("id_canal")
	private String idCanal;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
