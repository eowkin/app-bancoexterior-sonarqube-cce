package com.bancoexterior.app.cce.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.model.CceMontoMinimoComision;


@Repository
public interface ICceMontoMinimoComisionRepository extends JpaRepository<CceMontoMinimoComision, Integer>{

	public static final String UPDATECCEMONTOMINIMOCOMISION ="UPDATE cce.monto_minimo_comision "
			+ "SET monto=?1, usuario=?2, tipo_cliente=?3 "
			+ "WHERE id=?4";
	@Modifying
	@Transactional
	@Query(value = UPDATECCEMONTOMINIMOCOMISION, nativeQuery = true)
	public void updateMontoMinimoComision(BigDecimal monto, String usuario, String tipoCliente, int id);
}
