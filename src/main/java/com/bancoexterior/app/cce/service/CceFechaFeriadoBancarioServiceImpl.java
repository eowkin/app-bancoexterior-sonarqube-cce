package com.bancoexterior.app.cce.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceFechaFeriadoBancarioDto;
import com.bancoexterior.app.cce.model.CceFechaFeriadoBancario;
import com.bancoexterior.app.cce.repository.ICceFechaFeriadoBancarioRepository;
import com.bancoexterior.app.util.Mapper;

@Service
public class CceFechaFeriadoBancarioServiceImpl implements ICceFechaFeriadoBancarioService{

	private static final Logger LOGGER = LogManager.getLogger(CceFechaFeriadoBancarioServiceImpl.class);
	
	@Autowired
	private ICceFechaFeriadoBancarioRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICELISTAI = "[==== INICIO listaFechasFeriado CceFechaFecriadoBancario Consultas - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICELISTAF = "[==== FIN listaFechasFeriado CceFechaFecriadoBancario Consultas - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICESAVEI = "[==== INICIO save CceFechaFecriadoBancario - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICESAVEF = "[==== FIN save CceFechaFecriadoBancario - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICEFINDBYIDI = "[==== INICIO findById CceFechaFecriadoBancario - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICEFINDBYIDF = "[==== FIN findById CceFechaFecriadoBancario - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICEDELETEI = "[==== INICIO delete CceFechaFecriadoBancario - Service ====]";
	
	private static final String CCEFECHAFERIADOBANCARIOSERVICEDELETEF = "[==== FIN delete CceFechaFecriadoBancario - Service ====]";
	
	
	@Override
	public List<CceFechaFeriadoBancario> listaFechasFeriado() {
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICELISTAI);
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICELISTAF);
		return repo.findAll();
	}

	@Override
	public CceFechaFeriadoBancarioDto save(CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto) {
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICESAVEI);
		CceFechaFeriadoBancario cceFechaFeriadoBancario = mapper.map(cceFechaFeriadoBancarioDto, CceFechaFeriadoBancario.class);
		CceFechaFeriadoBancario cceFechaFeriadoBancarioSave = repo.save(cceFechaFeriadoBancario);
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICESAVEF);
		return mapper.map(cceFechaFeriadoBancarioSave, CceFechaFeriadoBancarioDto.class);  
	}

	@Override
	public CceFechaFeriadoBancarioDto findById(int id) {
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICEFINDBYIDI);
		CceFechaFeriadoBancario cceFechaFeriadoBancario = repo.findById(id).orElse(null);
		if(cceFechaFeriadoBancario != null) {
			LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICEFINDBYIDF);
			return mapper.map(cceFechaFeriadoBancario, CceFechaFeriadoBancarioDto.class);
		}
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICEFINDBYIDF);
 		return null;
	}

	@Override
	public void delete(int id) {
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICEDELETEI);
		repo.deleteById(id);
		LOGGER.info(CCEFECHAFERIADOBANCARIOSERVICEDELETEF);
	}

}
