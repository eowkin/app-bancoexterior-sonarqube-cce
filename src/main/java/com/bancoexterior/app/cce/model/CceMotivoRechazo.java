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
@Table(name = "\"motivo_rechazo\"", schema = "\"cce\"")
public class CceMotivoRechazo {

	@Id
	@Column(name = "codigo", nullable = false)
	private String codigo;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "aplica_comision_castigo", nullable = false)
	private String aplicaComisionCastigo;
}
