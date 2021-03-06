package com.bancoexterior.app.cce.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CceTransaccionAltoValorDto {

	private String bancoEmisor;
	
	private String bancoReceptor;
	
	private String cuentaBcvEmisor;
	
	private String cuentaBcvReceptor;
	
}
