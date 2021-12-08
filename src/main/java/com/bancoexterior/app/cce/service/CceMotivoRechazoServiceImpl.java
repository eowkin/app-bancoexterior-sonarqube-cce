package com.bancoexterior.app.cce.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMotivoRechazoDto;
import com.bancoexterior.app.cce.model.CceMotivoRechazo;
import com.bancoexterior.app.cce.repository.ICceMotivoRechazoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMotivoRechazoServiceImpl implements ICceMotivoRechazoService{
	
	private static final Logger LOGGER = LogManager.getLogger(CceMotivoRechazoServiceImpl.class);
	
	private static final String CCEMOTIVORECHAZOSERVICEFINDALLI = "[==== INICIO CceMotivoRechazo findAll - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICEFINDALLF = "[==== FIN CceMotivoRechazo findAll - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICESAVEI = "[==== INICIO CceMotivoRechazo save - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICESAVEF = "[==== FIN CceMotivoRechazo save - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICEFINDBYIDI = "[==== INICIO CceMotivoRechazo findById - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICEFINDBYIDF = "[==== FIN CceMotivoRechazo findById - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICEUPDATEMOTIVORECHAZOI = "[==== INICIO CceMotivoRechazo updateMotivoRechazo - Service ====]";
	
	private static final String CCEMOTIVORECHAZOSERVICEUPDATEMOTIVORECHAZOF = "[==== FIN CceMotivoRechazo updateMotivoRechazo - Service ====]";

	@Autowired
	private ICceMotivoRechazoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMotivoRechazo> findAll() {
		LOGGER.info(CCEMOTIVORECHAZOSERVICEFINDALLI);
		LOGGER.info(CCEMOTIVORECHAZOSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	public CceMotivoRechazoDto save(CceMotivoRechazoDto cceMotivoRechazoDto) {
		LOGGER.info(CCEMOTIVORECHAZOSERVICESAVEI);
		CceMotivoRechazo cceMotivoRechazo = mapper.map(cceMotivoRechazoDto, CceMotivoRechazo.class);
		CceMotivoRechazo cceMotivoRechazoSave = repo.save(cceMotivoRechazo);
		LOGGER.info(CCEMOTIVORECHAZOSERVICESAVEF);
		return mapper.map(cceMotivoRechazoSave, CceMotivoRechazoDto.class);
	
	}

	@Override
	public CceMotivoRechazoDto findById(String codigo) {
		LOGGER.info(CCEMOTIVORECHAZOSERVICEFINDBYIDI);
		CceMotivoRechazo cceMotivoRechazo = repo.findById(codigo).orElse(null);
		if(cceMotivoRechazo != null) {
			LOGGER.info(CCEMOTIVORECHAZOSERVICEFINDBYIDF);
			return mapper.map(cceMotivoRechazo, CceMotivoRechazoDto.class);
		}
		LOGGER.info(CCEMOTIVORECHAZOSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public void updateMotivoRechazo(String descripcion, String aplicaComisionCastigo, String codigo) {
		LOGGER.info(CCEMOTIVORECHAZOSERVICEUPDATEMOTIVORECHAZOI);
		LOGGER.info(CCEMOTIVORECHAZOSERVICEUPDATEMOTIVORECHAZOF);
		repo.updateMotivoRechazo(descripcion, aplicaComisionCastigo, codigo);	
	}
}
