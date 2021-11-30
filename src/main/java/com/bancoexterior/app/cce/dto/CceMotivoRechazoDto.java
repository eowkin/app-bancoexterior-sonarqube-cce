package com.bancoexterior.app.cce.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceMotivoRechazoDto {

	@NotBlank(message = "Código no puede ser vacio")
	@Size(min = 5,  max = 5, message = "Código debe tener un tamaño de 5 caracteres")
	private String codigo;
	
	@NotBlank(message = "Descripción no puede ser vacio")
	@Size(max = 140, message = "Descripción no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	@NotBlank(message = "Aplica Comisión no puede ser vacio")
	@Size(min = 1,  max = 1, message = "Aplica Comisión debe tener un tamaño de 1 caracteres")
	private String aplicaComisionCastigo;
	
	
	
	
}
