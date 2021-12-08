package com.bancoexterior.app.cce.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceTipoTransaccionDto;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;
import com.bancoexterior.app.cce.repository.ICceTipoTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceTipoTransaccionServiceImpl implements ICceTipoTransaccionService{
	
	private static final Logger LOGGER = LogManager.getLogger(CceTipoTransaccionServiceImpl.class);
	
	private static final String CCETIPOTRANSACCIONSERVICEFINDALLI = "[==== INICIO CceTipoTransaccion findAll - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICEFINDALLF = "[==== FIN CceTipoTransaccion findAll - Service ====]";

	private static final String CCETIPOTRANSACCIONSERVICEFINDBYIDI = "[==== INICIO CceTipoTransaccion findById - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICEFINDBYIDF = "[==== FIN CceTipoTransaccion findById - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICESAVEI = "[==== INICIO CceTipoTransaccion save - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICESAVEF = "[==== FIN CceTipoTransaccion save - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICEDELETEBYIDI = "[==== INICIO CceTipoTransaccion deleteById - Service ====]";
	
	private static final String CCETIPOTRANSACCIONSERVICEDELETEBYIDF = "[==== FIN CceTipoTransaccion deleteById - Service ====]";
	
	
	
	@Autowired
	private ICceTipoTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<CceTipoTransaccion> findAll() {
		LOGGER.info(CCETIPOTRANSACCIONSERVICEFINDALLI);
		LOGGER.info(CCETIPOTRANSACCIONSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	public CceTipoTransaccionDto findById(int id) {
		LOGGER.info(CCETIPOTRANSACCIONSERVICEFINDBYIDI);
		CceTipoTransaccion cceTipoTransaccion = repo.findById(id).orElse(null);
		
		if(cceTipoTransaccion != null) {
			LOGGER.info(CCETIPOTRANSACCIONSERVICEFINDBYIDF);
			return mapper.map(cceTipoTransaccion, CceTipoTransaccionDto.class);
		}
		LOGGER.info(CCETIPOTRANSACCIONSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public CceTipoTransaccionDto save(CceTipoTransaccionDto cceTipoTransaccionDto) {
		LOGGER.info(CCETIPOTRANSACCIONSERVICESAVEI);
		CceTipoTransaccion cceTipoTransaccion = mapper.map(cceTipoTransaccionDto, CceTipoTransaccion.class);
		CceTipoTransaccion cceTipoTransaccionSave = repo.save(cceTipoTransaccion);
		LOGGER.info(CCETIPOTRANSACCIONSERVICESAVEF);
		return mapper.map(cceTipoTransaccionSave, CceTipoTransaccionDto.class); 
		
	}

	@Override
	public void deleteById(int id) {
		LOGGER.info(CCETIPOTRANSACCIONSERVICEDELETEBYIDI);
		LOGGER.info(CCETIPOTRANSACCIONSERVICEDELETEBYIDF);
		repo.deleteById(id);
	}

}
