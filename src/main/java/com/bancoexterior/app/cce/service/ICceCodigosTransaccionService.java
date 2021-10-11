package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.dto.CceCodigosTransaccionDto;
import com.bancoexterior.app.cce.model.CceCodigosTransaccion;

public interface ICceCodigosTransaccionService {
	
	public List<CceCodigosTransaccion> findAll();

	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo();
	
	public CceCodigosTransaccionDto findById(String codTransaccion);
	
	public CceCodigosTransaccionDto codigoTransaccionById(String codTransaccion);
	
	public CceCodigosTransaccionDto save(CceCodigosTransaccionDto cceCodigosTransaccionDto);
	
	public void crearCodigoTransaccion(String codTransaccion, String descripcion, int idTipo);
	
	public void actualizarCodigoTransaccion(String codTransaccion, String descripcion, int idTipo);
	
	public void eliminarCodigoTransaccion(String codTransaccion);
	
	public int countCodigoTransaccionByTipo(int idTipo);
}
