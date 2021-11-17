package com.bancoexterior.app.cce.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;


@Data
public class DatosMontoMinComision implements Serializable{

	private Integer id;
	
	private BigDecimal monto;
	
	private String descripcion;
	
	private String usuario;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
