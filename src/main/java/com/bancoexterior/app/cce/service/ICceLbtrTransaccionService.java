package com.bancoexterior.app.cce.service;

import org.springframework.data.domain.Page;

import com.bancoexterior.app.cce.dto.CceLbtrTransaccionDto;
import com.bancoexterior.app.cce.model.CceLbtrTransaccion;

public interface ICceLbtrTransaccionService {

	
	public CceLbtrTransaccionDto save(CceLbtrTransaccionDto cceLbtrTransaccionDto);
	
	public Page<CceLbtrTransaccion> consultaLbtrTransacciones(int page, String userName);
	
	public CceLbtrTransaccionDto findById(int id);
	
	public Page<CceLbtrTransaccion> consultaLbtrTransaccionesAprobarFechas(String bancoReceptor, String fechaHoy, int page);
	
	public int countCodigoLbtrTransaccionByTipo(String codTransaccion);
}
