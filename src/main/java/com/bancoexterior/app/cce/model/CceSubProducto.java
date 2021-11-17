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
@Table(name = "\"sub_producto\"", schema = "\"cce\"")
public class CceSubProducto {

	
	@Column(name = "cod_producto")
	private String codProducto;
	@Id
	@Column(name = "cod_subproducto")
	private String codSubProducto;
	
	@Column(name = "nombre_subproducto")
	private String nombreSubProducto;
	
	@Column(name = "status_subproducto")
	private String statusSubProducto;
}
