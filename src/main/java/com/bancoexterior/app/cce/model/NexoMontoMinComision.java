package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NexoMontoMinComision implements Serializable{

	

	private int id;
	
	private BigDecimal monto;
	
	@NotBlank(message = "Descripcion no puede ser vacio")
	@Size(max = 140, message = "Descripcion no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	private String usuario;
	
	@Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?$", message = "El monto debe tener fotmato 0.00")
	private String montoString;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
