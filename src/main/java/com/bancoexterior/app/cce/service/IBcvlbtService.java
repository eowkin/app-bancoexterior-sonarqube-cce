package com.bancoexterior.app.cce.service;



import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.cce.dto.AprobacionRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.response.Resultado;

public interface IBcvlbtService {
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException;
	
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(AprobacionesConsultasRequest aprobacionesConsultasRequest, 
			String accion, HttpServletRequest request) throws CustomException;
	
	public BCVLBT buscarBCVLBT(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException;
	
	
	public Resultado aporbarAltoBajoValor(FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, 
			String accion, HttpServletRequest request) throws CustomException;
	
	public Resultado aporbarActualizar(AprobacionRequest aprobacionRequest, 
			String accion, HttpServletRequest request) throws CustomException;
}
