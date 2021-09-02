package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class MovimientosExcelRequest implements Serializable{

	@JsonProperty("idUsuario")
	private String idUsuario;

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("usuario")
	private String usuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("tamanoPagina")
	private int tamanoPagina;
	
	@JsonProperty("numeroPagina")
	private int numeroPagina;
	
	
	
	@JsonProperty("filtros")
	private Movimiento filtros;
	
	
	public MovimientosExcelRequest() {
		super();
		this.filtros = new Movimiento(); 
				
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
