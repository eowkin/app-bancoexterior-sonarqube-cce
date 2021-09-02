package com.bancoexterior.app.convenio.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;


@Service
public class TasaServiceApiRestImpl implements ITasaServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(TasaServiceApiRestImpl.class);
	
	@Autowired 
	private Mapper mapper;
	
	@Autowired
	private IWSService wsService;
	
	@Autowired
	private IAuditoriaService auditoriaService;
    
	@Value("${${app.ambiente}"+".tasa.urlActualizar}")
	private String urlActualizar;
	 
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".tasa.urlConsulta}")
    private String urlConsulta;
    
   
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio tasa";
    
    private static final String TASASERVICELISTATASASI = "[==== INICIO Lista Tasas Consultas - Service ====]";
	
	private static final String TASASERVICELISTATASASF = "[==== FIN Lista Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEBUSACRTASAI = "[==== INICIO Buscar Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEBUSCARTASAF = "[==== FIN Buscar Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEACTUALIZARTASAI = "[==== INICIO Actualizar Tasas - Service ====]";
	
	private static final String TASASERVICEACTUALIZARTASAF = "[==== FIN Actualizar Tasas - Service ====]";
	
	private static final String TASASERVICECREARTASAI = "[==== INICIO Crear Tasas - Service ====]";
	
	private static final String TASASERVICECREARTASAF = "[==== FIN Crear Tasas - Service ====]";
	
	private static final String TASASERVICEELIMINARTASAI = "[==== INICIO Eliminar Tasas - Service ====]";
	
	private static final String TASASERVICEELIMINARTASAF = "[==== FIN Eliminar Tasas - Service ====]";
	
	private static final String TASAFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  Tasa - Controller ====]";
	
	private static final String TASAFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  Tasa - Controller ====]";
	
	private static final String TASAS = "Tasas";
	
	private static final String EDIT = "edit";
	
	private static final String GUARDAR = "guardar";
	
	private static final String SAVE = "save";
	
	private static final String TASASCODMONEDAORIGEN = " Tasas:[codMonedaOrigen=";
	
	private static final String CODMONEDADESTINOQUERY = "[codMonedaDestino=";
	
	private static final String TIPOOPERACIONQUERY = "], [tipoOperacion=";
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	public WSResponse getRetornoPost(WSRequest wsrequest, TasaRequest tasaRequest, String url) {
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(url);
		retorno = wsService.post(wsrequest);
		
		return retorno;
	}

	
	public WSResponse getRetornoPut(WSRequest wsrequest, TasaRequest tasaRequest) {
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		return retorno;
	}
	
	@Override
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException {
		LOGGER.info(TASASERVICELISTATASASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICELISTATASASF);
				return respuesta2xxListaTasa(retorno);
			}else {
				String respuesta4xxListaTasas = respuesta4xxListaTasas(retorno);
				throw new CustomException(respuesta4xxListaTasas);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Tasa> respuesta2xxListaTasa(WSResponse retorno){
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			return tasaResponse.getTasa();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxListaTasas(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public List<Tasa> listaTasas(TasaRequest tasaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(TASASERVICELISTATASASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICELISTATASASF);
				return respuesta2xxListaTasa(retorno, accion, request);
			}else {
				String respuesta4xxListaTasas =  respuesta4xxListaTasas(retorno, accion, request);
				throw new CustomException(respuesta4xxListaTasas);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	public List<Tasa> respuesta2xxListaTasa(WSResponse retorno,  String accion, HttpServletRequest request){
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			guardarAuditoria(accion, true, tasaResponse.getResultado().getCodigo(),  tasaResponse.getResultado().getDescripcion(), request);
			return tasaResponse.getTasa();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxListaTasas(WSResponse retorno, String accion, HttpServletRequest request) {
		try {
			
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoria(accion, false, resultado.getCodigo(),  resultado.getDescripcion(), request);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), request);
			return null;
		}
	}
	
	@Override
	public Tasa buscarTasa(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(TASASERVICEBUSACRTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICEBUSCARTASAF);
				return respuesta2xxBuscarTasa(retorno, accion, tasaRequest, request);
			}else {
				String respuesta4xxBuscarTasa = respuesta4xxBuscarTasa(retorno, accion, tasaRequest, request);
				throw new CustomException(respuesta4xxBuscarTasa);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public Tasa respuesta2xxBuscarTasa(WSResponse retorno, String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			guardarAuditoriaTasa(accion, true, tasaResponse.getResultado().getCodigo(), tasaRequest, tasaResponse.getResultado().getDescripcion(), request);
			if(tasaResponse.getResultado().getCodigo().equals("0000")){
	        	return tasaResponse.getTasa().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}
	
	public String respuesta4xxBuscarTasa(WSResponse retorno, String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTasa(accion, false, resultado.getCodigo(), tasaRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasa(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	

	@Override
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException {
		LOGGER.info(TASASERVICEBUSACRTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICEBUSCARTASAF);
				return respuesta2xxBuscarTasa(retorno);
			}else {
				String respuesta4xxListaTasas = respuesta4xxListaTasas(retorno);
				throw new CustomException(respuesta4xxListaTasas);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}

	}

	public Tasa respuesta2xxBuscarTasa(WSResponse retorno) {
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			if(tasaResponse.getResultado().getCodigo().equals("0000")){
	        	return tasaResponse.getTasa().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}
	
	@Override
	public String actualizar(TasaRequest tasaRequest, String accion, HttpServletRequest request)
			throws CustomException {
		LOGGER.info(TASASERVICEACTUALIZARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, tasaRequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICEACTUALIZARTASAF);
				return respuesta2xxActualizarCrear(retorno,accion, tasaRequest, request);
			}else {
				String respuesta4xxActualizar = respuesta4xxActualizar(retorno,accion, tasaRequest, request);
				throw new CustomException(respuesta4xxActualizar);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public String respuesta2xxActualizarCrear(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTasa(accion, true, resultado.getCodigo(), tasaRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasa(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizar(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaTasa(accion, false, response.getResultado().getCodigo(), tasaRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasa(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	

	@Override
	public String actualizar(TasaRequest tasaRequest) throws CustomException {
		LOGGER.info(TASASERVICEACTUALIZARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPut(wsrequest, tasaRequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICEACTUALIZARTASAF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				String respuesta4xxActualizar = respuesta4xxActualizar(retorno);
				throw new CustomException(respuesta4xxActualizar);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}	
	}
	
	public String respuesta4xxActualizar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
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
	
	@Override
	public String crear(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(TASASERVICECREARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICECREARTASAF);
				return respuesta2xxActualizarCrear(retorno,accion, tasaRequest, request);
				
			}else {
				if (retorno.getStatus() == 422) {
					String respuesta422Crear = respuesta422Crear(retorno,accion, tasaRequest, request);
					throw new CustomException(respuesta422Crear);
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						String respuesta400Crear = respuesta400Crear(retorno, accion, tasaRequest, request);
						throw new CustomException(respuesta400Crear);
					}
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	
	public String respuesta422Crear(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			guardarAuditoriaTasa(accion, false, response.getResultado().getCodigo(), tasaRequest, response.getResultado().getDescripcion(), request);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasa(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	
	public String respuesta400Crear(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTasa(accion, false, resultado.getCodigo(), tasaRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasa(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	
	
	@Override
	public String crear(TasaRequest tasaRequest) throws CustomException {
		LOGGER.info(TASASERVICECREARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, tasaRequest, urlActualizar);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICECREARTASAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				if (retorno.getStatus() == 422) {
					String respuesta422 = respuesta422Crear(retorno);
					throw new CustomException(respuesta422);
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						String respuesta400 = respuesta400Crear(retorno);
						throw new CustomException(respuesta400);
					}
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	public String respuesta422Crear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta400Crear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String eliminar(TasaRequest tasaRequest, String accion, HttpServletRequest request) throws CustomException {
		LOGGER.info(TASASERVICEELIMINARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.delete(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TASASERVICEELIMINARTASAF);
				return respuesta2xxActualizarEliminar(retorno,accion, tasaRequest, request);
				
			}else {
				String respuesta400Eliminar = respuesta400Eliminar(retorno, accion, tasaRequest, request);
				throw new CustomException(respuesta400Eliminar);
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
		
	}

	public String respuesta2xxActualizarEliminar(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTasaEliminar(accion, true, resultado.getCodigo(), tasaRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasaEliminar(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	
	
	public String respuesta400Eliminar(WSResponse retorno,  String accion, TasaRequest tasaRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTasaEliminar(accion, false, resultado.getCodigo(), tasaRequest, resultado.getDescripcion(), request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTasaEliminar(accion, false, String.valueOf(retorno.getStatus()), tasaRequest, retorno.getBody(), request);
			return null;
		}
	}
	
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(TASAFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					TASAS, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(TASAFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	public void guardarAuditoriaTasa(String accion, boolean resultado, String codRespuesta, TasaRequest tasaRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(TASAFUNCIONAUDITORIAI);
			
			if(accion.equals(EDIT)) {
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TASAS, accion, codRespuesta, resultado, respuesta+TASASCODMONEDAORIGEN+tasaRequest.getTasa().getCodMonedaOrigen()+"], "
								+ CODMONEDADESTINOQUERY+tasaRequest.getTasa().getCodMonedaDestino()+TIPOOPERACIONQUERY+tasaRequest.getTasa().getTipoOperacion()+"]", request.getRemoteAddr());
			}else {
				if(accion.equals(GUARDAR) || accion.equals(SAVE)) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							TASAS, accion, codRespuesta, resultado, respuesta+TASASCODMONEDAORIGEN+tasaRequest.getTasa().getCodMonedaOrigen()+"], "
							+ CODMONEDADESTINOQUERY+tasaRequest.getTasa().getCodMonedaDestino()+TIPOOPERACIONQUERY+tasaRequest.getTasa().getTipoOperacion()+"], "
							+ "[montoTasaCompra="+tasaRequest.getTasa().getMontoTasaCompra()+"], [montoTasaVenta="+tasaRequest.getTasa().getMontoTasaVenta()+"]", request.getRemoteAddr());
				}
			}
			
			LOGGER.info(TASAFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void guardarAuditoriaTasaEliminar(String accion, boolean resultado, String codRespuesta, TasaRequest tasaRequest, String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(TASAFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							TASAS, accion, codRespuesta, resultado, respuesta+TASASCODMONEDAORIGEN+tasaRequest.getTasa().getCodMonedaOrigen()+"], "
							+ CODMONEDADESTINOQUERY+tasaRequest.getTasa().getCodMonedaDestino()+TIPOOPERACIONQUERY+tasaRequest.getTasa().getTipoOperacion()+"]", request.getRemoteAddr());
			LOGGER.info(TASAFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	

	

	

}
