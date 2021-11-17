package com.bancoexterior.app.cce.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.cce.dto.CuentasConsultaRequest;
import com.bancoexterior.app.cce.dto.CuentasConsultaResponse;
import com.bancoexterior.app.cce.model.CuentaCliente;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface ICuentasConsultaService {

	public CuentasConsultaResponse consultaCuentas(CuentasConsultaRequest cuentasConsultaRequest) throws CustomException;
	
	public List<CuentaCliente> consultaCuentasCliente(CuentasConsultaRequest cuentasConsultaRequest, String accion, HttpServletRequest request) throws CustomException;
}
