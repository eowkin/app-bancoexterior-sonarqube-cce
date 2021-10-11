package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceHistorialMontoMaximoAproAuto;
import com.bancoexterior.app.cce.repository.ICceHistorialMontoMaximoAproAutoRepository;

@Service
public class CceHistorialMontoMaximoAproAutoServiceImpl implements ICceHistorialMontoMaximoAproAutoService{

	@Autowired
	private ICceHistorialMontoMaximoAproAutoRepository repo;
	
	@Override
	public List<CceHistorialMontoMaximoAproAuto> findAll() {
		return repo.findAll(Sort.by(Sort.Direction.DESC, "fechaModificacion"));
	}

}
