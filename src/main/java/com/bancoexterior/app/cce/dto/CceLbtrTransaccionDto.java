package com.bancoexterior.app.cce.dto;


import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CceLbtrTransaccionDto {

	private int id;
	
	private String referencia;
	
	//@NotBlank(message = "Cod. Transaccion no puede ser vacio")
	//@Size(max = 4, message = "Cod. Transaccion no puede exceder mas de 4 caracteres")
	private String codTransaccion;
	
	private String canal;
	
	private BigDecimal monto;
	
	@NotBlank(message = "Moneda no puede ser vacio")
	@Size(max = 3, message = "Moneda no puede exceder mas de 4 caracteres")
	private String moneda;
	
	private String producto;
	
	@NotBlank(message = "Sub Producto no puede ser vacio")
	@Size(max = 3, message = "Sub Producto no puede exceder mas de 4 caracteres")
	private String subProducto;
	
	@NotBlank(message = "Banco Emisor no puede ser vacio")
	@Size(max = 4, message = "Banco Emisor no puede exceder mas de 4 caracteres")
	private String bancoEmisor;
	
	@NotBlank(message = "Cuenta Ordenante no puede ser vacio")
	@Size(max = 20, message = "Cuenta Ordenante no puede exceder mas de 20 caracteres")
	@Pattern(regexp = "^[0-9]{20,20}$", message = "Cuenta Ordenante debe ser numérico y contener 20 digitos.")
	private String cuentaEmisor;
	
	@NotBlank(message = "Cuenta Bcv Emisor no puede ser vacio")
	@Size(max = 20, message = "Cuenta Bcv Emisor no puede exceder mas de 20 caracteres")
	@Pattern(regexp = "^[0-9]{20,20}$", message = "Cuenta Bcv Emisor debe ser numérico y contener 20 digitos.")
	private String cuentaUnicaEmisor;
	
	@NotBlank(message = "Banco Receptor no puede ser vacio")
	@Size(max = 4, message = "Banco Receptor no puede exceder mas de 4 caracteres")
	private String bancoReceptor;

	@Pattern(regexp = "^[0-9]{20,20}$", message = "Cuenta Beneficiario debe ser numérico y contener 20 digitos.")
	private String cuentaReceptor;
	
	@NotBlank(message = "Cuenta Bcv Receptor no puede ser vacio")
	@Size(max = 20, message = "Cuenta Bcv Receptor no puede exceder mas de 20 caracteres")
	private String cuentaUnicaReceptor;
	
	@NotBlank(message = "Id. Ordenante no puede ser vacio")
	@Size(max = 35, message = "Id. Ordenante no puede exceder mas de 35 caracteres")
	private String idemEmisor;
	
	@NotBlank(message = "Nombre Ordenante no puede ser vacio")
	@Size(max = 140, message = "Nombre Ordenante no puede exceder mas de 140 caracteres")
	private String nomEmisor;
	
	//@NotBlank(message = "Id. Beneficiario no puede ser vacio")
	//@Size(max = 35, message = "Id. Beneficiario no puede exceder mas de 35 caracteres")
	@Pattern(regexp = "[VEPJGC][0-9]{1,15}", message = "Id. Beneficiario debe comenzar por una letra [V,E,P,J,G,C] luego los caracteres numéricos correspondiente.")
	private String idemReceptor;
	
	@NotBlank(message = "Nombre Beneficiario no puede ser vacio")
	@Size(max = 140, message = "Nombre Beneficiario no puede exceder mas de 140 caracteres")
	private String nomReceptor;
	
	private String status;
	
	private Date fechaValor;
	
	private Date fechaActualizacion;
	
	private String usuarioCreador;
	
	private String usuarioAprobador;
	
	@Size(max = 140, message = "Descripción no puede exceder mas de 140 caracteres")
	private String descripcion;
	
	@Pattern(regexp = "^[0-9]+[.][0-9]{2,2}$", message = "El monto debe tener fotmato 0.00")
	//@Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?$", message = "El monto debe tener fotmato 0.00")
	private String montoString;
	
	private String fechaValorString;
	
	
	private String fechaHoy;
	
}