package com.bancoexterior.app.cce.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CceCodigosTransaccionDto {

	@NotBlank(message = "Codigo no puede ser vacio")
	@Size(min = 4,  max = 6, message = "Codigo debe tener un tamaño entre 4 y 6 caracteres")
	private String codTransaccion;
	
	@NotBlank(message = "Descripcion no puede ser vacio")
	@Size(max = 140, message = "Descripcion no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	@NotNull(message = "El tipo no puede ser nulo.")
	private int idTipo;
	
}
