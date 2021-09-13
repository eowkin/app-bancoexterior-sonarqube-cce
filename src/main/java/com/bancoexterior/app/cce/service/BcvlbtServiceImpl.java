package com.bancoexterior.app.cce.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.AprobacionRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.model.BCVLBT;
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
public class BcvlbtServiceImpl implements IBcvlbtService{
	
	private static final Logger LOGGER = LogManager.getLogger(BcvlbtServiceImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio IBCVLBT";
	
	@Autowired 
	private Mapper mapper;
	
	private static final String BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARI = "[==== INICIO ListaTransaccionesPorAporbar Bcvlbt Consultas - Service ====]";
	 
	@Value("${${app.ambiente}"+".ConnectTimeout}")
	private int connectTimeout;
	    
	private static final String BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARF = "[==== FIN ListaTransaccionesPorAporbar Bcvlbt Consultas - Service ====]";
	 
	@Value("${${app.ambiente}"+".SocketTimeout}")
	private int socketTimeout;
	 
	@Value("${${app.ambiente}"+".transacciones.lbtr.urlConsulta}")
	private String urlConsulta;
	
	@Value("${${app.ambiente}"+".transacciones.lbtr.urlAprobacion}")
	private String urlAprobacion;
	
	@Value("${${app.ambiente}"+".transacciones.lbtr.urlAprobacionActualizar}")
	private String urlAprobacionActualizar;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	 
	private static final String CCETRANSACCIONSERVICEFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  Cce - Service ====]";
	
	private static final String CCETRANSACCIONSERVICEFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  Cce - Service ====]";
	
	private static final String CCE = "Cce";
	
	private static final String FORMAPROBARMOVIMIENTOSALTOVALORAUTOMATICO = "formAprobarMovimientosAltoValorLoteAutomatico";
 
	private static final String BCVLBTSERVICEPORAPROBARTRANSACCIONESI = "[==== INICIO AporbarTransaccion Bcvlbt - Service ====]";
	
	private static final String BCVLBTSERVICEPORAPROBARTRANSACCIONESF = "[==== FIN AporbarTransaccion Bcvlbt - Service ====]";
	 
	private static final String BCVLBTSERVICEPORAPROBARACTUALIZARTRANSACCIONESI = "[==== INICIO Aporbar ActualizarTransaccion Bcvlbt - Service ====]";
	
	private static final String BCVLBTSERVICEPORAPROBARACTUALIZARTRANSACCIONESF = "[==== FIN Aporbar Actualizar Transaccion Bcvlbt - Service ====]";
	
	 public WSRequest getWSRequest() {
	    	WSRequest wsrequest = new WSRequest();
	    	wsrequest.setConnectTimeout(connectTimeout);
			wsrequest.setContenType("application/json");
			wsrequest.setSocketTimeout(socketTimeout);
	    	return wsrequest;
	 }



	@Override
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(
			AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException {
		LOGGER.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost( wsrequest, aprobacionesConsultasRequest, urlConsulta);
		LOGGER.info(retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARF);
				return respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno);
			} else {
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public AprobacionesConsultasResponse respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	return aprobacionesConsultasResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}






	@Override
	public BCVLBT buscarBCVLBT(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobacionesConsultasRequestJSON;
		aprobacionesConsultasRequestJSON = new Gson().toJson(aprobacionesConsultasRequest);
		wsrequest.setBody(aprobacionesConsultasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxBCVLBT(retorno);
			} else {
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	
	public BCVLBT respuesta2xxBCVLBT(WSResponse retorno) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);	
			
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	
	        	return aprobacionesConsultasResponse.getOperaciones().get(0);
	        	
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}

	public WSResponse getRetornoPost(WSRequest wsrequest, AprobacionesConsultasRequest aprobacionesConsultasRequest, String url) {
		WSResponse retorno;
		String aprobacionesConsultasJSON;
		aprobacionesConsultasJSON = new Gson().toJson(aprobacionesConsultasRequest);
		wsrequest.setBody(aprobacionesConsultasJSON);
		LOGGER.info(aprobacionesConsultasJSON);
		wsrequest.setUrl(url);	
		retorno = wsService.post(wsrequest);
		return retorno;
	}
	
	public WSResponse getRetornoAprobarPost(WSRequest wsrequest, FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, String url) {
		WSResponse retorno;
		String fiToFiCustomerCreditTransferJSON;
		fiToFiCustomerCreditTransferJSON = new Gson().toJson(fiToFiCustomerCreditTransferRequest);
		wsrequest.setBody(fiToFiCustomerCreditTransferJSON);
		LOGGER.info(fiToFiCustomerCreditTransferJSON);
		wsrequest.setUrl(url);	
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(
			AprobacionesConsultasRequest aprobacionesConsultasRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost( wsrequest, aprobacionesConsultasRequest, urlConsulta);
		LOGGER.info(retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARF);
				return respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno, accion, aprobacionesConsultasRequest, request);
			} else {
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno, accion, aprobacionesConsultasRequest, request));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public AprobacionesConsultasResponse respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno, String accion,
			AprobacionesConsultasRequest aprobacionesConsultasRequest, HttpServletRequest request) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);
			guardarAuditoriaListaAutomatico(accion, true, aprobacionesConsultasResponse.getResultado().getCodigo(), 
					aprobacionesConsultasResponse.getResultado().getDescripcion(), aprobacionesConsultasRequest, request);
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	return aprobacionesConsultasResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaListaAutomatico(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), aprobacionesConsultasRequest, request);
			return null;
		}
		
	}
	
	
	public String respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno, String accion,
			AprobacionesConsultasRequest aprobacionesConsultasRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaListaAutomatico(accion, true, resultado.getCodigo(), resultado.getDescripcion(), aprobacionesConsultasRequest, request);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaListaAutomatico(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), aprobacionesConsultasRequest, request);
			return null;
		}
	}
	
	public void guardarAuditoriaListaAutomatico(String accion, boolean resultado, String codRespuesta,  String respuesta, AprobacionesConsultasRequest aprobacionesConsultasRequest, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAI);
			if(accion.equals(FORMAPROBARMOVIMIENTOSALTOVALORAUTOMATICO))
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta+" BcvlbtTransaccion:[status="+aprobacionesConsultasRequest.getFiltros().getStatus()+","
							+ "montoDesde="+aprobacionesConsultasRequest.getFiltros().getMontoDesde()+", montoHasta="+aprobacionesConsultasRequest.getFiltros().getMontoHasta()+"]", request.getRemoteAddr());
			else
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						CCE, accion, codRespuesta, resultado, respuesta+" BcvlbtTransaccion:[status="+aprobacionesConsultasRequest.getFiltros().getStatus()+","
								+ "montoDesde="+aprobacionesConsultasRequest.getFiltros().getMontoDesde()+", montoHasta="+aprobacionesConsultasRequest.getFiltros().getMontoHasta()+", "
								+ "fechaDesde="+aprobacionesConsultasRequest.getFiltros().getFechaDesde()+", fechaHasta="+aprobacionesConsultasRequest.getFiltros().getFechaHasta()+", "
								+ "bancoBeneficiario="+aprobacionesConsultasRequest.getFiltros().getBancoBeneficiario()+", nroIdEmisor="+aprobacionesConsultasRequest.getFiltros().getNroIdEmisor()+"]", request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}


	
	

	@Override
	public Resultado aporbarAltoBajoValor(
			FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(BCVLBTSERVICEPORAPROBARTRANSACCIONESI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoAprobarPost( wsrequest, fiToFiCustomerCreditTransferRequest, urlAprobacion);
		LOGGER.info(retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200 || retorno.getStatus() == 202) {
				LOGGER.info(BCVLBTSERVICEPORAPROBARTRANSACCIONESF);
				return respuesta2xxAporbar(retorno, accion, fiToFiCustomerCreditTransferRequest, request);
			} else {
				throw new CustomException(respuesta4xxAporbar(retorno, accion, fiToFiCustomerCreditTransferRequest, request));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	
	public Resultado respuesta2xxAporbar(WSResponse retorno, String accion,
			FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, HttpServletRequest request) {
		try {
			Resultado resultadoResponse = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaListaAprobar(accion, true, resultadoResponse.getCodigo(), 
					resultadoResponse.getDescripcion(), fiToFiCustomerCreditTransferRequest, request);
			return resultadoResponse;
	        
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaListaAprobar(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), fiToFiCustomerCreditTransferRequest, request);
			return null;
		}
		
	}
	
	public String respuesta4xxAporbar(WSResponse retorno, String accion,
			FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaListaAprobar(accion, true, resultado.getCodigo(), resultado.getDescripcion(), fiToFiCustomerCreditTransferRequest, request);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaListaAprobar(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), fiToFiCustomerCreditTransferRequest, request);
			return null;
		}
	}
	
	public void guardarAuditoriaListaAprobar(String accion, boolean resultado, String codRespuesta,  String respuesta,
			FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta+" BcvlbtTransaccion:[codTransaccion="+ fiToFiCustomerCreditTransferRequest.getParamIdentificacion().getCodTransaccion()+","
					+ "idSesion="+fiToFiCustomerCreditTransferRequest.getParamIdentificacion().getIdSesion()+", idUsuario="+fiToFiCustomerCreditTransferRequest.getParamIdentificacion().getIdUsuario()+", "
					+ "bancoReceptor="+fiToFiCustomerCreditTransferRequest.getParamIdentificacion().getBancoReceptor()+"]", request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}


	public WSResponse getRetornoAprobarPut(WSRequest wsrequest, AprobacionRequest aprobacionRequest, String url) {
		WSResponse retorno;
		String aprobacionJSON;
		aprobacionJSON = new Gson().toJson(aprobacionRequest);
		wsrequest.setBody(aprobacionJSON);
		LOGGER.info(aprobacionJSON);
		wsrequest.setUrl(url);	
		retorno = wsService.put(wsrequest);
		return retorno;
	}
	
	
	@Override
	public Resultado aporbarActualizar(AprobacionRequest aprobacionRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(BCVLBTSERVICEPORAPROBARACTUALIZARTRANSACCIONESI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoAprobarPut( wsrequest, aprobacionRequest, urlAprobacionActualizar);
		LOGGER.info(retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				LOGGER.info(BCVLBTSERVICEPORAPROBARACTUALIZARTRANSACCIONESF);
				return respuesta2xxAporbarActualizar(retorno, accion, aprobacionRequest, request);
			} else {
				throw new CustomException(respuesta4xxAporbarActualizar(retorno, accion, aprobacionRequest, request));
			}
		} else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
		
	}
	
	public Resultado respuesta2xxAporbarActualizar(WSResponse retorno, String accion,
			AprobacionRequest aprobacionRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			Resultado resultadoResponse = response.getResultado();
			guardarAuditoriaAprobarActualizar(accion, true, resultadoResponse.getCodigo(), 
					resultadoResponse.getDescripcion(), aprobacionRequest, request);
			return resultadoResponse;
	        
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaAprobarActualizar(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), aprobacionRequest, request);
			return null;
		}
		
	}
	
	public String respuesta4xxAporbarActualizar(WSResponse retorno, String accion,
			AprobacionRequest aprobacionRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			Resultado resultado = response.getResultado();
			guardarAuditoriaAprobarActualizar(accion, true, resultado.getCodigo(), resultado.getDescripcion(), aprobacionRequest, request);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaAprobarActualizar(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), aprobacionRequest, request);
			return null;
		}
	}
	
	public void guardarAuditoriaAprobarActualizar(String accion, boolean resultado, String codRespuesta,  String respuesta,
			AprobacionRequest aprobacionRequest, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta+" BcvlbtTransaccion:[referencia="+ aprobacionRequest.getDatosAprobacion().getReferencia()+","
					+ "nroIdEmisor="+aprobacionRequest.getDatosAprobacion().getNroIdEmisor()+", status="+aprobacionRequest.getDatosAprobacion().getStatus()+", "
					+ "idSesion="+aprobacionRequest.getIdSesion()+"]", request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONSERVICEFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
