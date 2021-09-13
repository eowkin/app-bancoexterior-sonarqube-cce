package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import com.bancoexterior.app.cce.dto.CceMontoMaximoAproAutoDto;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;

public interface ICceMontoMaximoAproAutoService {

	public CceMontoMaximoAproAuto buscarMontoMaximoAproAutoActual();
	
	public List<CceMontoMaximoAproAuto> findAll();
	
	public CceMontoMaximoAproAutoDto findById(int id);
	
	public void updateMontoMaximoAproAuto(BigDecimal monto, String usuario, int id);
}
