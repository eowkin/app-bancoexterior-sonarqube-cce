package com.bancoexterior.app.cce.service;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import com.bancoexterior.app.cce.dto.TransaccionRequest;
import com.bancoexterior.app.cce.dto.TransaccionResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

@Service
public class TransaccionServiceImpl implements ITransaccionService{

	private static final Logger LOGGER = LogManager.getLogger(TransaccionServiceImpl.class);
	
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
	
	@Value("${${app.ambiente}"+".transacciones.urlProcesar}")
	private String urlConsulta;  
	
	private static final String TRANSACCIONSERVICEPROCESARI = "[==== INICIO Procesar TransaccionService - Service ====]";
	
	private static final String TRANSACCIONSERVICEPROCESARF = "[==== FIN Procesar TransaccionService - Service ====]";
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Transaccion";
	
	private static final String TRANSACCIONFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  Transaccion - Controller ====]";
	
	private static final String TRANSACCIONFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  Transaccion - Controller ====]";
	
	private static final String TRANSACCION = "cceTransaccionManual";
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	
	public WSResponse getRetornoPost(WSRequest wsrequest, TransaccionRequest transaccionRequest, String url) {
    	WSResponse retorno;
		String transaccionRequestJSON;
		transaccionRequestJSON = new Gson().toJson(transaccionRequest);
		wsrequest.setBody(transaccionRequestJSON);
		wsrequest.setUrl(url);
		retorno = wsService.post(wsrequest);
		return retorno;
    }
	
	@Override
	public TransaccionResponse procesar(TransaccionRequest transaccionRequest) throws CustomException {
		LOGGER.info(TRANSACCIONSERVICEPROCESARI);
		return null;
	}


	@Override
	public TransaccionResponse procesar(TransaccionRequest transaccionRequest, String accion,
			HttpServletRequest request) throws CustomException {
		LOGGER.info(TRANSACCIONSERVICEPROCESARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		retorno = getRetornoPost(wsrequest, transaccionRequest, urlConsulta);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(TRANSACCIONSERVICEPROCESARF);
				return respuesta2xxProcesar(retorno, accion, transaccionRequest, request);
			}else {
				if(retorno.getStatus() == 502 || retorno.getStatus() == 503) {
					LOGGER.error(ERRORMICROCONEXION);
					throw new CustomException(ERRORMICROCONEXION);
				}else {
					throw new CustomException(respuesta4xxProcesar(retorno, accion, transaccionRequest, request));
				}	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public TransaccionResponse respuesta2xxProcesar(WSResponse retorno, String accion, TransaccionRequest transaccionRequest, HttpServletRequest request) {
		try {
			TransaccionResponse transaccionResponse = mapper.jsonToClass(retorno.getBody(), TransaccionResponse.class);
			guardarAuditoriaTransaccion(accion, true, transaccionResponse.getResultado().getCodigo(),  transaccionResponse.getResultado().getDescripcion(), transaccionRequest, request);
			return  mapper.jsonToClass(retorno.getBody(), TransaccionResponse.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTransaccion(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), transaccionRequest, request);
			return null;
		}
			
	}
	
	public String respuesta4xxProcesar(WSResponse retorno, String accion, TransaccionRequest transaccionRequest, HttpServletRequest request) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			guardarAuditoriaTransaccion(accion, true, resultado.getCodigo(),  resultado.getDescripcion(), transaccionRequest, request);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTransaccion(accion, false, String.valueOf(retorno.getStatus()), retorno.getBody(), transaccionRequest, request);	
			return null;
		}
		
	}
	
	public void guardarAuditoriaTransaccion(String accion, boolean resultado, String codRespuesta,  String respuesta, TransaccionRequest transaccionRequest, HttpServletRequest request) {
		try {
			LOGGER.info(TRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					TRANSACCION, accion, codRespuesta, resultado, respuesta+" Transaccion:[idSesion="+transaccionRequest.getIdSesion()+""
							+ ", idUsuario="+transaccionRequest.getIdUsuario()+", codigoOperacion="+transaccionRequest.getCodigoOperacion()+""
							+ ", cuentaDesde="+transaccionRequest.getCuentaDesde()+", cuentaHasta="+transaccionRequest.getCuentaHasta()+""
							+ ", referencia="+transaccionRequest.getReferencia()+", montoTransaccion="+transaccionRequest.getMontoTransaccion()+""
							+ ", cedulaRif="+transaccionRequest.getCedulaRif()+", nombreBeneneficiario="+transaccionRequest.getNombreBeneneficiario()+""
							+ ", cedulaRifBeneficiario="+transaccionRequest.getCedulaRifBeneficiario()+", usuarioCanal="+transaccionRequest.getUsuarioCanal()+""
							+ ", codigoTransaccion="+transaccionRequest.getCodigoTransaccion()+", canal="+transaccionRequest.getCanal()+""
							+ ", NombreBancoBeneneficiario="+transaccionRequest.getNombreBancoBeneneficiario()+""
							+ ", descripcion="+transaccionRequest.getDescripcion()+"]", request.getRemoteAddr());
			LOGGER.info(TRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
}
