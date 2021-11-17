package com.bancoexterior.app.cce.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"lbtr_transacciones\"", schema = "\"cce\"")
public class CceLbtrTransaccion {

	
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "referencia", nullable = false)
	private String referencia;
	
	@Column(name = "cod_transaccion", nullable = false)
	private String codTransaccion;
	
	@Column(name = "canal", nullable = false)
	private String canal;
	
	@Column(name = "monto", nullable = false)
	private BigDecimal monto;
	
	@Column(name = "moneda", nullable = false)
	private String moneda;
	
	@Column(name = "producto", nullable = false)
	private String producto;
	
	@Column(name = "sub_producto", nullable = false)
	private String subProducto;
	
	@Column(name = "banco_emisor", nullable = false)
	private String bancoEmisor;
	
	@Column(name = "cuenta_emisor", nullable = false)
	private String cuentaEmisor;
	
	@Column(name = "cuenta_unica_emisor", nullable = false)
	private String cuentaUnicaEmisor;
	
	@Column(name = "banco_receptor", nullable = false)
	private String bancoReceptor;
	
	@Column(name = "cuenta_receptor", nullable = false)
	private String cuentaReceptor;
	
	@Column(name = "cuenta_unica_receptor", nullable = false)
	private String cuentaUnicaReceptor;
	
	@Column(name = "iden_emisor", nullable = false)
	private String idemEmisor;
	
	@Column(name = "nom_emisor", nullable = false)
	private String nomEmisor;
	
	@Column(name = "iden_receptor", nullable = false)
	private String idemReceptor;
	
	@Column(name = "nom_receptor", nullable = false)
	private String nomReceptor;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "fecha_valor", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaValor;
	
	@Column(name = "fecha_actualizacion", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaActualizacion;
	
	@Column(name = "usuario", nullable = false)
	private String usuario;
	
	@Column(name = "descripcion")
	private String descripcion;

	@Transient
	private String montoString;
	
	@PrePersist
	public void prePersist() {
		setFechaActualizacion(new Date());
	}
	
	@PreUpdate
	public void preUpdate() {
		setFechaActualizacion(new Date());
	}
}
