package com.bancoexterior.app.cce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceSubProducto;

@Repository
public interface ICceSubProductoRepository extends JpaRepository<CceSubProducto, String>{

	public static final String SELECTCCESUBPRODUCTO="SELECT cod_producto, cod_subproducto, nombre_subproducto, status_subproducto "
			+ "FROM cce.sub_producto "
			+ "where cod_producto = '801' and status_subproducto = 'A'";
	
	
	@Query(value = SELECTCCESUBPRODUCTO,
		    nativeQuery = true)
	public List<CceSubProducto> listaSubproductos();
	
	
	public static final String SELECTCCESUBPRODUCTOPORCODPRODUCTOANDPORCODSUBPRODUCTO="SELECT cod_producto, cod_subproducto, nombre_subproducto, status_subproducto "
			+ "FROM cce.sub_producto "
			+ "where cod_producto = ?1 and cod_subproducto= ?2 and status_subproducto = 'A'";
	
	@Query(value = SELECTCCESUBPRODUCTOPORCODPRODUCTOANDPORCODSUBPRODUCTO,
		    nativeQuery = true)
	public CceSubProducto buscarSubProductoPorCodProductoAndCodSubProducto(String codProducto, String codSubProducto);
}
