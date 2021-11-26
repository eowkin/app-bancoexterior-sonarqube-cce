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
import com.bancoexterior.app.cce.dto.CceMontoMinimoComisionDto;
import com.bancoexterior.app.cce.dto.Filtros;
import com.bancoexterior.app.cce.dto.MontoMinComisionConsultaRequest;
import com.bancoexterior.app.cce.model.CceHistorialMontoMinimoComision;
import com.bancoexterior.app.cce.model.CceMontoMinimoComision;
import com.bancoexterior.app.cce.model.NexoMontoMinComision;
import com.bancoexterior.app.cce.service.ICceHistorialMontoMinimoComisionService;
import com.bancoexterior.app.cce.service.ICceMontoMinimoComisionService;
import com.bancoexterior.app.cce.service.INexoMontoMinComisionService;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/montoMinimoComision")
public class CceMontoMinimoComisionController {

	private static final Logger LOGGER = LogManager.getLogger(CceMontoMinimoComisionController.class);
	
	@Autowired
	private ICceMontoMinimoComisionService service;
	
	@Autowired
	private ICceHistorialMontoMinimoComisionService serviceHistorial;
	
	@Autowired
	private INexoMontoMinComisionService nexoMontoMinComisionService;
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.comisionMinima.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERINDEXI = "[==== INICIO Index MontoMinimoComision Consultas - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERINDEXF = "[==== FIN Index MontoMinimoComision Consultas - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERHISTORIALI = "[==== INICIO Historial MontoMinimoComision Consultas - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERHISTORIALF = "[==== FIN Historial MontoMinimoComision Consultas - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEERRORNEXO = "mensajeErrorNexo";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLEREDITI = "[==== INICIO Edit MontoMinimoComision - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLEREDITF = "[==== FIN Edit MontoMinimoComision - Controller ====]";
	
	private static final String REDIRECTINDEX = "redirect:/montoMinimoComision/index";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERGUARDARI = "[==== INICIO Guardar MontoMinimoComision - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONCONTROLLERGUARDARF = "[==== FIN Guardar MontoMinimoComision - Controller ====]";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operacion Exitosa.";
	
	private static final String MONTOMINIMOCOMISIONFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  MontoMinimoComision - Controller ====]";
	
	private static final String MONTOMINIMOCOMISIONFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  MontoMinimoComision - Controller ====]";
	
	private static final String MONTOMINIMOCOMISION = "montoMinimoComision";
	
	private static final String MONTOMINIMOCOMISIONTITULO = "CCE - Monto Tope Comisión Mínima  Crédito Inmediato";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operacion Fallida.";
	
	private static final String LISTANEXOMONTOMINCOMISION = "listaNexoMontoMinComision";
	
	private static final String INDEX = "index";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		////------------INICIO Codigo NUevo de Nexo Comision-------------////
		List<NexoMontoMinComision> listaNexoMontoMinComision = new ArrayList<>();
		MontoMinComisionConsultaRequest montoMinComisionConsultaRequest = getMontoMinComisionConsultaRequest();
		Filtros comision = new Filtros();
		comision.setId(0);
		montoMinComisionConsultaRequest.setComision(comision);
		
		
	    ////------------FIN Codigo NUevo de Nexo Comision-------------////
		try {
			
			listaNexoMontoMinComision = nexoMontoMinComisionService.listaNexoMontoMinComision(montoMinComisionConsultaRequest, INDEX, request);
			if(listaNexoMontoMinComision.isEmpty()) {
				model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			}else {
				convertirListaNexo(listaNexoMontoMinComision);
			}
			
			model.addAttribute(LISTANEXOMONTOMINCOMISION, listaNexoMontoMinComision);
			
				
			List<CceMontoMinimoComision> listCceMontoMinimoComision = service.findAll();
			
			if(listCceMontoMinimoComision.isEmpty()) {
				model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			}else {
				convertirLista(listCceMontoMinimoComision);
			}
			guardarAuditoria(INDEX, true, "0000",  MENSAJEOPERACIONEXITOSA, request);
			model.addAttribute("listCceMontoMinimoComision", listCceMontoMinimoComision);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERRORNEXO, e.getMessage());
			List<CceMontoMinimoComision> listCceMontoMinimoComision = service.findAll();
			
			if(listCceMontoMinimoComision.isEmpty()) {
				model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			}else {
				convertirLista(listCceMontoMinimoComision);
			}
			guardarAuditoria(INDEX, true, "0000",  MENSAJEOPERACIONEXITOSA, request);
			model.addAttribute("listCceMontoMinimoComision", listCceMontoMinimoComision);
			
		}
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERINDEXF);
		return "cce/comisionMinima/listaMontosComisionMinima";
	}	
	
	@GetMapping("/historial")
	public String historial(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERHISTORIALI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceHistorialMontoMinimoComision> listCceHistorialMontoMinimoComision = serviceHistorial.findAll();
		
		if(listCceHistorialMontoMinimoComision.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}else {
			convertirListaHistorial(listCceHistorialMontoMinimoComision);
		}
		guardarAuditoria("historial", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		model.addAttribute("listCceHistorialMontoMinimoComision", listCceHistorialMontoMinimoComision);
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERHISTORIALF);
		return "cce/comisionMinima/listaHistorialMontosComisionMinima";
			
	}	
	
	@GetMapping("/edit")
	public String editMontoMinimoComision(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		CceMontoMinimoComisionDto cceMontoMinimoComisionEdit = service.findById(id);
		
		if(cceMontoMinimoComisionEdit != null) {
			cceMontoMinimoComisionEdit.setMontoString(cceMontoMinimoComisionEdit.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
			model.addAttribute("cceMontoMinimoComisionDto", cceMontoMinimoComisionEdit);
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(MONTOMINIMOCOMISIONCONTROLLEREDITF);
			return "cce/comisionMinima/formEditMontoComisionMinima";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(MONTOMINIMOCOMISIONCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}	
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceMontoMinimoComisionDto cceMontoMinimoComisionDto, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		if (result.hasErrors()) {
			setError(model, result);
			return "cce/comisionMinima/formEditMontoComisionMinima";
		}
		
		cceMontoMinimoComisionDto.setMonto(new BigDecimal(cceMontoMinimoComisionDto.getMontoString()).setScale(2, RoundingMode.HALF_UP));
		service.updateMontoMinimoComision(cceMontoMinimoComisionDto.getMonto().setScale(2, RoundingMode.HALF_UP), SecurityContextHolder.getContext().getAuthentication().getName(),
				cceMontoMinimoComisionDto.getTipoCliente(), cceMontoMinimoComisionDto.getId());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaMontoMinimoComision("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceMontoMinimoComisionDto, request);
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERGUARDARF);
		return REDIRECTINDEX;
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
	
	public List<NexoMontoMinComision> convertirListaNexo(List<NexoMontoMinComision> listaNexoMontoMinComision){
		for (NexoMontoMinComision nexoMontoMinComision : listaNexoMontoMinComision) {
			nexoMontoMinComision.setMontoString(libreriaUtil.formatNumber(nexoMontoMinComision.getMonto()));
		}
		
		return listaNexoMontoMinComision;
	}
	
	
	public List<CceMontoMinimoComision> convertirLista(List<CceMontoMinimoComision> listCceMontoMinimoComision){
		
		for (CceMontoMinimoComision cceMontoMinimoComision : listCceMontoMinimoComision) {
			cceMontoMinimoComision.setMontoString(libreriaUtil.formatNumber(cceMontoMinimoComision.getMonto()));
		}
		
		return listCceMontoMinimoComision;
	}
	
	public List<CceHistorialMontoMinimoComision> convertirListaHistorial(List<CceHistorialMontoMinimoComision> listCceHistorialMontoMinimoComision){
		
		for (CceHistorialMontoMinimoComision cceHistorialMontoMinimoComision : listCceHistorialMontoMinimoComision) {
			cceHistorialMontoMinimoComision.setMontoString(libreriaUtil.formatNumber(cceHistorialMontoMinimoComision.getMonto()));
		}
		
		return listCceHistorialMontoMinimoComision;
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
	
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = MONTOMINIMOCOMISIONTITULO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMINIMOCOMISION, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMINIMOCOMISION, accion, codRespuesta, resultado, respuesta+" MontoMinimoComision:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaMontoMinimoComision(String accion, boolean resultado, String codRespuesta,  String respuesta, CceMontoMinimoComisionDto cceMontoMinimoComisionDto, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMINIMOCOMISION, accion, codRespuesta, resultado, respuesta+" MontoMinimoComision:[id="+cceMontoMinimoComisionDto.getId()+""
							+ ", monto="+cceMontoMinimoComisionDto.getMonto()+", tipoCliente="+cceMontoMinimoComisionDto.getTipoCliente()+"]", request.getRemoteAddr());
			LOGGER.info(MONTOMINIMOCOMISIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
