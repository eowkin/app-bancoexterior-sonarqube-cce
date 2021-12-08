package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceMontoComisionCastigoDto;
import com.bancoexterior.app.cce.model.CceMontoComisionCastigo;
import com.bancoexterior.app.cce.repository.ICceMontoComisionCastigoRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceMontoComisionCastigoServiceImpl implements ICceMontoComisionCastigoService{

	private static final Logger LOGGER = LogManager.getLogger(CceMontoComisionCastigoServiceImpl.class);
	
	private static final String CCECOMISIONCASTIGOSERVICEFINDALLI = "[==== INICIO CceComisionCastigo findAll - Service ====]";
	
	private static final String CCECOMISIONCASTIGOSERVICEFINDALLF = "[==== FIN CceComisionCastigo findAll - Service ====]";
	
	private static final String CCECOMISIONCASTIGOSERVICEFINDBYIDI = "[==== INICIO CceComisionCastigo findById - Service ====]";
	
	private static final String CCECOMISIONCASTIGOSERVICEFINDBYIDF = "[==== FIN CceComisionCastigo findById - Service ====]";
	
	private static final String CCECOMISIONCASTIGOSERVICEUPDATEMONTOCASTIGOTIPOTRANSACCIONI = "[==== INICIO CceComisionCastigo updateMontoCastigoTipoTransaccion - Service ====]";
	
	private static final String CCECOMISIONCASTIGOSERVICEUPDATEMONTOCASTIGOTIPOTRANSACCIONF = "[==== FIN CceComisionCastigo updateMontoCastigoTipoTransaccion - Service ====]";
	
	
	@Autowired
	private ICceMontoComisionCastigoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceMontoComisionCastigo> findAll() {
		LOGGER.info(CCECOMISIONCASTIGOSERVICEFINDALLI);
		LOGGER.info(CCECOMISIONCASTIGOSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	public CceMontoComisionCastigoDto findById(int id) {
		LOGGER.info(CCECOMISIONCASTIGOSERVICEFINDBYIDI);
		CceMontoComisionCastigo cceMontoComisionCastigo = repo.findById(id).orElse(null);
		if(cceMontoComisionCastigo != null) {
			LOGGER.info(CCECOMISIONCASTIGOSERVICEFINDBYIDF);
			return mapper.map(cceMontoComisionCastigo, CceMontoComisionCastigoDto.class); 
		}
		LOGGER.info(CCECOMISIONCASTIGOSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public void updateMontoCastigoTipoTransaccion(BigDecimal monto, String usuario, String tipoCliente, int id) {
		LOGGER.info(CCECOMISIONCASTIGOSERVICEUPDATEMONTOCASTIGOTIPOTRANSACCIONI);
		LOGGER.info(CCECOMISIONCASTIGOSERVICEUPDATEMONTOCASTIGOTIPOTRANSACCIONF);
		repo.updateMontoCastigoTipoTransaccion(monto, usuario, tipoCliente, id);
	}

}
