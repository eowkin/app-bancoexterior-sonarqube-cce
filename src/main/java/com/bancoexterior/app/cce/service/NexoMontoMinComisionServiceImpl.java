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


import com.bancoexterior.app.cce.dto.HistorialMontoMinComisionConsultaResponse;
import com.bancoexterior.app.cce.dto.MontoMinComisionConsultaRequest;
import com.bancoexterior.app.cce.dto.MontoMinComisionConsultaResponse;
import com.bancoexterior.app.cce.dto.MontoMinComisionRequest;
import com.bancoexterior.app.cce.model.NexoHistorialMontoMinComision;
import com.bancoexterior.app.cce.model.NexoMontoMinComision;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

@Service
public class NexoMontoMinComisionServiceImpl implements INexoMontoMinComisionService{

	private static final Logger LOGGER = LogManager.getLogger(NexoMontoMinComisionServiceImpl.class);
	
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
	 
    @Value("${${app.ambiente}"+".nexo.montomincomision.urlconsulta}")
	private String urlConsulta; 
	 
    @Value("${${app.ambiente}"+".nexo.historialmontomincomision.urlconsulta}")
	private String urlConsultaHistorial;
    
    @Value("${${app.ambiente}"+".nexo.actualizarmontomincomision.urlactualizar}")
	private String urlActualizar;
    
	private static final String NEXOMONTOMINCOMISIONSERVICELISTAI = "[==== INICIO Lista NexoMontoMinComision Consultas - Service ====]";
	
	private static final String NEXOMONTOMINCOMISIONSERVICELISTAF = "[==== FIN Lista NexoMontoMinComision Consultas - Service ====]";
	
	private static final String NEXOHISTORIALMONTOMINCOMISIONSERVICELISTAI = "[==== INICIO  NexoHistorialMontoMinComision Consultas - Service ====]";
	
	private static final String NEXOHISTORIALMONTOMINCOMISIONSERVICELISTAF = "[==== FIN  NexoHistorialMontoMinComision Consultas - Service ====]";
	
	private static final String NEXOMONTOMINCOMISIONBUSCARSERVICELISTAI = "[==== INICIO  NexoMontoMinComision Bucar - Service ====]";
	
	private static final String NEXOMONTOMINCOMISIONBUSCARSERVICELISTAF = "[==== FIN  NexoMontoMinComision Bucar - Service ====]";
	
	private static final String NEXOMONTOMINCOMISIONACTUALIZARSERVICELISTAI = "[==== INICIO  NexoMontoMinComision Actualizar - Service ====]";
	
	private static final String NEXOMONTOMINCOMISIONACTUALIZARSERVICELISTAF = "[==== FIN  NexoMontoMinComision Actualizar - Service ====]";
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio NexoMontoMinComision";
	
	private static final String NEXOMONTOMINCOMISIONFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  NexoMontoMinComision - Controller ====]";
	
	private static final String NEXOMONTOMINCOMISIONFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  NexoMontoMinComision - Controller ====]";
	
	private static final String COMISIONNEXOPERSONA = "comisionNexoPersona";
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	
	public WSResponse getRetornoPost(WSRequest wsrequest, MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String url) {
    	WSResponse retorno;
		String montoMinComisionConsultaRequestJSON;
		montoMinComisionConsultaRequestJSON = new Gson().toJson(montoMinComisionConsultaRequest);
		wsrequest.setBody(montoMinComisionConsultaRequestJSON);
		wsrequest.setUrl(url);
		retorno = wsService.post(wsrequest);
		return retorno;
    }
	
	public WSResponse getRetornoPut(WSRequest wsrequest, MontoMinComisionRequest montoMinComisionRequest) {
		WSResponse retorno;
		String montoMinComisionRequestJSON;
		montoMinComisionRequestJSON = new Gson().toJson(montoMinComisionRequest);
		wsrequest.setBody(montoMinComisionRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		return retorno;
	}
	
	
	@Override
	public List<NexoMontoMinComision> listaNexoMontoMinComision(
			MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(NEXOMONTOMINCOMISIONSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, montoMinComisionConsultaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(NEXOMONTOMINCOMISIONSERVICELISTAF);
				return respuesta2xxListaNexoMontoMinComision(retorno, accion, request);
			}else {
				if(retorno.getStatus() == 502 || retorno.getStatus() == 503) {
					LOGGER.error(ERRORMICROCONEXION);
					throw new CustomException(ERRORMICROCONEXION);
				}else {
					throw new CustomException(respuesta4xxListaNexoMontoMinComision(retorno, accion, montoMinComisionConsultaRequest, request));
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}

	
	public List<NexoMontoMinComision> respuesta2xxListaNexoMontoMinComision(WSResponse retorno, String accion, HttpServletRequest request){
		try {
			MontoMinComisionConsultaResponse montoMinComisionConsultaResponse = mapper.jsonToClass(retorno.getBody(), MontoMinComisionConsultaResponse.class);
			guardarAuditoria(accion, true, montoMinComisionConsultaResponse.getResultado().getCodigo(), montoMinComisionConsultaResponse.getResultado().getDescripcion(), request);
			return montoMinComisionConsultaResponse.getComisiones();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return new ArrayList<>();
		}
        
	}
	
	
	public String respuesta4xxListaNexoMontoMinComision(WSResponse retorno, String accion, MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals("index") || accion.equals("historial")) {
				guardarAuditoria(accion, false, resultado.getCodigo(), resultado.getDescripcion(), request);
			}else {
				guardarAuditoriaId(accion, false, resultado.getCodigo(), resultado.getDescripcion(), montoMinComisionConsultaRequest.getComision().getId(), request);
			}
				
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals("index") || accion.equals("historial")) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			}else {
				guardarAuditoriaId(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), montoMinComisionConsultaRequest.getComision().getId(), request);
			}	
			return null;
		}
	}


	@Override
	public List<NexoHistorialMontoMinComision> listaNexoHistorialMontoMinComision(
			MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(NEXOHISTORIALMONTOMINCOMISIONSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, montoMinComisionConsultaRequest, urlConsultaHistorial);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(NEXOHISTORIALMONTOMINCOMISIONSERVICELISTAF);
				return respuesta2xxListaNexoHistorialMontoMinComision(retorno, accion, request);
			}else {
				if(retorno.getStatus() == 502 || retorno.getStatus() == 503) {
					LOGGER.error(ERRORMICROCONEXION);
					throw new CustomException(ERRORMICROCONEXION);
				}else {
					throw new CustomException(respuesta4xxListaNexoMontoMinComision(retorno, accion, montoMinComisionConsultaRequest, request));
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
		
	}
	
	public List<NexoHistorialMontoMinComision> respuesta2xxListaNexoHistorialMontoMinComision(WSResponse retorno, String accion, HttpServletRequest request){
		try {
			HistorialMontoMinComisionConsultaResponse historialMontoMinComisionConsultaResponse = mapper.jsonToClass(retorno.getBody(), HistorialMontoMinComisionConsultaResponse.class);
			guardarAuditoria(accion, true, historialMontoMinComisionConsultaResponse.getResultado().getCodigo(), historialMontoMinComisionConsultaResponse.getResultado().getDescripcion(), request);
			return historialMontoMinComisionConsultaResponse.getComisiones();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return new ArrayList<>();
		}
        
	}


	
	
	@Override
	public NexoMontoMinComision buscarNexoMontoMinComision(
			MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(NEXOMONTOMINCOMISIONBUSCARSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, montoMinComisionConsultaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(NEXOMONTOMINCOMISIONBUSCARSERVICELISTAF);
				return respuesta2xxBuscarNexoMontoMinComision(retorno, accion, montoMinComisionConsultaRequest, request);
			}else {
				if(retorno.getStatus() == 502 || retorno.getStatus() == 503) {
					LOGGER.error(ERRORMICROCONEXION);
					throw new CustomException(ERRORMICROCONEXION);
				}else {
					throw new CustomException(respuesta4xxListaNexoMontoMinComision(retorno, accion, montoMinComisionConsultaRequest, request));
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public NexoMontoMinComision respuesta2xxBuscarNexoMontoMinComision(WSResponse retorno, String accion, MontoMinComisionConsultaRequest montoMinComisionConsultaRequest, HttpServletRequest request){
		try {
			MontoMinComisionConsultaResponse montoMinComisionConsultaResponse = mapper.jsonToClass(retorno.getBody(), MontoMinComisionConsultaResponse.class);
			guardarAuditoriaId(accion, true, montoMinComisionConsultaResponse.getResultado().getCodigo(), montoMinComisionConsultaResponse.getResultado().getDescripcion(), 
					montoMinComisionConsultaRequest.getComision().getId(), request);
			
			if(montoMinComisionConsultaResponse.getResultado().getCodigo().equals("0000")){
	        	return montoMinComisionConsultaResponse.getComisiones().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaId(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), montoMinComisionConsultaRequest.getComision().getId(), request);
			return null;
		}
        
	}
	
	
	@Override
	public String actualizar(MontoMinComisionRequest montoMinComisionRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(NEXOMONTOMINCOMISIONACTUALIZARSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, montoMinComisionRequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(NEXOMONTOMINCOMISIONACTUALIZARSERVICELISTAF);
				return respuesta4xxActualizarCrear(retorno,accion, montoMinComisionRequest, request);
			}else {
				if(retorno.getStatus() == 502 || retorno.getStatus() == 503) {
					LOGGER.error(ERRORMICROCONEXION);
					throw new CustomException(ERRORMICROCONEXION);
				}else {
					String respuesta4xxActualizar = respuesta4xxActualizar(retorno,accion, montoMinComisionRequest, request);
					throw new CustomException(respuesta4xxActualizar);
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, MontoMinComisionRequest montoMinComisionRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaMontoMinimoComision(accion, true, response.getResultado().getCodigo(),  response.getResultado().getDescripcion(), montoMinComisionRequest, request);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaMontoMinimoComision(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), montoMinComisionRequest, request);
			return null;
		}
	}
	
	
	public String respuesta4xxActualizar(WSResponse retorno,  String accion, MontoMinComisionRequest montoMinComisionRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaMontoMinimoComision(accion, false, response.getResultado().getCodigo(),  response.getResultado().getDescripcion(), montoMinComisionRequest, request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaMontoMinimoComision(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), montoMinComisionRequest, request);
			return null;
		}
	}

	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					COMISIONNEXOPERSONA, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					COMISIONNEXOPERSONA, accion, codRespuesta, resultado, respuesta+" MontoMinComision:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaMontoMinimoComision(String accion, boolean resultado, String codRespuesta,  String respuesta, MontoMinComisionRequest montoMinComisionRequest, HttpServletRequest request) {
		try {
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					COMISIONNEXOPERSONA, accion, codRespuesta, resultado, respuesta+" MontoMinimoComision:[id="+montoMinComisionRequest.getComision().getId()+""
							+ ", monto="+montoMinComisionRequest.getComision().getMonto()+", descripcion="+montoMinComisionRequest.getComision().getDescripcion()+"]", request.getRemoteAddr());
			LOGGER.info(NEXOMONTOMINCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
