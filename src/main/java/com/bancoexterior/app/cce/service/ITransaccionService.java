package com.bancoexterior.app.cce.service;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.cce.dto.TransaccionRequest;
import com.bancoexterior.app.cce.dto.TransaccionResponse;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface ITransaccionService {

	public TransaccionResponse procesar(TransaccionRequest transaccionRequest) throws CustomException;
	
	public TransaccionResponse procesar(TransaccionRequest transaccionRequest, String accion, HttpServletRequest request) throws CustomException;
}
