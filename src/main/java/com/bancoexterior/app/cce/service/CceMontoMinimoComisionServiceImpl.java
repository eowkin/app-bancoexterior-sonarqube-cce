package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoMinimoComisionDto;
import com.bancoexterior.app.cce.model.CceMontoMinimoComision;
import com.bancoexterior.app.cce.repository.ICceMontoMinimoComisionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoMinimoComisionServiceImpl implements ICceMontoMinimoComisionService{

	private static final Logger LOGGER = LogManager.getLogger(CceMontoMinimoComisionServiceImpl.class);
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEFINDALLI = "[==== INICIO CceMontoMinimoComision findAll - Service ====]";
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEFINDALLF = "[==== FIN CceMontoMinimoComision findAll - Service ====]";
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEFINDBYIDI = "[==== INICIO CceMontoMinimoComision findById - Service ====]";
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEFINDBYIDF = "[==== FIN CceMontoMinimoComision findById - Service ====]";
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEUPDATEMONTOMINIMOCOMISIONI = "[==== INICIO CceMontoMinimoComision updateMontoMinimoComision - Service ====]";
	
	private static final String CCEMONTOMINIMOCOMISIONSERVICEUPDATEMONTOMINIMOCOMISIONF = "[==== FIN CceMontoMinimoComision updateMontoMinimoComision - Service ====]";
	
	
	
	@Autowired 
	private ICceMontoMinimoComisionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMontoMinimoComision> findAll() {
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEFINDALLI);
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	public CceMontoMinimoComisionDto findById(int id) {
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEFINDBYIDI);
		CceMontoMinimoComision cceMontoMinimoComision = repo.findById(id).orElse(null);
		
		if(cceMontoMinimoComision != null) {
			LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEFINDBYIDF);
			return mapper.map(cceMontoMinimoComision, CceMontoMinimoComisionDto.class);
		}
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public void updateMontoMinimoComision(BigDecimal monto, String usuario, String tipoCliente, int id) {
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEUPDATEMONTOMINIMOCOMISIONI);
		LOGGER.info(CCEMONTOMINIMOCOMISIONSERVICEUPDATEMONTOMINIMOCOMISIONF);
		repo.updateMontoMinimoComision(monto, usuario, tipoCliente, id);
		
	}

}
