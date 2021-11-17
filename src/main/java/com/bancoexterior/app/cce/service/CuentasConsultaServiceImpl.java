package com.bancoexterior.app.cce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CuentasConsultaRequest;
import com.bancoexterior.app.cce.dto.CuentasConsultaResponse;
import com.bancoexterior.app.cce.model.CuentaCliente;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

@Service
public class CuentasConsultaServiceImpl implements ICuentasConsultaService{

	private static final Logger LOGGER = LogManager.getLogger(CuentasConsultaServiceImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
	
	@Value("${${app.ambiente}"+".SocketTimeout}")
	private int socketTimeout;
	 
	@Value("${${app.ambiente}"+".interconex.consultarcuentas.urlconsultar}")
	private String urlConsulta;  
	
	private static final String CUENTASCONSULTASERVICELISTAI = "[==== INICIO Lista CuentasConsulta ConsultaCuentas - Service ====]";
	
	private static final String CUENTASCONSULTASERVICELISTAF = "[==== FIN Lista CuentasConsulta ConsultaCuentas - Service ====]";
	
	private static final String CUENTASCONSULTAFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  CuentasConsulta - Service ====]";
	
	private static final String CUENTASCONSULTAFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  CuentasConsulta - Service ====]";
	 
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio CuentasConsulta"; 
	
	private static final String CUENTASCONSULTA = "cuentasConsulta";
	 
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	public WSResponse getRetornoPost(WSRequest wsrequest, CuentasConsultaRequest cuentasConsultaRequest, String url) {
    	WSResponse retorno;
		String cuentasConsultaRequestJSON;
		cuentasConsultaRequestJSON = new Gson().toJson(cuentasConsultaRequest);
		wsrequest.setBody(cuentasConsultaRequestJSON);
		LOGGER.info(cuentasConsultaRequestJSON);
		wsrequest.setUrl(url);
		LOGGER.info(url);
		retorno = wsService.post(wsrequest);
		LOGGER.info(retorno);
		return retorno;
    }
	
	@Override
	public CuentasConsultaResponse consultaCuentas(CuentasConsultaRequest cuentasConsultaRequest)
			throws CustomException {
		LOGGER.info(CUENTASCONSULTASERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, cuentasConsultaRequest, urlConsulta);
		LOGGER.info(retorno);
		return null;
	}

	@Override
	public List<CuentaCliente> consultaCuentasCliente(CuentasConsultaRequest cuentasConsultaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(CUENTASCONSULTASERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, cuentasConsultaRequest, urlConsulta);
		LOGGER.info(retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(CUENTASCONSULTASERVICELISTAF);
				return respuesta2xxConsultaCuentasCliente(retorno, accion, cuentasConsultaRequest, request);
			}else {
				throw new CustomException(respuesta4xxConsultaCuentasCliente(retorno, accion, cuentasConsultaRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<CuentaCliente> respuesta2xxConsultaCuentasCliente(WSResponse retorno, String accion, CuentasConsultaRequest cuentasConsultaRequest, HttpServletRequest request){
		try {
			CuentasConsultaResponse cuentasConsultaResponse = mapper.jsonToClass(retorno.getBody(), CuentasConsultaResponse.class);
			//if(cuentasConsultaResponse.getResultado().getCodigo().equals("0000") && cuentasConsultaResponse.getDatos().getTotalCuentas() > 0) {
			if(cuentasConsultaResponse.getResultado().getCodigo().equals("0000")) {
				guardarAuditoriaId(accion, true, cuentasConsultaResponse.getResultado().getCodigo(),  cuentasConsultaResponse.getResultado().getDescripcion(), cuentasConsultaRequest.getIdCliente(), request);
				return cuentasConsultaResponse.getDatos().getCuentas();
			}else {
				guardarAuditoriaId(accion, true, cuentasConsultaResponse.getResultado().getCodigo(),  cuentasConsultaResponse.getResultado().getDescripcion(), cuentasConsultaRequest.getIdCliente(), request);
				return new ArrayList<>();
			}	
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaId(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), cuentasConsultaRequest.getIdCliente(), request);
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxConsultaCuentasCliente(WSResponse retorno, String accion, CuentasConsultaRequest cuentasConsultaRequest, HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaId(accion, false, resultado.getCodigo(),  resultado.getDescripcion(), cuentasConsultaRequest.getIdCliente(), request);				
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaId(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), cuentasConsultaRequest.getIdCliente(), request);	
			return null;
		}
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CUENTASCONSULTAFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CUENTASCONSULTA, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(CUENTASCONSULTAFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, String idCliente, HttpServletRequest request) {
		try {
			LOGGER.info(CUENTASCONSULTAFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CUENTASCONSULTA, accion, codRespuesta, resultado, respuesta+" Cuenta:[idCliente="+idCliente+"]", request.getRemoteAddr());
			LOGGER.info(CUENTASCONSULTAFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

}
