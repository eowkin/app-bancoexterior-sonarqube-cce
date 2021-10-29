package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.model.CceCuentasUnicasBcv;

public interface ICceCuentasUnicasBcvService {

	public List<CceCuentasUnicasBcv> findAll();
	
	public CceCuentasUnicasBcv findById(String codigoBic);
	
	public CceCuentasUnicasBcv consultaCuentasUnicasBcvByCodigoBic(String codigoBic);
}
