package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoMaximoAproAutoDto;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.repository.ICceMontoMaximoAproAutoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoMaximoAproAutoServiceImpl implements ICceMontoMaximoAproAutoService{

	@Autowired
	private ICceMontoMaximoAproAutoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public CceMontoMaximoAproAuto buscarMontoMaximoAproAutoActual() {
		return repo.getMontoMaximoAproAutoActual();
	}

	@Override
	public List<CceMontoMaximoAproAuto> findAll() {
		return repo.findAll();
	}

	@Override
	public CceMontoMaximoAproAutoDto findById(int id) {
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = repo.findById(id).orElse(null);
		if(cceMontoMaximoAproAuto != null) {
			return mapper.map(cceMontoMaximoAproAuto, CceMontoMaximoAproAutoDto.class);
		}
		
		return null;
	}

	@Override
	public void updateMontoMaximoAproAuto(BigDecimal monto, String usuario, int id) {
		repo.updateMontoMaximoAproAuto(monto, usuario, id);
		
	}

	
}
