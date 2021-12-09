package com.bancoexterior.app.cce.controller;


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
import com.bancoexterior.app.cce.dto.CceMotivoRechazoDto;
import com.bancoexterior.app.cce.model.CceMotivoRechazo;
import com.bancoexterior.app.cce.service.ICceMotivoRechazoService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/motivoRechazo")
public class CceMotivoRechazoController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceMotivoRechazoController.class);
	
	@Autowired
	private ICceMotivoRechazoService service;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.motivoRechazo.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String MOTIVORECHAZOCONTROLLERINDEXI = "[==== INICIO Index MotivoRechazo Consultas - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERINDEXF = "[==== FIN Index MotivoRechazo Consultas - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MENSAJECODIGOEXISTE = "El codigo a insertar ya existe.";
	
	private static final String MOTIVORECHAZOCONTROLLERFORMI = "[==== INICIO Form MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERFORMF = "[==== FIN Form MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERSAVEI = "[==== INICIO Save MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERSAVEF = "[==== FIN Save MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERGUARDARI = "[==== INICIO Guardar MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLERGUARDARF = "[==== FIN Guardar MotivoRechazo - Controller ====]";
	
	private static final String URLFORMMOTIVORECHAZO = "cce/motivoRechazo/formMotivoRechazo";
	
	private static final String URLFORMEDITMOTIVORECHAZO = "cce/motivoRechazo/formEditMotivoRechazo";
	
	private static final String URLINDEX = "cce/motivoRechazo/listaMotivoRechazo";
	
	private static final String REDIRECTINDEX = "redirect:/motivoRechazo/index";
	
	private static final String MOTIVORECHAZOCONTROLLEREDITI = "[==== INICIO Edit MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOCONTROLLEREDITF = "[==== FIN Edit MotivoRechazo - Controller ====]";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";

	private static final String MOTIVORECHAZO = "motivoRechazo";
	
	private static final String MOTIVORECHAZOTITULO = "CCE - Códigos Rechazo BCV";
	
	private static final String MOTIVORECHAZOFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  MotivoRechazo - Controller ====]";
	
	private static final String MOTIVORECHAZOFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  MotivoRechazo - Controller ====]";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operación Fallida.";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String DESCRIPCION = "descripcion";
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MOTIVORECHAZOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceMotivoRechazo> listaCceMotivoRechazo = service.findAll();
		if(listaCceMotivoRechazo.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}
		
		model.addAttribute("listaCceMotivoRechazo", listaCceMotivoRechazo); 
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(MOTIVORECHAZOCONTROLLERINDEXF);
		return URLINDEX;
	}	
	
	@GetMapping("/formMotivoRechazo")
	public String formMotivoRechazo(Model model, CceMotivoRechazoDto cceMotivoRechazoDto, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MOTIVORECHAZOCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		
		cceMotivoRechazoDto.setAplicaComisionCastigo("N");
		guardarAuditoria("formMotivoRechazo", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(MOTIVORECHAZOCONTROLLERFORMF);
		return URLFORMMOTIVORECHAZO;
		
	}
	
	@PostMapping("/save")
	public String save(@Valid  CceMotivoRechazoDto cceMotivoRechazoDto, BindingResult resultSMT,
			Model modelSMT, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MOTIVORECHAZOCONTROLLERSAVEI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<String> listaError = new ArrayList<>();
		if (resultSMT.hasErrors()) {
			for (ObjectError error : resultSMT.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add(error.getDefaultMessage());
			}
			modelSMT.addAttribute(LISTAERROR, listaError);
			modelSMT.addAttribute(DESCRIPCION, cceMotivoRechazoDto.getDescripcion());
			return URLFORMMOTIVORECHAZO;
		}
		
		CceMotivoRechazoDto cceMotivoRechazoDtoBuscar = service.findById(cceMotivoRechazoDto.getCodigo());
		LOGGER.info(cceMotivoRechazoDtoBuscar);
		if(cceMotivoRechazoDtoBuscar == null) {
			service.save(cceMotivoRechazoDto);
			guardarAuditoriaMotivoRechazo("save", true, "0000", MENSAJEOPERACIONEXITOSA, cceMotivoRechazoDto, request);
			LOGGER.info(MOTIVORECHAZOCONTROLLERSAVEF);
			return REDIRECTINDEX;
		}else {
			modelSMT.addAttribute(DESCRIPCION, cceMotivoRechazoDto.getDescripcion());
			modelSMT.addAttribute(MENSAJEERROR, MENSAJECODIGOEXISTE);
			guardarAuditoriaMotivoRechazo("save", false, "0001", MENSAJECODIGOEXISTE, cceMotivoRechazoDto, request);
			LOGGER.info(MOTIVORECHAZOCONTROLLERSAVEF);
			return URLFORMMOTIVORECHAZO;
		}
		
		
	}
	
	@GetMapping("/edit")
	public String edit(@RequestParam("id") String codigo, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MOTIVORECHAZOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceMotivoRechazoDto cceMotivoRechazoEdit = service.findById(codigo);
		if(cceMotivoRechazoEdit != null) {
			model.addAttribute("cceMotivoRechazoDto", cceMotivoRechazoEdit);
			model.addAttribute(DESCRIPCION, cceMotivoRechazoEdit.getDescripcion());
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, codigo, request);
			LOGGER.info(MOTIVORECHAZOCONTROLLEREDITF);
			return URLFORMEDITMOTIVORECHAZO;
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, codigo, request);
			LOGGER.info(MOTIVORECHAZOCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
	}
	
	@PostMapping("/guardar")
	public String guardar(@Valid  CceMotivoRechazoDto cceMotivoRechazoDto, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MOTIVORECHAZOCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<String> listaError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add(error.getDefaultMessage());
			}
			model.addAttribute(LISTAERROR, listaError);
			model.addAttribute(DESCRIPCION, cceMotivoRechazoDto.getDescripcion());
			return URLFORMEDITMOTIVORECHAZO;
		}
		
		service.updateMotivoRechazo(cceMotivoRechazoDto.getDescripcion(), cceMotivoRechazoDto.getAplicaComisionCastigo(), cceMotivoRechazoDto.getCodigo());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaMotivoRechazo("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceMotivoRechazoDto, request);
		LOGGER.info(MOTIVORECHAZOCONTROLLERGUARDARF);
		return REDIRECTINDEX;
	}	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = MOTIVORECHAZOTITULO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MOTIVORECHAZO, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, String codigo, HttpServletRequest request) {
		try {
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MOTIVORECHAZO, accion, codRespuesta, resultado, respuesta+" MotivoRechazo:[codigo="+codigo+"]", request.getRemoteAddr());
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaMotivoRechazo(String accion, boolean resultado, String codRespuesta,  String respuesta, CceMotivoRechazoDto cceMotivoRechazoDto, HttpServletRequest request) {
		try {
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MOTIVORECHAZO, accion, codRespuesta, resultado, respuesta+" MotivoRechazo:[codigo="+cceMotivoRechazoDto.getCodigo()+""
							+ ", descripcion="+cceMotivoRechazoDto.getDescripcion()+", aplicaComisionCastigo="+cceMotivoRechazoDto.getAplicaComisionCastigo()+"]", request.getRemoteAddr());
			LOGGER.info(MOTIVORECHAZOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
}
