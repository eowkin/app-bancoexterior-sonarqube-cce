package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosPaginacion implements Serializable{

	@JsonProperty("totalRegistros")
	private Integer totalRegistros;
	
	@JsonProperty("paginaActual")
	private Integer paginaActual;
	
	@JsonProperty("totalPaginas")
	private Integer totalPaginas;
	
	@JsonProperty("tamanoPagina")
	private Integer tamanoPagina;
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
