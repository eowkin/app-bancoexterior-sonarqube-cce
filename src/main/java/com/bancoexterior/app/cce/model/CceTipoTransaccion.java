package com.bancoexterior.app.cce.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"tipo_transaccion\"", schema = "\"cce\"")
public class CceTipoTransaccion {
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;

	@Column(name = "envio")
	private boolean envio;
	
}
