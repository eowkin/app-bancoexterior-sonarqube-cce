package com.bancoexterior.app.cce.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;


@Repository
public interface ICceMontoMaximoAproAutoRepository extends JpaRepository<CceMontoMaximoAproAuto, Integer>{
	public static final String SELECTCCEMONTOMAXIMOAPROAUTO ="SELECT id, monto, usuario "
			+ "FROM cce.monto_maximo_apro_automatica\r\n"
			+ "WHERE id = (select MAX(id) FROM cce.monto_maximo_apro_automatica)";
	
	
	
	@Query(value = SELECTCCEMONTOMAXIMOAPROAUTO, nativeQuery = true)
	public CceMontoMaximoAproAuto getMontoMaximoAproAutoActual();
	
	public static final String UPDATECCEMONTOMAXIMOAPROAUTO ="UPDATE cce.monto_maximo_apro_automatica "
			+ "SET monto=?1, usuario=?2 "
			+ "WHERE id=?3";
	@Modifying
	@Transactional
	@Query(value = UPDATECCEMONTOMAXIMOAPROAUTO, nativeQuery = true)
	public void updateMontoMaximoAproAuto(BigDecimal monto, String usuario, int id);
	

}
