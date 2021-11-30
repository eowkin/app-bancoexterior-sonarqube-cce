package com.bancoexterior.app.cce.dto;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceTipoTransaccionDto {

	private Integer id;
	
	@NotBlank(message = "Descripción no puede ser vacio")
	@Size(max = 140, message = "Descripción no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	private boolean envio;
}
