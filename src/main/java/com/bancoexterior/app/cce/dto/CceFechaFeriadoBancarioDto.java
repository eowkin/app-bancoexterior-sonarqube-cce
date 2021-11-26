package com.bancoexterior.app.cce.dto;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceFechaFeriadoBancarioDto {

	private Integer id;
	
	@NotBlank(message = "Descripcion no puede ser vacio,")
	@Size(max = 140, message = "Descripcion no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	//@Future(message = "La fecha invalida, debe ser una fecha futura.")
	private Date fechaFeriado;
	
	private String fechaFeriadoString; 
	
	private String usuario; 
	
}
