package com.bancoexterior.app.cce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceCuentasUnicasBcv;

@Repository
public interface ICceCuentasUnicasBcvRepository extends JpaRepository<CceCuentasUnicasBcv, String>{

	public static final String SELECTCCECUENTASUNICASBCVBYCODIGOBIC="SELECT cuenta, codigo_bic, nombre_banco, status "
			+ "FROM cce.cuentas_unicas_bcv "
			+ "where codigo_bic = ?1 and status = 'A'";
	
	@Query(value = SELECTCCECUENTASUNICASBCVBYCODIGOBIC,
		    nativeQuery = true)
	public CceCuentasUnicasBcv consultaCuentasUnicasBcvByCodigoBic(String codigoBic);
}
