package com.bancoexterior.app.cce.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceMontoComisionCastigoDto {
	
	private Integer id;
	
	private BigDecimal monto;
	
	private String usuario;
	
	private String tipoCliente;
	
	@Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?$", message = "El monto debe tener fotmato 0.00")
	private String montoString;
}
