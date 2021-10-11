package com.bancoexterior.app.cce.model;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"codigos_transaccion\"", schema = "\"cce\"")
public class CceCodigosTransaccion{

	
	@Id
	@Column(name = "cod_transaccion", nullable = false)
	private String codTransaccion;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "id_tipo", nullable = false)
	private int idTipo;
	
	@AccessType(Type.FIELD)
	private int id;
	
	@AccessType(Type.FIELD)
	private String nombretipo;
	
	@Transient
	private CceTipoTransaccion cceTipoTransaccion;
	
	//@ManyToOne(fetch = FetchType.EAGER)
	//@JoinColumn(name = "id_tipo")
	//private CceTipoTransaccion tipoTransaccion;


	

	
	
	
	
	
}
