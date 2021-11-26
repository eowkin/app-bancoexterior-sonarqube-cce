package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.dto.CceFechaFeriadoBancarioDto;
import com.bancoexterior.app.cce.model.CceFechaFeriadoBancario;

public interface ICceFechaFeriadoBancarioService {

	public List<CceFechaFeriadoBancario> listaFechasFeriado();
	
	public CceFechaFeriadoBancarioDto save(CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto);
	
	public CceFechaFeriadoBancarioDto findById(int id);
	
	public void delete(int id);
}
