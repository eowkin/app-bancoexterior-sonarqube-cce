package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceCuentasUnicasBcv;
import com.bancoexterior.app.cce.repository.ICceCuentasUnicasBcvRepository;

@Service
public class CceCuentasUnicasBcvServiceImpl implements ICceCuentasUnicasBcvService{

	@Autowired
	ICceCuentasUnicasBcvRepository repo;
	
	@Override
	public List<CceCuentasUnicasBcv> findAll() {
		return repo.findAll();
	}

	@Override
	public CceCuentasUnicasBcv findById(String codigoBic) {
		return repo.findById(codigoBic).orElse(null);
	}

	@Override
	public CceCuentasUnicasBcv consultaCuentasUnicasBcvByCodigoBic(String codigoBic) {
		return repo.consultaCuentasUnicasBcvByCodigoBic(codigoBic);
	}

}
