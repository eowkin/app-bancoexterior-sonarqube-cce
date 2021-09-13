package com.bancoexterior.app.cce.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.model.CceMontoComisionCastigo;

@Repository
public interface ICceMontoComisionCastigoRepository extends JpaRepository<CceMontoComisionCastigo, Integer>{

	public static final String SELECTUPDATECOMISIONCASTIGO = "UPDATE cce.monto_comision_castigo "
			+ "SET monto=?1, usuario=?2, tipo_cliente=?3 "
			+ "WHERE id=?4";
	
	@Modifying
	@Transactional
	@Query(value = SELECTUPDATECOMISIONCASTIGO, nativeQuery = true)
	public void updateMontoCastigoTipoTransaccion(BigDecimal monto, String usuario, String tipoCliente, int id);
	
}
