package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceHistorialMontoMinimoComision;
import com.bancoexterior.app.cce.repository.ICceHistorialMontoMinimoComisionRepository;

@Service
public class CceHistorialMontoMinimoComisionServiceImpl implements ICceHistorialMontoMinimoComisionService{

	@Autowired
	private ICceHistorialMontoMinimoComisionRepository repo;
	
	@Override
	public List<CceHistorialMontoMinimoComision> findAll() {
		return repo.findAll();
	}

}
