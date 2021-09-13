package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import com.bancoexterior.app.cce.dto.CceMontoComisionCastigoDto;
import com.bancoexterior.app.cce.model.CceMontoComisionCastigo;

public interface ICceMontoComisionCastigoService {

	public List<CceMontoComisionCastigo> findAll();
	
	public CceMontoComisionCastigoDto findById(int id);
	
	public void updateMontoCastigoTipoTransaccion(BigDecimal monto, String usuario, String tipoCliente, int id);
}
