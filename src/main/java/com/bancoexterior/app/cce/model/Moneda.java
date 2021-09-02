package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Moneda implements Serializable{

	@JsonProperty("Ccy")
	@SerializedName("Ccy")
	private String ccy;
	
	@JsonProperty("Amt")
	@SerializedName("Amt")
	private double amt;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
