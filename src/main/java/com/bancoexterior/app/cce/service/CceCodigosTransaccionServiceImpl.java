package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.dto.CceCodigosTransaccionDto;
import com.bancoexterior.app.cce.model.CceCodigosTransaccion;
import com.bancoexterior.app.cce.repository.ICceCodigosTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceCodigosTransaccionServiceImpl implements ICceCodigosTransaccionService{

	@Autowired
	private ICceCodigosTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceCodigosTransaccion> findAll() {
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public CceCodigosTransaccionDto findById(String codTransaccion) {
		
		CceCodigosTransaccion cceCodigosTransaccion = repo.findById(codTransaccion).orElse(null);
		
		if(cceCodigosTransaccion != null) {
			return mapper.map(cceCodigosTransaccion, CceCodigosTransaccionDto.class);
		}
		
		return null;
	}

	@Override
	public CceCodigosTransaccionDto save(CceCodigosTransaccionDto cceCodigosTransaccionDto) {
		CceCodigosTransaccion cceCodigosTransaccion = mapper.map(cceCodigosTransaccionDto, CceCodigosTransaccion.class);
		CceCodigosTransaccion cceCodigosTransaccionSave = repo.save(cceCodigosTransaccion);
		return mapper.map(cceCodigosTransaccionSave, CceCodigosTransaccionDto.class);
	}

	@Override
	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo() {
		return repo.consultarTodosCodigosConTipo();
	}

	@Override
	public CceCodigosTransaccionDto codigoTransaccionById(String codTransaccion) {
		CceCodigosTransaccion cceCodigosTransaccion = repo.codigoTransaccionById(codTransaccion);
		
		if(cceCodigosTransaccion != null) {
			return mapper.map(cceCodigosTransaccion, CceCodigosTransaccionDto.class);
		}
		
		return null;
	}

	@Override
	public void crearCodigoTransaccion(String codTransaccion, String descripcion, int idTipo) {
		repo.crearCodigoTransaccion(codTransaccion, descripcion, idTipo);
		
	}

	@Override
	public void actualizarCodigoTransaccion(String codTransaccion, String descripcion, int idTipo) {
		repo.actualizarCodigoTransaccion(codTransaccion, descripcion, idTipo);
	}

	@Override
	public void eliminarCodigoTransaccion(String codTransaccion) {
		repo.eliminarCodigoTransaccion(codTransaccion);
	}

	@Override
	public int countCodigoTransaccionByTipo(int idTipo) {
		return repo.countCodigoTransaccionByTipo(idTipo);
	}

}
