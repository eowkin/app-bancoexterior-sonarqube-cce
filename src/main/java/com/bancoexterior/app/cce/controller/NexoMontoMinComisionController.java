package com.bancoexterior.app.cce.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.cce.dto.DatosMontoMinComision;
import com.bancoexterior.app.cce.dto.Filtros;
import com.bancoexterior.app.cce.dto.MontoMinComisionConsultaRequest;
import com.bancoexterior.app.cce.dto.MontoMinComisionRequest;
import com.bancoexterior.app.cce.model.NexoHistorialMontoMinComision;
import com.bancoexterior.app.cce.model.NexoMontoMinComision;
import com.bancoexterior.app.cce.service.INexoMontoMinComisionService;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/comisionNexoPersona")
public class NexoMontoMinComisionController {
	
	private static final Logger LOGGER = LogManager.getLogger(NexoMontoMinComisionController.class);
	
	@Autowired
	private INexoMontoMinComisionService nexoMontoMinComisionService; 
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	
	
	@Value("${${app.ambiente}"+".cce.comisionMinima.valorBD}")
    private int valorBD;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERINDEXI = "[==== INICIO Index NexoMontoMinimoComision Consultas - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERINDEXF = "[==== FIN Index NexoMontoMinimoComision Consultas - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERHISTORIALI = "[==== INICIO Historial NexoMontoMinimoComision Consultas - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERHISTORIALF = "[==== FIN Historial NexoMontoMinimoComision Consultas - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLEREDITI = "[==== INICIO Edit NexoMontoMinimoComision - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLEREDITF = "[==== FIN Edit NexoMontoMinimoComision - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERGUARDARI = "[==== INICIO Guardar NexoMontoMinimoComision - Controller ====]";
	
	private static final String NEXOMONTOMINIMOCOMISIONCONTROLLERGUARDARF = "[==== FIN Guardar NexoMontoMinimoComision - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeErrorNexo";
	
	private static final String URLINDEX = "cce/comisionNexoPersona/listaNexoMontoMinComision";
	
	private static final String URLHISTORIAL = "cce/comisionNexoPersona/listaHistorialNexoMontoMinComision";
	
	private static final String URLFORMEDITNEXOMONTOMINCOMISION = "cce/comisionNexoPersona/formEditNexoMontoMinComision";
	
	private static final String REDIRECTMONTOMINIMOCOMISIONINDEX = "redirect:/montoMinimoComision/index";
	
	private static final String LISTANEXOMONTOMINCOMISION = "listaNexoMontoMinComision";
	
	private static final String LISTANEXOHISTORIALMONTOMINCOMISION = "listaNexoHistorialMontoMinComision";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJE = "mensajeNexo";
	
	private static final String GUARDAR = "guardar";
	
	private static final String COMISIONNEXOPERSONATITULO = "CCE - Monto Tope Comisión Mínima  Crédito Inmediato";
	
	@GetMapping("index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<NexoMontoMinComision> listaNexoMontoMinComision = new ArrayList<>();
		MontoMinComisionConsultaRequest montoMinComisionConsultaRequest = getMontoMinComisionConsultaRequest();
		Filtros comision = new Filtros();
		comision.setId(0);
		montoMinComisionConsultaRequest.setComision(comision);
		
		try {
		
			listaNexoMontoMinComision = nexoMontoMinComisionService.listaNexoMontoMinComision(montoMinComisionConsultaRequest, "index", request);
			if(listaNexoMontoMinComision.isEmpty()) {
				model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			}else {
				convertirLista(listaNexoMontoMinComision);
			}
			
			model.addAttribute(LISTANEXOMONTOMINCOMISION, listaNexoMontoMinComision);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}	
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERINDEXF);
		return URLINDEX;
	}
	
	
	@GetMapping("/historial")
	public String historial(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERHISTORIALI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}

		List<NexoHistorialMontoMinComision> listaNexoHistorialMontoMinComision = new ArrayList<>();
		MontoMinComisionConsultaRequest montoMinComisionConsultaRequest = getMontoMinComisionConsultaRequest();
		Filtros comision = new Filtros();
		comision.setId(0);
		montoMinComisionConsultaRequest.setComision(comision);
		
		try {
			listaNexoHistorialMontoMinComision = nexoMontoMinComisionService.listaNexoHistorialMontoMinComision(montoMinComisionConsultaRequest, "historial", request);
			if(listaNexoHistorialMontoMinComision.isEmpty()) {
				model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			}else {
				convertirListaHistorial(listaNexoHistorialMontoMinComision);
			}
		
			model.addAttribute(LISTANEXOHISTORIALMONTOMINCOMISION, listaNexoHistorialMontoMinComision);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}		
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERHISTORIALF);
		return URLHISTORIAL;
		
	}
	
	
	@GetMapping("/edit")
	public String editMontoMinimoComision(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		NexoMontoMinComision nexoMontoMinComision = new NexoMontoMinComision();
		MontoMinComisionConsultaRequest montoMinComisionConsultaRequest = getMontoMinComisionConsultaRequest();
		Filtros comision = new Filtros();
		comision.setId(id);
		montoMinComisionConsultaRequest.setComision(comision);
		
		try {
			
			nexoMontoMinComision = nexoMontoMinComisionService.buscarNexoMontoMinComision(montoMinComisionConsultaRequest, "edit", request);
			if(nexoMontoMinComision != null) {
				nexoMontoMinComision.setMontoString(nexoMontoMinComision.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
				model.addAttribute("nexoMontoMinComision", nexoMontoMinComision);
				return URLFORMEDITNEXOMONTOMINCOMISION;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
				LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLEREDITF);
				return REDIRECTMONTOMINIMOCOMISIONINDEX;
			}
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTMONTOMINIMOCOMISIONINDEX;
		}	
		
		
		
	}	
	
	@PostMapping("/guardar")
	public String guardar(@Valid NexoMontoMinComision nexoMontoMinComision, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		if (result.hasErrors()) {
			setError(model, result);
			return URLFORMEDITNEXOMONTOMINCOMISION;
		}
		
		MontoMinComisionRequest montoMinComisionRequest = getMontoMinComisionRequest();
		DatosMontoMinComision datosMontoMinComision = new DatosMontoMinComision();
		datosMontoMinComision.setId(nexoMontoMinComision.getId());
		datosMontoMinComision.setMonto(new BigDecimal(nexoMontoMinComision.getMontoString()).setScale(2, RoundingMode.HALF_UP));
		datosMontoMinComision.setDescripcion(nexoMontoMinComision.getDescripcion());
		datosMontoMinComision.setUsuario(montoMinComisionRequest.getIdUsuarioDR());
		montoMinComisionRequest.setComision(datosMontoMinComision);
		
		
		List<String> listaError = new ArrayList<>();
		try {
			
			
			String respuesta = nexoMontoMinComisionService.actualizar(montoMinComisionRequest, GUARDAR, request);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(NEXOMONTOMINIMOCOMISIONCONTROLLERGUARDARF);
			return REDIRECTMONTOMINIMOCOMISIONINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return REDIRECTMONTOMINIMOCOMISIONINDEX;
		}
		
		
		
	}	
	
	public Model setError(Model model, BindingResult result) {
		List<String> listaError = new ArrayList<>();
		for (ObjectError error : result.getAllErrors()) {
			LOGGER.info(error.getDefaultMessage());
			listaError.add(error.getDefaultMessage());
		}
		model.addAttribute(LISTAERROR, listaError);
		return model;
	}
	
	public List<NexoMontoMinComision> convertirLista(List<NexoMontoMinComision> listaNexoMontoMinComision){
		for (NexoMontoMinComision nexoMontoMinComision : listaNexoMontoMinComision) {
			nexoMontoMinComision.setMontoString(libreriaUtil.formatNumber(nexoMontoMinComision.getMonto()));
		}
		
		return listaNexoMontoMinComision;
	}
	
	
	public List<NexoHistorialMontoMinComision> convertirListaHistorial(List<NexoHistorialMontoMinComision> listaNexoHistorialMontoMinComision){
		for (NexoHistorialMontoMinComision historialMontoMinComision : listaNexoHistorialMontoMinComision) {
			historialMontoMinComision.setMontoString(libreriaUtil.formatNumber(historialMontoMinComision.getMonto()));
		}
		
		return listaNexoHistorialMontoMinComision;
	}
	
	public MontoMinComisionConsultaRequest getMontoMinComisionConsultaRequest() {
		
		MontoMinComisionConsultaRequest montoMinComisionConsultaRequest = new MontoMinComisionConsultaRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		montoMinComisionConsultaRequest.setIdUsuarioMR(userName);
		montoMinComisionConsultaRequest.setIdSesionMR(libreriaUtil.obtenerIdSesion());
		montoMinComisionConsultaRequest.setCodUsuarioMR(userName);
		montoMinComisionConsultaRequest.setCanalCM(canal);
		return montoMinComisionConsultaRequest;
	}
	
	
	public MontoMinComisionRequest getMontoMinComisionRequest() {
		
		MontoMinComisionRequest montoMinComisionRequest = new MontoMinComisionRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		montoMinComisionRequest.setIdUsuarioDR(userName);
		montoMinComisionRequest.setIdSesionDR(libreriaUtil.obtenerIdSesion());
		montoMinComisionRequest.setCodUsuarioDR(userName);
		montoMinComisionRequest.setCanalDM(canal);
		return montoMinComisionRequest;
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = COMISIONNEXOPERSONATITULO;
		model.addAttribute("arrUri", arrUriP);
	}
}
