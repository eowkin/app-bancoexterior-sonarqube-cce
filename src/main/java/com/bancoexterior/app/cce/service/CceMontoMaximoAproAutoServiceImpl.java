package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoMaximoAproAutoDto;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.repository.ICceMontoMaximoAproAutoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoMaximoAproAutoServiceImpl implements ICceMontoMaximoAproAutoService{

	private static final Logger LOGGER = LogManager.getLogger(CceMontoMaximoAproAutoServiceImpl.class);
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEFINDALLI = "[==== INICIO CceMontoMaximoAproAuto findAll - Service ====]";
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEFINDALLF = "[==== FIN CceMontoMaximoAproAuto findAll - Service ====]";
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEFINDBYIDI = "[==== INICIO CceMontoMaximoAproAuto findById - Service ====]";
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEFINDBYIDF = "[==== FIN CceMontoMaximoAproAuto findById - Service ====]";
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEUPDATEMONTOMAXIMOAPROAUTOI = "[==== INICIO CceMontoMaximoAproAuto updateMontoMaximoAproAuto - Service ====]";
	
	private static final String CCEMONTOMAXIMOAPROAUTOSERVICEUPDATEMONTOMAXIMOAPROAUTOF = "[==== FIN CceMontoMaximoAproAuto updateMontoMaximoAproAuto - Service ====]";
	
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
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEFINDALLI);
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	public CceMontoMaximoAproAutoDto findById(int id) {
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEFINDBYIDI);
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = repo.findById(id).orElse(null);
		if(cceMontoMaximoAproAuto != null) {
			LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEFINDBYIDF);
			return mapper.map(cceMontoMaximoAproAuto, CceMontoMaximoAproAutoDto.class);
		}
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public void updateMontoMaximoAproAuto(BigDecimal monto, String usuario, int id) {
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEUPDATEMONTOMAXIMOAPROAUTOI);
		LOGGER.info(CCEMONTOMAXIMOAPROAUTOSERVICEUPDATEMONTOMAXIMOAPROAUTOF);
		repo.updateMontoMaximoAproAuto(monto, usuario, id);
		
	}

	
}
