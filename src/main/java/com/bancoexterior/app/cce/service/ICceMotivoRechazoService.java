package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.dto.CceMotivoRechazoDto;
import com.bancoexterior.app.cce.model.CceMotivoRechazo;

public interface ICceMotivoRechazoService {

	public List<CceMotivoRechazo> findAll();
	
	public CceMotivoRechazoDto save(CceMotivoRechazoDto cceMotivoRechazoDto);
	
	public CceMotivoRechazoDto findById(String codigo);
	
	public void updateMotivoRechazo(String descripcion, String aplicaComisionCastigo, String codigo);
}
