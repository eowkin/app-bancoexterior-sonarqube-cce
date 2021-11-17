package com.bancoexterior.app.cce.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceLbtrTransaccionDto;
import com.bancoexterior.app.cce.model.CceLbtrTransaccion;
import com.bancoexterior.app.cce.repository.ICceLbtrTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceLbtrTransaccionServiceImpl implements ICceLbtrTransaccionService{

	private static final Logger LOGGER = LogManager.getLogger(CceLbtrTransaccionServiceImpl.class);
	
	
	@Autowired
	private ICceLbtrTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	private static final String CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESI = "[==== INICIO ConsultaLbtrTransacciones CceLbtrTransaccion Consultas - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESF = "[==== FIN ConsultaLbtrTransacciones CceLbtrTransaccion Consultas - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESAPROBARFECHASI = "[==== INICIO ConsultaLbtrTransaccionesAprobarFechas CceLbtrTransaccion Consultas - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESAPROBARFECHASF = "[==== FIN ConsultaLbtrTransaccionesAprobarFechas CceLbtrTransaccion Consultas - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICESAVEI = "[==== INICIO Save CceLbtrTransaccion - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICESAVEF = "[==== FIN Save CceLbtrTransaccion - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICEFINDBYIDI = "[==== INICIO FindById CceLbtrTransaccion - Service ====]";
	
	private static final String CCELBTRTRANSACCIONSERVICEFINDBYIDF = "[==== FIN FindById CceLbtrTransaccion - Service ====]";
	
	private static final String HORADESDE = " 00:00:00";
	
	private static final String HORAHASTA = " 23:59:59";
	
	@Override
	public Page<CceLbtrTransaccion> consultaLbtrTransacciones(int page) {
		LOGGER.info(CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESI);
		int pageNumber = page;
		int pageSize = 10;

		Sort sort = Sort.by("fecha_actualizacion").descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		LOGGER.info(CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESF);
		return repo.consultaLbtrTransacciones(pageable);
	}

	@Override
	public CceLbtrTransaccionDto save(CceLbtrTransaccionDto cceLbtrTransaccionDto) {
		LOGGER.info(CCELBTRTRANSACCIONSERVICESAVEI);
		CceLbtrTransaccion cceLbtrTransaccion = mapper.map(cceLbtrTransaccionDto, CceLbtrTransaccion.class);
		CceLbtrTransaccion cceLbtrTransaccionSave = repo.save(cceLbtrTransaccion);
		LOGGER.info(CCELBTRTRANSACCIONSERVICESAVEF);
		return mapper.map(cceLbtrTransaccionSave, CceLbtrTransaccionDto.class);
	}

	@Override
	public CceLbtrTransaccionDto findById(int id) {
		LOGGER.info(CCELBTRTRANSACCIONSERVICEFINDBYIDI);
		CceLbtrTransaccion cceLbtrTransaccion = repo.findById(id).orElse(null);
		if(cceLbtrTransaccion != null) {
			LOGGER.info(CCELBTRTRANSACCIONSERVICEFINDBYIDF);
			return mapper.map(cceLbtrTransaccion, CceLbtrTransaccionDto.class);
		}
		LOGGER.info(CCELBTRTRANSACCIONSERVICEFINDBYIDF);
		return null;
	}

	@Override
	public Page<CceLbtrTransaccion> consultaLbtrTransaccionesAprobarFechas(String bancoReceptor, String fechaDesde,
			String fechaHasta, int page) {
		LOGGER.info(CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESAPROBARFECHASI);
		
		fechaDesde = fechaDesde +HORADESDE;
		fechaHasta = fechaHasta +HORAHASTA;
		
		int pageNumber = page;
		int pageSize = 10;

		Sort sort = Sort.by("fecha_valor").descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		LOGGER.info(CCELBTRTRANSACCIONSERVICECONSULTATRANSACCIONESAPROBARFECHASF);
		return repo.consultaLbtrTransaccionesAprobarFechas(bancoReceptor, fechaDesde, fechaHasta, pageable);
	}

	@Override
	public int countCodigoLbtrTransaccionByTipo(String codTransaccion) {
		return repo.countCodigoLbtrTransaccionByTipo(codTransaccion);
	}

}
