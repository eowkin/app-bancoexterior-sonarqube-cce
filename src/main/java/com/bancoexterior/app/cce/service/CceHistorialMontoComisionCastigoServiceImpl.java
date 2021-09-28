package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceHistorialMontoComisionCastigo;
import com.bancoexterior.app.cce.repository.ICceHistorialMontoComisionCastigoRepository;

@Service
public class CceHistorialMontoComisionCastigoServiceImpl implements ICceHistorialMontoComisionCastigoService{

	@Autowired
	ICceHistorialMontoComisionCastigoRepository repo;
	
	@Override
	public List<CceHistorialMontoComisionCastigo> findAll() {
		return repo.findAll(Sort.by(Sort.Direction.DESC, "fechaModificacion"));
	}

}
