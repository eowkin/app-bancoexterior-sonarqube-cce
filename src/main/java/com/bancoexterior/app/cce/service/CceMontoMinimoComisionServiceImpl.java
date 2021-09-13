package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoMinimoComisionDto;
import com.bancoexterior.app.cce.model.CceMontoMinimoComision;
import com.bancoexterior.app.cce.repository.ICceMontoMinimoComisionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoMinimoComisionServiceImpl implements ICceMontoMinimoComisionService{

	@Autowired 
	private ICceMontoMinimoComisionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMontoMinimoComision> findAll() {
		return repo.findAll();
	}

	@Override
	public CceMontoMinimoComisionDto findById(int id) {
		
		CceMontoMinimoComision cceMontoMinimoComision = repo.findById(id).orElse(null);
		
		if(cceMontoMinimoComision != null) {
			return mapper.map(cceMontoMinimoComision, CceMontoMinimoComisionDto.class);
		}
		
		return null;
	}

	@Override
	public void updateMontoMinimoComision(BigDecimal monto, String usuario, String tipoCliente, int id) {
		repo.updateMontoMinimoComision(monto, usuario, tipoCliente, id);
		
	}

}
