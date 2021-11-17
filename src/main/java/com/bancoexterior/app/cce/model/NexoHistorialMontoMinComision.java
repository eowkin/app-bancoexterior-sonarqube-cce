package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class NexoHistorialMontoMinComision implements Serializable{

	

	private int id;
	
	private String usuario;
	
	private String descripcion;
	
	private BigDecimal monto;
	
	private String fecha;
	
	private String montoString;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
