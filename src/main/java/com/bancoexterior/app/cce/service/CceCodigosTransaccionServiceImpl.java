package com.bancoexterior.app.cce.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.cce.dto.CceCodigosTransaccionDto;
import com.bancoexterior.app.cce.model.CceCodigosTransaccion;
import com.bancoexterior.app.cce.repository.ICceCodigosTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceCodigosTransaccionServiceImpl implements ICceCodigosTransaccionService{

	private static final Logger LOGGER = LogManager.getLogger(CceCodigosTransaccionServiceImpl.class);
	
	@Autowired
	private ICceCodigosTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	
	private static final String CCECODIGOSTRANSACCIONSERVICEFINDALLI = "[==== INICIO CceCodigosTransaccion findAll - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEFINDALLF = "[==== FIN CceCodigosTransaccion findAll - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEFINDBYIDI = "[==== INICIO CceCodigosTransaccion findById - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEFINDBYIDF = "[==== FIN CceCodigosTransaccion findById - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICESAVEI = "[==== INICIO CceCodigosTransaccion save - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICESAVEF = "[==== FIN CceCodigosTransaccion save - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOI = "[==== INICIO CceCodigosTransaccion consultarTodosCodigosConTipo - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOF = "[==== FIN CceCodigosTransaccion consultarTodosCodigosConTipo - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECODIGOTRANSACCIONBYIDI = "[==== INICIO CceCodigosTransaccion codigoTransaccionById - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECODIGOTRANSACCIONBYIDF = "[==== FIN CceCodigosTransaccion codigoTransaccionById - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECREARCODIGOTRANSACCIONI = "[==== INICIO CceCodigosTransaccion crearCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECREARCODIGOTRANSACCIONF = "[==== FIN CceCodigosTransaccion crearCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEACTUALIZARCODIGOTRANSACCIONI = "[==== INICIO CceCodigosTransaccion actualizarCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEACTUALIZARCODIGOTRANSACCIONF = "[==== FIN CceCodigosTransaccion actualizarCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEELIMINARCODIGOTRANSACCIONI = "[==== INICIO CceCodigosTransaccion eliminarCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICEELIMINARCODIGOTRANSACCIONF = "[==== FIN CceCodigosTransaccion eliminarCodigoTransaccion - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECOUNTCODIGOTRANSACCIONBYTIPOI = "[==== INICIO CceCodigosTransaccion countCodigoTransaccionByTipo - Service ====]";
	
	private static final String CCECODIGOSTRANSACCIONSERVICECOUNTCODIGOTRANSACCIONBYTIPOF = "[==== FIN CceCodigosTransaccion countCodigoTransaccionByTipo - Service ====]";
	
	
	
	@Override
	public List<CceCodigosTransaccion> findAll() {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEFINDALLI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEFINDALLF);
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public CceCodigosTransaccionDto findById(String codTransaccion) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEFINDBYIDI);
		CceCodigosTransaccion cceCodigosTransaccion = repo.findById(codTransaccion).orElse(null);
		
		if(cceCodigosTransaccion != null) {
			LOGGER.info(CCECODIGOSTRANSACCIONSERVICEFINDBYIDF);
			return mapper.map(cceCodigosTransaccion, CceCodigosTransaccionDto.class);
		}
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public CceCodigosTransaccionDto save(CceCodigosTransaccionDto cceCodigosTransaccionDto) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICESAVEI);
		CceCodigosTransaccion cceCodigosTransaccion = mapper.map(cceCodigosTransaccionDto, CceCodigosTransaccion.class);
		CceCodigosTransaccion cceCodigosTransaccionSave = repo.save(cceCodigosTransaccion);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICESAVEF);
		return mapper.map(cceCodigosTransaccionSave, CceCodigosTransaccionDto.class);
	}

	@Override
	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo() {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOF);
		return repo.consultarTodosCodigosConTipo();
	}

	@Override
	public CceCodigosTransaccionDto codigoTransaccionById(String codTransaccion) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECODIGOTRANSACCIONBYIDI);
		CceCodigosTransaccion cceCodigosTransaccion = repo.codigoTransaccionById(codTransaccion);
		
		if(cceCodigosTransaccion != null) {
			LOGGER.info(CCECODIGOSTRANSACCIONSERVICECODIGOTRANSACCIONBYIDF);
			return mapper.map(cceCodigosTransaccion, CceCodigosTransaccionDto.class);
		}
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECODIGOTRANSACCIONBYIDF);
		return null;
	}

	@Override
	public void crearCodigoTransaccion(String codTransaccion, String descripcion, int idTipo) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECREARCODIGOTRANSACCIONI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECREARCODIGOTRANSACCIONF);
		repo.crearCodigoTransaccion(codTransaccion, descripcion, idTipo);
		
	}

	@Override
	public void actualizarCodigoTransaccion(String codTransaccion, String descripcion, int idTipo) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEACTUALIZARCODIGOTRANSACCIONI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEACTUALIZARCODIGOTRANSACCIONF);
		repo.actualizarCodigoTransaccion(codTransaccion, descripcion, idTipo);
	}

	@Override
	public void eliminarCodigoTransaccion(String codTransaccion) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEELIMINARCODIGOTRANSACCIONI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICEELIMINARCODIGOTRANSACCIONF);
		repo.eliminarCodigoTransaccion(codTransaccion);
	}

	@Override
	public int countCodigoTransaccionByTipo(int idTipo) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECOUNTCODIGOTRANSACCIONBYTIPOI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECOUNTCODIGOTRANSACCIONBYTIPOF);
		return repo.countCodigoTransaccionByTipo(idTipo);
	}

	@Override
	public List<CceCodigosTransaccion> consultarTodosCodigosConTipo(int idTipo) {
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOI);
		LOGGER.info(CCECODIGOSTRANSACCIONSERVICECONSULTARTODOSCODIGOSCONTIPOF);
		return repo.consultarTodosCodigosConTipo(idTipo);
	}

}
