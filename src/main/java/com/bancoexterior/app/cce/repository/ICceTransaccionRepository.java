package com.bancoexterior.app.cce.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bancoexterior.app.cce.model.CceTransaccion;

@Repository
public interface ICceTransaccionRepository extends JpaRepository<CceTransaccion, String>{

	
	public static final String SELECTCCETRANSACCIONBYENDTOENDID="SELECT endtoend_id, referencia, t.cod_transaccion, cuenta_origen, tipo_identificacion, "
			+ "numero_identificacion, monto, moneda, telefono_origen, alias_origen, concepto, canal, fecha_modificacion, banco_origen, alias_destino, cuenta_destino, telefono_destino, banco_destino, reverso, tipo_transaccion, nombre_banco_destino, estadobcv, rsnbcv, "
			+ "tipo_identificacion_destino, numero_identificacion_destino, beneficiario_origen, beneficiario_destino, corte_liquidacion, fecha_liquida_bcv, "
			+ "t3.envio "
			+ "FROM cce.cce_transaccion t, cce.codigos_transaccion t2, cce.tipo_transaccion t3 "
			+ "where "
			+ "t.endtoend_id = ?1 and "
			+ "t.cod_transaccion = t2.cod_transaccion and "
			+ "t2.id_tipo = t3.id";
	
	
	@Query(value = SELECTCCETRANSACCIONBYENDTOENDID,
		    nativeQuery = true)
	public CceTransaccion getTransaccionByEndToEndId(String endtoendId);
	
	
	

	
	
	public static final String SELECTCCETRANSACCIONFECHASFINAL ="SELECT endtoend_id, referencia, t.cod_transaccion, cuenta_origen, tipo_identificacion, "
			+ "numero_identificacion, monto, moneda, telefono_origen, alias_origen, concepto, canal, fecha_modificacion, banco_origen, alias_destino, cuenta_destino, "
			+ "telefono_destino, banco_destino, reverso, tipo_transaccion, nombre_banco_destino, estadobcv, rsnbcv, "
			+ "tipo_identificacion_destino, numero_identificacion_destino, beneficiario_origen, beneficiario_destino, corte_liquidacion, fecha_liquida_bcv, t3.envio "
			+ "FROM cce.cce_transaccion t, cce.codigos_transaccion t2, cce.tipo_transaccion t3 "
			+ "where t.cod_transaccion = t2.cod_transaccion and t2.id_tipo = t3.id and t2.id_tipo = (case when ?1 = 0 then t2.id_tipo else ?1 end) and "
			+ "t.banco_destino = (case when ?2 = '' then t.banco_destino else ?2 end) and t.numero_identificacion = (case when ?3 = '' then t.numero_identificacion else ?3 end) and "
			+ "t.fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
			
	
	
	
	public static final String SELECTCCETRANSACCIONFECHASCOUNTFINAL ="SELECT  count(t.cod_transaccion = t2.cod_transaccion and "
			+ "t2.id_tipo = t3.id and "
			+ "t2.id_tipo = (case when ?1 = 0 then t2.id_tipo else ?1 end) and "
			+ "t.banco_destino = (case when ?2 = '' then t.banco_destino else ?2 end) and "
			+ "t.numero_identificacion = (case when ?3 = '' then t.numero_identificacion else ?3 end) and "
			+ "t.fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')) "
			+ "FROM cce.cce_transaccion t, cce.codigos_transaccion t2, cce.tipo_transaccion t3 "
			+ "where t.cod_transaccion = t2.cod_transaccion and "
			+ "t2.id_tipo = t3.id and "
			+ "t2.id_tipo = (case when ?1 = 0 then t2.id_tipo else ?1 end) and "
			+ "t.banco_destino = (case when ?2 = '' then t.banco_destino else ?2 end) and "
			+ "t.numero_identificacion = (case when ?3 = '' then t.numero_identificacion else ?3 end) and "
			+ "t.fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	@Query(value = SELECTCCETRANSACCIONFECHASFINAL,
		    countQuery = SELECTCCETRANSACCIONFECHASCOUNTFINAL,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosFinal(int tipoTransaccion, String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	
	public static final String COUNTCCETRANSACCIONBYCODTRANSACCION = "SELECT count(*) as cantidad "
			+ "FROM cce.cce_transaccion "
			+ "where "
			+ "cod_transaccion = ?1";
	
	@Query(value = COUNTCCETRANSACCIONBYCODTRANSACCION,
		    nativeQuery = true)
	public int countTransaccionByCodTransaccion(String codTransaccion);
}
