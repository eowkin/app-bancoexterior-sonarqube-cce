package com.bancoexterior.app.cce.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bancoexterior.app.cce.dto.MontoMinComisionConsultaRequest;
import com.bancoexterior.app.cce.dto.MontoMinComisionRequest;
import com.bancoexterior.app.cce.model.NexoHistorialMontoMinComision;
import com.bancoexterior.app.cce.model.NexoMontoMinComision;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface INexoMontoMinComisionService {

	public List<NexoMontoMinComision> listaNexoMontoMinComision(MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public List<NexoHistorialMontoMinComision> listaNexoHistorialMontoMinComision(MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public NexoMontoMinComision buscarNexoMontoMinComision(MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)throws CustomException;
	
	public String actualizar(MontoMinComisionRequest montoMinComisionRequest, String accion, HttpServletRequest request)throws CustomException;
}
