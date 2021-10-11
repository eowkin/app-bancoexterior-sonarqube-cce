package com.bancoexterior.app.cce.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceTipoTransaccionDto {

	private Integer id;
	
	private String descripcion;
	
	private boolean envio;
}
