package com.bancoexterior.app.cce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"cuentas_unicas_bcv\"", schema = "\"cce\"")
public class CceCuentasUnicasBcv {

	@Id
	@Column(name = "codigo_bic")
	private String codigoBic;
	
	@Column(name = "cuenta")
	private String cuenta;
	
	@Column(name = "nombre_banco")
	private String nombreBanco;
	
	@Column(name = "status")
	private String status;
}
