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
import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;




@Service
public class LimitesGeneralesServicioApiRestImpl implements ILimitesGeneralesServiceApirest{

	private static final Logger LOGGER = LogManager.getLogger(LimitesGeneralesServicioApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio LimitesGenerales";
	
	@Autowired 
	private Mapper mapper;
	
	private static final String LIMITESGENERALESSERVICELISTAI = "[==== INICIO Lista LimitesGenerales Consultas - Service ====]";
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String LIMITESGENERALESSERVICELISTAF = "[==== FIN Lista LimitesGenerales Consultas - Service ====]";
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
	private int connectTimeout;
	
	private static final String LIMITESGENERALESSERVICEBUSCARI = "[==== INICIO Buscar LimitesGenerales Consultas - Service ====]";
	
	@Value("${${app.ambiente}"+".SocketTimeout}")
	private int socketTimeout;
	
	private static final String LIMITESGENERALESSERVICEBUSCARF = "[==== FIN Buscar LimitesGenerales Consultas - Service ====]";
	
	@Value("${${app.ambiente}"+".limitesGenerales.urlConsulta}")
	private String urlConsulta;
	
	private static final String LIMITESGENERALESSERVICEACTUALIZARI = "[==== INICIO Actualizar LimitesGenerales - Service ====]";
	
	@Value("${${app.ambiente}"+".limitesGenerales.urlActualizar}")
	private String urlActualizar;
	
	private static final String LIMITESGENERALESSERVICEACTUALIZARF = "[==== FIN Actualizar LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESSERVICECREARI = "[==== INICIO Crear LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESSERVICECREARF = "[==== FIN Crear LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALES = "limitesGenerales";
	
	private static final String INDEX = "index";
	
	private static final String DETALLE = "detalle";
	
	private static final String EDIT = "edit";
	
	private static final String GUARDAR = "guardar";
	
	private static final String SAVE = "save";
	
	private static final String ACTIVAR = "Activar";
	
	private static final String DESACTIVAR = "Desactivar";
	
	private static final String SEARCH = "search"; 
	
	private static final String LIMITESGENERALESQUERY =" LimitesGenerales:[codMoneda=";
	
	public WSRequest getWSRequest() {
	   WSRequest wsrequest = new WSRequest();
 	   wsrequest.setConnectTimeout(connectTimeout);
	   wsrequest.setContenType("application/json");
	   wsrequest.setSocketTimeout(socketTimeout);
	   return wsrequest;
	}

	public WSResponse getRetornoPost(WSRequest wsrequest, LimiteRequest limiteRequest, String url) {
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(url);	
		retorno = wsService.post(wsrequest);
		return retorno;
	}
	
	public WSResponse getRetornoPut(WSRequest wsrequest, LimiteRequest limiteRequest, String url) {
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(url);	
		retorno = wsService.put(wsrequest);
		return retorno;
	}
	
	
	@Override
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICELISTAF);
				return respuesta2xxListaLimitesGenerales(retorno);
			}else {
				throw new CustomException(respuesta4xxListaLimitesGenerales(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesGenerales> respuesta2xxListaLimitesGenerales(WSResponse retorno){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			return limiteResponse.getLimites();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaLimitesGenerales(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICEBUSCARF);
				return respuesta2xxbuscarLimitesGenerales(retorno);
			}else {
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public LimitesGenerales respuesta2xxbuscarLimitesGenerales(WSResponse retorno){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimites().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
        
	}

	public String respuesta4xxbuscarLimitesGenerales(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public String actualizar(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, limiteRequest, urlActualizar);
			if(retorno.isExitoso()) {
				if(retorno.getStatus() == 200) {
					LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARF);
					return respuesta2xxActualizarCrear(retorno);
				}else {
					throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
				}
			}else {
				LOGGER.error(ERRORMICROCONEXION);
				throw new CustomException(ERRORMICROCONEXION);
			}	
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	
	@Override
	public String crear(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	
	
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					LIMITESGENERALES, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaLimitesGenerales(String accion, boolean resultado, String codRespuesta, LimiteRequest limiteRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAI);
			
			if(accion.equals(DETALLE) || accion.equals(EDIT) || accion.equals(ACTIVAR) || accion.equals(DESACTIVAR)) {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						LIMITESGENERALES, accion, codRespuesta, resultado, respuesta+LIMITESGENERALESQUERY+limiteRequest.getLimite().getCodMoneda()+"], "
								+ "[tipoTransaccion="+limiteRequest.getLimite().getTipoTransaccion()+"], [tipoCliente="+limiteRequest.getLimite().getTipoCliente()+"]", request.getRemoteAddr());
			}else {
				if(accion.equals(GUARDAR)||accion.equals(SAVE)) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							LIMITESGENERALES, accion, codRespuesta, resultado, respuesta+LIMITESGENERALESQUERY+limiteRequest.getLimite().getCodMoneda()+"], "
							+ "[tipoTransaccion="+limiteRequest.getLimite().getTipoTransaccion()+"], [tipoCliente="+limiteRequest.getLimite().getTipoCliente()+"], "
							+ "[montoMin="+limiteRequest.getLimite().getMontoMin()+"], [montoMax="+limiteRequest.getLimite().getMontoMax()+"], "
							+ "[montoDiario="+limiteRequest.getLimite().getMontoDiario()+"], [montoMensual="+limiteRequest.getLimite().getMontoMensual()+"], "
							+ "[montoBanco="+limiteRequest.getLimite().getMontoBanco()+"], [montoTope="+limiteRequest.getLimite().getMontoTope()+"]", request.getRemoteAddr());
				}
			}
			
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void guardarAuditoriaLimitesGeneralesSearch(String accion, boolean resultado, String codRespuesta, LimiteRequest limiteRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAI);
			if(limiteRequest.getLimite().getCodMoneda() != null) {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
				LIMITESGENERALES, accion, codRespuesta, resultado, respuesta+LIMITESGENERALESQUERY+limiteRequest.getLimite().getCodMoneda().toUpperCase()+"] ", request.getRemoteAddr());
			}else {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
				LIMITESGENERALES, accion, codRespuesta, resultado, respuesta+LIMITESGENERALESQUERY, request.getRemoteAddr());
			}
			LOGGER.info(LIMITESGENERALESFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}


	@Override
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICELISTAF);
				return respuesta2xxListaLimitesGenerales(retorno, accion, limiteRequest, request);
			}else {
				throw new CustomException(respuesta4xxListaLimitesGenerales(retorno, accion, limiteRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<LimitesGenerales> respuesta2xxListaLimitesGenerales(WSResponse retorno, String accion, LimiteRequest limiteRequest,
			HttpServletRequest request){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, true, limiteResponse.getResultado().getCodigo(),  limiteResponse.getResultado().getDescripcion(), request);
			}else {
				if(accion.equals(SEARCH)) {
					guardarAuditoriaLimitesGeneralesSearch(accion, true, limiteResponse.getResultado().getCodigo(), limiteRequest, limiteResponse.getResultado().getDescripcion(),request);
				}
			}
			
			return limiteResponse.getLimites();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), request);
			}else {
				if(accion.equals(SEARCH)) {
					guardarAuditoriaLimitesGeneralesSearch(accion, false, String.valueOf(retorno.getStatus()), limiteRequest, retorno.getBody(),request);
				}
			}		
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaLimitesGenerales(WSResponse retorno, String accion, LimiteRequest limiteRequest,
			HttpServletRequest request){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, resultado.getCodigo(),  resultado.getDescripcion(), request);
			}else {
				if(accion.equals(SEARCH)) {
					guardarAuditoriaLimitesGeneralesSearch(accion, false, resultado.getCodigo(), limiteRequest, resultado.getDescripcion(),request);
				}
			}	
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			if(accion.equals(INDEX)) {
				guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()),  retorno.getBody(), request);
			}else {
				if(accion.equals(SEARCH)) {
					guardarAuditoriaLimitesGeneralesSearch(accion, false, String.valueOf(retorno.getStatus()), limiteRequest, retorno.getBody(),request);
				}
			}	
			return null;
		}
	}




	@Override
	public String actualizar(LimiteRequest limiteRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, limiteRequest, urlActualizar);
			if(retorno.isExitoso()) {
				if(retorno.getStatus() == 200) {
					LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARF);
					return respuesta2xxActualizarCrear(retorno,accion, limiteRequest, request);
				}else {
					throw new CustomException(respuesta4xxActualizarCrear(retorno,accion, limiteRequest, request));
				}
			}else {
				LOGGER.error(ERRORMICROCONEXION);
				throw new CustomException(ERRORMICROCONEXION);
			}
	}
	
	public String respuesta2xxActualizarCrear(WSResponse retorno, String accion, LimiteRequest limiteRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaLimitesGenerales(accion, true, response.getResultado().getCodigo(), limiteRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaLimitesGenerales(accion, false, String.valueOf(retorno.getStatus()), limiteRequest, retorno.getBody(), request);
			return null;
		}
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno, String accion, LimiteRequest limiteRequest, HttpServletRequest request){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaLimitesGenerales(accion, false, response.getResultado().getCodigo(), limiteRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaLimitesGenerales(accion, false, String.valueOf(retorno.getStatus()), limiteRequest, retorno.getBody(), request);
			return null;
		}
	}




	@Override
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICEBUSCARF);
				return respuesta2xxbuscarLimitesGenerales(retorno, accion, limiteRequest, request);
			}else {
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno, accion, limiteRequest, request));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public LimitesGenerales respuesta2xxbuscarLimitesGenerales(WSResponse retorno, String accion, LimiteRequest limiteRequest, HttpServletRequest request){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			guardarAuditoriaLimitesGenerales(accion, true, limiteResponse.getResultado().getCodigo(), limiteRequest, limiteResponse.getResultado().getDescripcion(), request);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimites().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
        
	}

	public String respuesta4xxbuscarLimitesGenerales(WSResponse retorno, String accion, LimiteRequest limiteRequest, HttpServletRequest request){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaLimitesGenerales(accion, false, response.getResultado().getCodigo(), limiteRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaLimitesGenerales(accion, false, String.valueOf(retorno.getStatus()), limiteRequest, retorno.getBody(), request);
			return null;
		}
	}




	@Override
	public String crear(LimiteRequest limiteRequest, String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, limiteRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICECREARF);
				return respuesta2xxActualizarCrear(retorno,accion, limiteRequest, request);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno,accion, limiteRequest, request));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
}
