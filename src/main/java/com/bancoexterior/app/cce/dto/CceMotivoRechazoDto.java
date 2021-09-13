package com.bancoexterior.app.cce.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceMotivoRechazoDto {

	private String codigo;
	
	private String descripcion;
	
	private String aplicaComisionCastigo;
	
	
}
