package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Comision implements Serializable{

	

	private String moneda;
	
	private BigDecimal monto;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
