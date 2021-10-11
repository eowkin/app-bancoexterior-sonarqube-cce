package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMotivoRechazoDto;
import com.bancoexterior.app.cce.model.CceMotivoRechazo;
import com.bancoexterior.app.cce.repository.ICceMotivoRechazoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMotivoRechazoServiceImpl implements ICceMotivoRechazoService{

	@Autowired
	private ICceMotivoRechazoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMotivoRechazo> findAll() {
		return repo.findAll();
	}

	@Override
	public CceMotivoRechazoDto save(CceMotivoRechazoDto cceMotivoRechazoDto) {
		CceMotivoRechazo cceMotivoRechazo = mapper.map(cceMotivoRechazoDto, CceMotivoRechazo.class);
		CceMotivoRechazo cceMotivoRechazoSave = repo.save(cceMotivoRechazo);
		return mapper.map(cceMotivoRechazoSave, CceMotivoRechazoDto.class);
	
	}

	@Override
	public CceMotivoRechazoDto findById(String codigo) {
		CceMotivoRechazo cceMotivoRechazo = repo.findById(codigo).orElse(null);
		if(cceMotivoRechazo != null) {
			return mapper.map(cceMotivoRechazo, CceMotivoRechazoDto.class);
		}
		return null;
	}

	@Override
	public void updateMotivoRechazo(String descripcion, String aplicaComisionCastigo, String codigo) {
		repo.updateMotivoRechazo(descripcion, aplicaComisionCastigo, codigo);	
	}
}
