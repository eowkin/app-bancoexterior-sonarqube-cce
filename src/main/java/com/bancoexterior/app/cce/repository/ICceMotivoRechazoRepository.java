package com.bancoexterior.app.cce.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.model.CceMotivoRechazo;


@Repository
public interface ICceMotivoRechazoRepository extends JpaRepository<CceMotivoRechazo, String>{

	public static final String UPDATEMOTIVORECHAZO = "UPDATE cce.motivo_rechazo "
			+ "SET descripcion=?1, aplica_comision_castigo=?2 "
			+ "WHERE codigo=?3";
	
	@Modifying
	@Transactional
	@Query(value = UPDATEMOTIVORECHAZO, nativeQuery = true)
	public void updateMotivoRechazo(String descripcion, String aplicaComisionCastigo, String codigo);
	
}
