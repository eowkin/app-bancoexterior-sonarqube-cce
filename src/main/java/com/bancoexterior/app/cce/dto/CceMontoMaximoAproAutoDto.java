package com.bancoexterior.app.cce.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CceMontoMaximoAproAutoDto {
	
	private Integer id;
	
	private BigDecimal monto;
	
	private String usuario;
}
