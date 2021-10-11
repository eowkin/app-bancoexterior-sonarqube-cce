package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.dto.CceTipoTransaccionDto;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;

public interface ICceTipoTransaccionService {

	public List<CceTipoTransaccion> findAll();
	
	public CceTipoTransaccionDto findById(int id);
	
	public CceTipoTransaccionDto save(CceTipoTransaccionDto cceTipoTransaccionDto);
	
	public void deleteById(int id);
}
