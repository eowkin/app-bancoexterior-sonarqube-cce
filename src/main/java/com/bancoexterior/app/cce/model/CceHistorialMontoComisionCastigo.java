package com.bancoexterior.app.cce.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"historial_monto_comision_castigo\"", schema = "\"cce\"")
public class CceHistorialMontoComisionCastigo {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Transient
	private String montoString;
	
	@Column(name = "usuario", nullable = false)
	private String usuario;
	
	@Column(name = "fecha_modificacion", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	
	@Column(name = "monto", nullable = false)
	@Digits(integer=13, fraction=2)
	private BigDecimal monto;
	
	@Column(name = "tipo_cliente", nullable = false)
	private String tipoCliente;
	
	
	
	
	
}
