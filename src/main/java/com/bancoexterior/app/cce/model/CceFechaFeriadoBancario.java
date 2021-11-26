package com.bancoexterior.app.cce.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"fecha_feriado_bancario\"", schema = "\"cce\"")
public class CceFechaFeriadoBancario {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "usuario", nullable = false)
	private String usuario;
	
	@Column(name = "fecha_feriado", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaFeriado;
}
