package com.bancoexterior.app.cce.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceTransaccion;

@Repository
public interface ICceTransaccionRepository extends JpaRepository<CceTransaccion, String>{

	public static final String BANCODESTINOCONDICION = "banco_destino = (case when ?2 = '' then banco_destino else ?2 end) and ";
	
	public static final String NUMEROIDENTIFICACIONCONDICION = "numero_identificacion = (case when ?3 = '' then numero_identificacion else ?3 end) and ";
	
	public static final String SELECTCAMPOS = "SELECT endtoend_id, referencia, cod_transaccion, cuenta_origen, tipo_identificacion, "
			+ "numero_identificacion, monto, moneda, telefono_origen, alias_origen, concepto, canal, fecha_modificacion, banco_origen, alias_destino, "
			+ "cuenta_destino, telefono_destino, banco_destino, reverso, tipo_transaccion, nombre_banco_destino, estadobcv, rsnbcv, "
			+ "tipo_identificacion_destino, numero_identificacion_destino, beneficiario_origen, beneficiario_destino, corte_liquidacion, fecha_liquida_bcv ";
	
	public static final String FROMTABLA = "FROM cce.cce_transaccion ";
	
	public static final String WHERE = "where ";
	
	public static final String SELECTCOUNT = "SELECT  count(";
	
	public static final String BANCODESTINOUNO = "banco_destino = (case when ?1 = '' then banco_destino else ?1 end) and ";
	
	public static final String NUMEROIDENTIFICACIONDOS = "numero_identificacion = (case when ?2 = '' then numero_identificacion else ?2 end) and ";
	
	public static final String FECHASMODIFICACION = "fecha_modificacion between to_timestamp(?3, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS')";
	
	public static final String WHERECREDITOINMEDIATO = "(cod_transaccion = '5723' or cod_transaccion = '9733' or cod_transaccion = '9740' "
			+ "or cod_transaccion = '9741' or cod_transaccion = '5760' or cod_transaccion = '5759') and "
			+ BANCODESTINOUNO
			+ NUMEROIDENTIFICACIONDOS
			+ FECHASMODIFICACION;
	
	public static final String WHERECREDITOINMEDIATORECIBIDO = "(cod_transaccion = '5724' or cod_transaccion = '9734' or cod_transaccion = '9742' or cod_transaccion = '9743') and "
			+ BANCODESTINOUNO
			+ NUMEROIDENTIFICACIONDOS
			+ FECHASMODIFICACION;
	
	public static final String WHERELBTRENVIADO = "(cod_transaccion = '5727' or cod_transaccion = '5714' or cod_transaccion = '5718' or cod_transaccion = '9024' "
			+ "or cod_transaccion = '9025' or cod_transaccion = '5745' or cod_transaccion = '5746' or cod_transaccion = '5783' "
			+ "or cod_transaccion = '5793') and "
			+ BANCODESTINOUNO
			+ NUMEROIDENTIFICACIONDOS
			+ FECHASMODIFICACION;
	
	public static final String WHERELBTRRECIBIDO = "(cod_transaccion = '5728' or cod_transaccion = '9738') and "
			+ BANCODESTINOUNO
			+ NUMEROIDENTIFICACIONDOS
			+ FECHASMODIFICACION;
	
	public static final String SELECTCCETRANSACCIONFECHAS =SELECTCAMPOS
			+ FROMTABLA
			+ "where cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end) and "
			+ BANCODESTINOCONDICION
			+ NUMEROIDENTIFICACIONCONDICION
			+ "fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	public static final String SELECTCCETRANSACCIONFECHASCOUNT ="SELECT  count(cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end) and "
			+ BANCODESTINOCONDICION
			+ NUMEROIDENTIFICACIONCONDICION
			+ "fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')) "
			+ FROMTABLA
			+ "where cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end) and "
			+ BANCODESTINOCONDICION
			+ NUMEROIDENTIFICACIONCONDICION
			+ "fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	
	
	public static final String SELECTCCETRANSACCIONFECHASPRUEBA =SELECTCAMPOS
			+ FROMTABLA
			+ "where fecha_modificacion between to_timestamp(:fechaDesde, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(:fechaHasta, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	
	@Query(value = SELECTCCETRANSACCIONFECHAS,
		    countQuery = SELECTCCETRANSACCIONFECHASCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	@Query(value = SELECTCCETRANSACCIONFECHAS, nativeQuery = true)
	public List<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
																String fechaDesde, String fechaHasta);
	
	@Query(value = SELECTCCETRANSACCIONFECHASPRUEBA, nativeQuery = true)
	public List<CceTransaccion> consultaMovimientosConFechasPrueba(String fechaDesde, String fechaHasta);
	
	
	public List<CceTransaccion> findByCodTransaccion(String codTransaccion);
	
	
	public static final String SELECTCCETRANSACCIONFECHASCREDITOINMEDIATO =SELECTCAMPOS
			+ FROMTABLA
			+ WHERE+WHERECREDITOINMEDIATO;
	
	public static final String SELECTCCETRANSACCIONFECHASCREDITOINMEDIATOCOUNT =SELECTCOUNT+WHERECREDITOINMEDIATO+") "
			+ FROMTABLA
			+ WHERE+WHERECREDITOINMEDIATO;
	
	
	@Query(value = SELECTCCETRANSACCIONFECHASCREDITOINMEDIATO,
		    countQuery = SELECTCCETRANSACCIONFECHASCREDITOINMEDIATOCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosCreditoInmediato(String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	public static final String SELECTCCETRANSACCIONFECHASCREDITOINMEDIATORECIBIDO =SELECTCAMPOS
			+ FROMTABLA
			+ WHERE+WHERECREDITOINMEDIATORECIBIDO;
	
	public static final String SELECTCCETRANSACCIONFECHASCREDITOINMEDIATORECIBIDOCOUNT =SELECTCOUNT+WHERECREDITOINMEDIATORECIBIDO+") "
			+ FROMTABLA
			+ WHERE+WHERECREDITOINMEDIATORECIBIDO;
	
	@Query(value = SELECTCCETRANSACCIONFECHASCREDITOINMEDIATORECIBIDO,
		    countQuery = SELECTCCETRANSACCIONFECHASCREDITOINMEDIATORECIBIDOCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosCreditoInmediatoRecibido(String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	
	public static final String SELECTCCETRANSACCIONFECHASLBTRENVIADO =SELECTCAMPOS
			+ FROMTABLA
			+ WHERE+WHERELBTRENVIADO;
	
	public static final String SELECTCCETRANSACCIONFECHASLBTRENVIADOCOUNT =SELECTCOUNT+WHERELBTRENVIADO+") "
			+ FROMTABLA
			+ WHERE+WHERELBTRENVIADO;
	
	@Query(value = SELECTCCETRANSACCIONFECHASLBTRENVIADO,
		    countQuery = SELECTCCETRANSACCIONFECHASLBTRENVIADOCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosLbtrEnviado(String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	public static final String SELECTCCETRANSACCIONFECHASLBTRRECIBIDO =SELECTCAMPOS
			+ FROMTABLA
			+ WHERE+WHERELBTRRECIBIDO;
	
	public static final String SELECTCCETRANSACCIONFECHASLBTRRECIBIDOCOUNT =SELECTCOUNT+WHERELBTRRECIBIDO+") "
			+ FROMTABLA
			+ WHERE+WHERELBTRRECIBIDO;
	
	@Query(value = SELECTCCETRANSACCIONFECHASLBTRRECIBIDO,
		    countQuery = SELECTCCETRANSACCIONFECHASLBTRRECIBIDOCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosLbtrRecibido(String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
}
