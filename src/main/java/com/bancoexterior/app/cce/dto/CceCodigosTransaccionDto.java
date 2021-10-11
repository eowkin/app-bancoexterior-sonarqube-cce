package com.bancoexterior.app.cce.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CceCodigosTransaccionDto {

	private String codTransaccion;
	
	private String descripcion;
	
	private int idTipo;
	
}
