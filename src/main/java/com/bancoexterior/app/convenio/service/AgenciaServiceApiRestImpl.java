package com.bancoexterior.app.convenio.service;

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

import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.dto.AgenciaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;




@Service
public class AgenciaServiceApiRestImpl implements IAgenciaServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(AgenciaServiceApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Agencias";
	
	@Autowired 
	private Mapper mapper;
	
	private static final String AGENCIASERVICELISTAI = "[==== INICIO Lista Agencias Consultas - Service ====]";
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String AGENCIASERVICELISTAF = "[==== FIN Lista Agencias Consultas - Service ====]";
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
	private static final String AGENCIASERVICEBUSCARI = "[==== INICIO Buscar Agencia Consultas - Service ====]";
	
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    private static final String AGENCIASERVICEBUSCARF = "[==== FIN Buscar Agencia Consultas - Service ====]";
    
    @Value("${${app.ambiente}"+".agencia.urlConsulta}")
    private String urlConsulta;
    
    private static final String AGENCIASERVICEACTUALIZARI = "[==== INICIO Actualizar Agencia - Service ====]";
    
    @Value("${${app.ambiente}"+".agencia.urlActualizar}")
    private String urlActualizar;
	
	private static final String AGENCIASERVICEACTUALIZARF = "[==== FIN Actualizar Agencia - Service ====]";
	
	private static final String AGENCIASERVICECREARI = "[==== INICIO Crear Agencia - Service ====]";
	
	private static final String AGENCIASERVICECREARF = "[==== FIN Crear Agencia - Service ====]";
	
	private static final String AGENCIAFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  Agencia - Controller ====]";
	
	private static final String AGENCIAFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  Agencia - Controller ====]";
	
	private static final String AGENCIAFUNCIONAUDITORIAACTIVARDESACTIVARI = "[==== INICIO Guardar Auditoria Activar/Desactivar Agencia - Controller ====]";
	
	private static final String AGENCIAFUNCIONAUDITORIAACTIVARDESACTIVARF = "[==== FIN Guardar Auditoria Activar/Desactivar Agencia - Controller ====]";
	
	private static final String SEARCH = "search";
	
	private static final String SAVE = "save";
	
	private static final String SEARCHNOMBRE = "searchNombre";
	
	private static final String AGENCIAS = "Agencias";
	
	private static final String ACTIVAR = "Activar";
	
	private static final String DESACTIVAR = "Desactivar";
	
	private static final String INDEX = "index";
	
	private static final String FORMBUSCARAGENCIA = "formBuscarAgencia";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    public WSResponse getRetornoPost(WSRequest wsrequest, AgenciaRequest agenciaRequest, String url) {
    	WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(url);
		retorno = wsService.post(wsrequest);
		return retorno;
    }
    
    public WSResponse getRetornoPut(WSRequest wsrequest, AgenciaRequest agenciaRequest, String url) {
    	WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(url);
		retorno = wsService.put(wsrequest);
		return retorno;
    }
    
    
	@Override
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICELISTAF);
				return respuesta2xxListaAgencias(retorno);
			}else {
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<Agencia> respuesta2xxListaAgencias(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
	        return agenciaResponse.getListaAgencias();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaAgencias(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(AGENCIASERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlConsulta);
		LOGGER.info(retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICELISTAF);
				return respuesta2xxListaAgencias(retorno, accion, agenciaRequest, request);
			}else {
				throw new CustomException(respuesta4xxListaAgencias(retorno, accion, agenciaRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Agencia> respuesta2xxListaAgencias(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
			if(accion.equals(INDEX) || accion.equals(FORMBUSCARAGENCIA)) {
				guardarAuditoria(accion, true, agenciaResponse.getResultado().getCodigo(), agenciaResponse.getResultado().getDescripcion(), request);
			}else {
				guardarAuditoriaSearchGeneral(accion,true, agenciaResponse,agenciaRequest,  request);
			}
			
			return agenciaResponse.getListaAgencias();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX) || accion.equals(FORMBUSCARAGENCIA)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			}else {
				guardarAuditoriaSearchGeneralException(accion, false, agenciaRequest, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			}	
			return new ArrayList<>();
		}
        
	}
		
		

	public String respuesta4xxListaAgencias(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals(INDEX) || accion.equals(FORMBUSCARAGENCIA)) {
				guardarAuditoria(accion, false, resultado.getCodigo(), resultado.getDescripcion(), request);
			}else {
				guardarAuditoriaSearchGeneralException(accion, false, agenciaRequest, resultado.getCodigo(), resultado.getDescripcion(), request);
			}
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX) || accion.equals(FORMBUSCARAGENCIA)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			}else {
				guardarAuditoriaSearchGeneralException(accion, false, agenciaRequest, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			}	
			return null;
		}
	}
	
	

	
	@Override
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEBUSCARF);
				return respuesta2xxBuscarAgencia(retorno);
			}else {
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	
	@Override
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(AGENCIASERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEBUSCARF);
				return respuesta2xxBuscarAgencia(retorno, accion, agenciaRequest, request);
			}else {
				throw new CustomException(respuesta4xxBuscarAgencia(retorno, accion, agenciaRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public Agencia respuesta2xxBuscarAgencia(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
			guardarAuditoriaCodigo(accion, true, agenciaResponse.getResultado().getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), agenciaResponse.getResultado().getDescripcion(), request);
	        if(agenciaResponse.getResultado().getCodigo().equals("0000")){
	        	return agenciaResponse.getListaAgencias().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
			return null;
		}
        
	}
	
	public String respuesta4xxBuscarAgencia(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaCodigo(accion, false, resultado.getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
			return null;
		}
        
	}

	public Agencia respuesta2xxBuscarAgencia(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
	        if(agenciaResponse.getResultado().getCodigo().equals("0000")){
	        	return agenciaResponse.getListaAgencias().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}
	
	@Override
	public String actualizar(AgenciaRequest agenciaRequest, String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(AGENCIASERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, agenciaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno, accion, agenciaRequest, request);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno, accion, agenciaRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public String respuesta2xxActualizarCrear(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals(ACTIVAR) || accion.equals(DESACTIVAR)) {
				guardarAuditoriaActivarDesactivar(accion, true, resultado.getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), resultado.getDescripcion(), request);
			}else {
				if(accion.equals(SAVE)) {
					guardarAuditoriaCodigo(accion, true, resultado.getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), resultado.getDescripcion(), request);
				}
			}
			
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(ACTIVAR) || accion.equals(DESACTIVAR)) {
				guardarAuditoriaActivarDesactivar(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
			}else {
				if(accion.equals(SAVE)) {
					guardarAuditoriaCodigo(accion, true, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
				}
			}
			
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			if(accion.equals(ACTIVAR) || accion.equals(DESACTIVAR)) {
				guardarAuditoriaActivarDesactivar(accion, false, response.getResultado().getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), response.getResultado().getDescripcion(), request);
			}else {
				if(accion.equals(SAVE)) {
					guardarAuditoriaCodigo(accion, false, response.getResultado().getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), response.getResultado().getDescripcion(), request);
				}
			}
			
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(ACTIVAR) || accion.equals(DESACTIVAR)) {
				guardarAuditoriaActivarDesactivar(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
			}else {
				if(accion.equals(SAVE)) {
					guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
				}
			}
			return null;
		}
	}
	
	@Override
	public String actualizar(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, agenciaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public String crear(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				if (retorno.getStatus() == 422) {
					throw new CustomException(respuesta4xxActualizarCrear(retorno));
					
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						throw new CustomException(respuesta4xxListaAgencias(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	@Override
	public String crear(AgenciaRequest agenciaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(AGENCIASERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, agenciaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICECREARF);
				return respuesta2xxActualizarCrear(retorno,accion, agenciaRequest, request);
			}else {
				if (retorno.getStatus() == 422) {
					throw new CustomException(respuesta4xxActualizarCrear(retorno,accion, agenciaRequest, request));
					
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						throw new CustomException(respuesta4xxActualizarCrearResultado(retorno,accion, agenciaRequest, request));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	public String respuesta4xxActualizarCrearResultado(WSResponse retorno, String accion, AgenciaRequest agenciaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaCodigo(accion, false, resultado.getCodigo(), agenciaRequest.getAgencia().getCodAgencia(), resultado.getDescripcion(), request);
			
			return resultado.getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaCodigo(accion, false, String.valueOf(retorno.getStatus()), agenciaRequest.getAgencia().getCodAgencia(), retorno.getBody(), request);
			return null;
		}
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta, String respuesta, HttpServletRequest request) {
		LOGGER.info(AGENCIAFUNCIONAUDITORIAI);
		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
				AGENCIAS, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
		LOGGER.info(AGENCIAFUNCIONAUDITORIAF);
	}
	
	
	public void guardarAuditoriaSearchGeneral(String accion, boolean resultado, AgenciaResponse agenciaResponse,AgenciaRequest agenciaRequest,  HttpServletRequest request) {
		if(accion.equals(SEARCH)) {
			String codAgencia = "";
			if(agenciaRequest.getAgencia().getCodAgencia() != null) {
				codAgencia = agenciaRequest.getAgencia().getCodAgencia();
			}
			guardarAuditoriaCodigo(accion, resultado, agenciaResponse.getResultado().getCodigo(), codAgencia, agenciaResponse.getResultado().getDescripcion(), request);
		}else {
			if(accion.equals(SEARCHNOMBRE)) {
				String nombreAgencia = "";
				if(agenciaRequest.getAgencia().getNombreAgencia() != null) {
					nombreAgencia = agenciaRequest.getAgencia().getNombreAgencia();
				}
				guardarAuditoriaSearchNombre(accion, resultado, agenciaResponse.getResultado().getCodigo(), nombreAgencia, agenciaResponse.getResultado().getDescripcion(), request);
			}
		}
	}
	
	public void guardarAuditoriaSearchGeneralException(String accion, boolean resultado,AgenciaRequest agenciaRequest, String status, String respuesta, HttpServletRequest request) {
		if(accion.equals(SEARCH)) {
			String codAgencia = "";
			if(agenciaRequest.getAgencia().getCodAgencia() != null) {
				codAgencia = agenciaRequest.getAgencia().getCodAgencia();
			}
			guardarAuditoriaCodigo(accion, resultado, status, codAgencia, respuesta, request);
		}else {
			if(accion.equals(SEARCHNOMBRE)) {
				String nombreAgencia = "";
				if(agenciaRequest.getAgencia().getNombreAgencia() != null) {
					nombreAgencia = agenciaRequest.getAgencia().getNombreAgencia();
				}
				guardarAuditoriaSearchNombre(accion, resultado, status, nombreAgencia, respuesta, request);
			}
		}
	}
	
	
	public void guardarAuditoriaSearchNombre(String accion, boolean resultado, String codRespuesta, String nombreAgencia, String respuesta, HttpServletRequest request) {
		LOGGER.info(AGENCIAFUNCIONAUDITORIAI);
		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
				AGENCIAS, accion, codRespuesta, resultado, respuesta+" Agencia:[nombreAgencia="+nombreAgencia+"]", request.getRemoteAddr());
		LOGGER.info(AGENCIAFUNCIONAUDITORIAF);
	}
	
	
	public void guardarAuditoriaCodigo(String accion, boolean resultado, String codRespuesta, String codAgencia, String respuesta, HttpServletRequest request) {
		LOGGER.info(AGENCIAFUNCIONAUDITORIAI);
		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
	 AGENCIAS, accion, codRespuesta, resultado, respuesta+" Agencia:[codAgencia="+codAgencia+"]", request.getRemoteAddr());
		LOGGER.info(AGENCIAFUNCIONAUDITORIAF);
	}
	
	
	public void guardarAuditoriaActivarDesactivar(String accion, boolean resultado, String codRespuesta, String codAgencia, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(AGENCIAFUNCIONAUDITORIAACTIVARDESACTIVARI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					AGENCIAS, accion, codRespuesta, resultado, respuesta+" Agencia:[codAgencia="+codAgencia+"]", request.getRemoteAddr());
			LOGGER.info(AGENCIAFUNCIONAUDITORIAACTIVARDESACTIVARF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}


	


	


	

}
