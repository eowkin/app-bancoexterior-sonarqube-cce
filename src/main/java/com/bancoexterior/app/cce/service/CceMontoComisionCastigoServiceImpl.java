package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoComisionCastigoDto;
import com.bancoexterior.app.cce.model.CceMontoComisionCastigo;
import com.bancoexterior.app.cce.repository.ICceMontoComisionCastigoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoComisionCastigoServiceImpl implements ICceMontoComisionCastigoService{

	@Autowired
	private ICceMontoComisionCastigoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMontoComisionCastigo> findAll() {
		return repo.findAll();
	}

	@Override
	public CceMontoComisionCastigoDto findById(int id) {
		CceMontoComisionCastigo cceMontoComisionCastigo = repo.findById(id).orElse(null);
		if(cceMontoComisionCastigo != null) {
			return mapper.map(cceMontoComisionCastigo, CceMontoComisionCastigoDto.class); 
		}
		return null;
	}

	@Override
	public void updateMontoCastigoTipoTransaccion(BigDecimal monto, String usuario, String tipoCliente, int id) {
		repo.updateMontoCastigoTipoTransaccion(monto, usuario, tipoCliente, id);
	}

}
