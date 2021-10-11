package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceTipoTransaccionDto;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;
import com.bancoexterior.app.cce.repository.ICceTipoTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceTipoTransaccionServiceImpl implements ICceTipoTransaccionService{

	@Autowired
	private ICceTipoTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceTipoTransaccion> findAll() {
		return repo.findAll();
	}

	@Override
	public CceTipoTransaccionDto findById(int id) {
		
		CceTipoTransaccion cceTipoTransaccion = repo.findById(id).orElse(null);
		
		if(cceTipoTransaccion != null) {
			return mapper.map(cceTipoTransaccion, CceTipoTransaccionDto.class);
		}
		return null;
	}

	@Override
	public CceTipoTransaccionDto save(CceTipoTransaccionDto cceTipoTransaccionDto) {
		CceTipoTransaccion cceTipoTransaccion = mapper.map(cceTipoTransaccionDto, CceTipoTransaccion.class);
		CceTipoTransaccion cceTipoTransaccionSave = repo.save(cceTipoTransaccion);
		return mapper.map(cceTipoTransaccionSave, CceTipoTransaccionDto.class); 
		
	}

	@Override
	public void deleteById(int id) {
		repo.deleteById(id);
	}

}
