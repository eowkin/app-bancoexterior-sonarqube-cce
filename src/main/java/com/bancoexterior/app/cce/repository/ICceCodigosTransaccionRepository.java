package com.bancoexterior.app.cce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.model.CceCodigosTransaccion;


@Repository
public interface ICceCodigosTransaccionRepository extends JpaRepository<CceCodigosTransaccion, String>{

	public static final String SELECTCCECODIGOSTRANSACCIONANDTIPOTRANSACCION="SELECT cod_transaccion, t.descripcion,"
			+ " id_tipo, t2.id, t2.descripcion as nombretipo "
			+ "FROM cce.codigos_transaccion t,"
			+ " cce.tipo_transaccion t2 "
			+ "where t.id_tipo = t2.id";
	
	@Query(value = SELECTCCECODIGOSTRANSACCIONANDTIPOTRANSACCION,
		    nativeQuery = true)
	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo();
	
	public static final String SELECTCCECODIGOSTRANSACCIONPORTIPOTRANSACCION="SELECT cod_transaccion, t.descripcion, id_tipo, t2.id, t2.descripcion as nombretipo "
			+ "FROM cce.codigos_transaccion t, cce.tipo_transaccion t2 "
			+ "where t.id_tipo = t2.id and t.id_tipo = ?1";
	
	@Query(value = SELECTCCECODIGOSTRANSACCIONPORTIPOTRANSACCION,
		    nativeQuery = true)
	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo(int idTipo);
	
	
	public static final String SELECTCCECODIGOSTRANSACCIONBYID="SELECT cod_transaccion, t.descripcion, id_tipo, t2.id, t2.descripcion as nombretipo "
			+ "FROM cce.codigos_transaccion t, cce.tipo_transaccion t2 "
			+ "where t.id_tipo = t2.id and t.cod_transaccion=?1";
	
	@Query(value = SELECTCCECODIGOSTRANSACCIONBYID,
		    nativeQuery = true)
	public CceCodigosTransaccion codigoTransaccionById(String codTransaccion);
	
	
	public static final String INSERTCCECODIGOSTRANSACCION = "INSERT INTO cce.codigos_transaccion "
			+ "(cod_transaccion, descripcion, id_tipo) "
			+ "VALUES(?1, ?2, ?3)";
	
	@Modifying
	@Transactional
	@Query(value = INSERTCCECODIGOSTRANSACCION, nativeQuery = true)
	public void crearCodigoTransaccion(String codTransaccion, String descripcion, int idTipo);
	
	
	public static final String UPDATECCECODIGOSTRANSACCION = "UPDATE cce.codigos_transaccion "
			+ "SET descripcion=?2, id_tipo=?3 "
			+ "WHERE cod_transaccion=?1";
	
	@Modifying
	@Transactional
	@Query(value = UPDATECCECODIGOSTRANSACCION, nativeQuery = true)
	public void actualizarCodigoTransaccion(String codTransaccion, String descripcion, int idTipo);
			
	
	public static final String DELETECCECODIGOSTRANSACCION = "DELETE FROM cce.codigos_transaccion "
			+ "WHERE cod_transaccion=?1";
	
	@Modifying
	@Transactional
	@Query(value = DELETECCECODIGOSTRANSACCION, nativeQuery = true)
	public void eliminarCodigoTransaccion(String codTransaccion);
	
	public static final String COUNTCCECODIGOSTRANSACCION = "SELECT count(*) as cantidad "
			+ "FROM cce.codigos_transaccion "
			+ "where "
			+ "id_tipo = ?1";
	
	@Query(value = COUNTCCECODIGOSTRANSACCION,
		    nativeQuery = true)
	public int countCodigoTransaccionByTipo(int idTipo);
	
}
