package com.bancoexterior.app.cce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceLbtrTransaccion;

@Repository
public interface ICceLbtrTransaccionRepository extends JpaRepository<CceLbtrTransaccion, Integer>{

	public static final String SELECTCCELBTRTRANSACCION ="SELECT referencia, cod_transaccion, canal, monto, "
			+ "moneda, producto, sub_producto, banco_emisor, cuenta_emisor, cuenta_unica_emisor, banco_receptor, cuenta_receptor, cuenta_unica_receptor, iden_emisor, nom_emisor, iden_receptor, nom_receptor, status, fecha_valor, fecha_actualizacion, usuario, id, descripcion "
			+ "FROM cce.lbtr_transacciones where status <> 'E'";
	
	public static final String SELECTCCELBTRTRANSACCIONCOUNT ="SELECT count(*) as cantidad "
			+ "FROM cce.lbtr_transacciones where status <> 'E'";
	
	
	@Query(value = SELECTCCELBTRTRANSACCION,
		    countQuery = SELECTCCELBTRTRANSACCIONCOUNT,
		    nativeQuery = true)
	
	public Page<CceLbtrTransaccion> consultaLbtrTransacciones(Pageable pageable);
	
	
	public static final String SELECTCCELBTRTRANSACCIONAPROBARFECHAS ="SELECT referencia, "
			+ "cod_transaccion, canal, monto, moneda, producto, sub_producto, banco_emisor, cuenta_emisor, cuenta_unica_emisor, banco_receptor, cuenta_receptor, cuenta_unica_receptor, iden_emisor, nom_emisor, iden_receptor, nom_receptor, status, fecha_valor, fecha_actualizacion, usuario, id, descripcion "
			+ "FROM cce.lbtr_transacciones "
			+ "where banco_receptor = (case when ?1 = '' then banco_receptor else ?1 end) and "
			+ "status = 'I' and "
			+ "fecha_valor between to_timestamp(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	public static final String SELECTCCELBTRTRANSACCIONAPROBARFECHASCOUNT ="SELECT count(*) as cantidad "
			+ "FROM cce.lbtr_transacciones "
			+ "where banco_receptor = (case when ?1 = '' then banco_receptor else ?1 end) and "
			+ "status = 'I' and "
			+ "fecha_valor between to_timestamp(?2, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS')";
	
	@Query(value = SELECTCCELBTRTRANSACCIONAPROBARFECHAS,
		    countQuery = SELECTCCELBTRTRANSACCIONAPROBARFECHASCOUNT,
		    nativeQuery = true)
	public Page<CceLbtrTransaccion> consultaLbtrTransaccionesAprobarFechas(String bancoReceptor, String fechaDesde, String fechaHasta, Pageable pageable);
	
	
	public static final String COUNTCCECODIGOSLBTRTRANSACCION = "SELECT count(*) as cantidad "
			+ "FROM cce.lbtr_transacciones "
			+ "where "
			+ "cod_transaccion = ?1";
	
	@Query(value = COUNTCCECODIGOSLBTRTRANSACCION,
		    nativeQuery = true)
	public int countCodigoLbtrTransaccionByTipo(String codTransaccion);
}
