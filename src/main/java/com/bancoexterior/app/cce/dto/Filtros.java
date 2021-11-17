package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filtros implements Serializable{

	
	@JsonProperty("id")
	private Integer id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
