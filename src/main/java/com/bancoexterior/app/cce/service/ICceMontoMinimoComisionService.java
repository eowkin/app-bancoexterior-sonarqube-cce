package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import com.bancoexterior.app.cce.dto.CceMontoMinimoComisionDto;
import com.bancoexterior.app.cce.model.CceMontoMinimoComision;

public interface ICceMontoMinimoComisionService {

	public List<CceMontoMinimoComision> findAll();
	
	public CceMontoMinimoComisionDto findById(int id);
	
	public void updateMontoMinimoComision(BigDecimal monto, String usuario, String tipoCliente, int id);
	
}
