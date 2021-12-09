package com.bancoexterior.app.cce.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.bancoexterior.app.cce.dto.CceFechaFeriadoBancarioDto;
import com.bancoexterior.app.cce.model.CceFechaFeriadoBancario;
import com.bancoexterior.app.cce.service.ICceFechaFeriadoBancarioService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/calendarioBancario")
public class CceFechaFeriadoBancarioController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceFechaFeriadoBancarioController.class);
	
	@Autowired
	private ICceFechaFeriadoBancarioService service;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.calendarioBancario.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String URLLISTAFECHAFERIADOBANCARIO = "cce/fechaFeriado/listaFechaFeriado";
	
	private static final String URLFORMFECHAFERIADOBANCARIO = "cce/fechaFeriado/formFechaFeriadoBancario";
	
	private static final String URLFORMFECHAFERIADOBANCARIOEDIT = "cce/fechaFeriado/formFechaFeriadoBancarioEdit";
	
	private static final String REDIRECTINDEX = "redirect:/calendarioBancario/index";
	
	private static final String LISTAFECHAFERIADOBANCARIO = "listaCceFechaFeriadoBancario";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERINDEXI = "[==== INICIO Index FechaFeriadoBAncario Consultas - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERINDEXF = "[==== FIN Index FechaFeriadoBAncario Consultas - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERFORMI = "[==== INICIO Form FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERFORMF = "[==== FIN Form FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERSAVEI = "[==== INICIO Save FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERSAVEF = "[==== FIN Save FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLEREDITI = "[==== INICIO Edit FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLEREDITF = "[==== FIN Edit FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERGUARDARI = "[==== INICIO Guardar FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOCONTROLLERGUARDARF = "[==== FIN Guardar FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  FechaFeriadoBancario - Controller ====]";
	
	private static final String FECHAFERIADOBANCARIOFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  FechaFeriadoBancario - Controller ====]";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONFALLO = "Operación fallida, ocurrio un error.";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";
	
	private static final String MENSAJEERRORFECHAFERIADO = "Fecha Feriado Inválida, debe ser igual o mayor al día actual.";
	
	private static final String DESCRIPCION = "descripcion";
	
	private static final String FORMATOFECHAINICIALIZAR =  "yyyy-MM-dd";
	
	private static final String CALENDARIOBANCARIOTITULO = "CCE-Calendario Bancario";
	
	private static final String CALENDARIOBANCARIO = "calendarioBancario";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		List<CceFechaFeriadoBancario>  listaCceFechaFeriadoBancario = service.listaFechasFeriado();
		
		if(listaCceFechaFeriadoBancario.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}
		
		model.addAttribute(LISTAFECHAFERIADOBANCARIO, listaCceFechaFeriadoBancario); 
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERINDEXF);
		return URLLISTAFECHAFERIADOBANCARIO;
	}
	
	@GetMapping("/formFechaFeriadoBancario")
	public String formFechaFeriadoBancario(Model model, CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		guardarAuditoria("formTipoTransaccion", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERFORMF);
		return URLFORMFECHAFERIADOBANCARIO;
	}
	
	@PostMapping("/save")
	public String save(@Valid CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto, BindingResult resultSFB, 
			Model modelSFB, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERSAVEI); 
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<String> listaError = new ArrayList<>();
		if (resultSFB.hasErrors()) {
			for (ObjectError error : resultSFB.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add(error.getDefaultMessage());
			}
			modelSFB.addAttribute(LISTAERROR, listaError);
			modelSFB.addAttribute(DESCRIPCION, cceFechaFeriadoBancarioDto.getDescripcion());
			return URLFORMFECHAFERIADOBANCARIO;
		}
		
		LOGGER.info(cceFechaFeriadoBancarioDto.getFechaFeriadoString());
		LOGGER.info(fechaHoy());
		asignarValores(cceFechaFeriadoBancarioDto);
		if(libreriaUtil.isFechaValidaDesdeHastaDate(fechaHoy(), cceFechaFeriadoBancarioDto.getFechaFeriado())) {
			
			CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDtoSave = service.save(cceFechaFeriadoBancarioDto);
			if(cceFechaFeriadoBancarioDtoSave != null) {
				guardarAuditoriaFechaFeriadoBancario("save", true, "0000", MENSAJEOPERACIONEXITOSA, cceFechaFeriadoBancarioDtoSave, request);
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
				guardarAuditoriaFechaFeriadoBancario("save", false, "0001",  MENSAJEOPERACIONFALLO, cceFechaFeriadoBancarioDto, request);
			}
			LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERSAVEF);
			return REDIRECTINDEX;
			
		}else {
			modelSFB.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAFERIADO);
			modelSFB.addAttribute(DESCRIPCION, cceFechaFeriadoBancarioDto.getDescripcion());
			return URLFORMFECHAFERIADOBANCARIO;
		}
			
		
	}	

	
	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDtoEdit = service.findById(id);
		if(cceFechaFeriadoBancarioDtoEdit != null) {
			cceFechaFeriadoBancarioDtoEdit.setFechaFeriadoString(obtenerFechaFormateada(cceFechaFeriadoBancarioDtoEdit.getFechaFeriado(), FORMATOFECHAINICIALIZAR));
			model.addAttribute("cceFechaFeriadoBancarioDto", cceFechaFeriadoBancarioDtoEdit);
			model.addAttribute(DESCRIPCION, cceFechaFeriadoBancarioDtoEdit.getDescripcion());
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(FECHAFERIADOBANCARIOCONTROLLEREDITF);
			return URLFORMFECHAFERIADOBANCARIOEDIT;
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLO+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(FECHAFERIADOBANCARIOCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
	}
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto, BindingResult resultGFB, 
			Model modelGFB, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERGUARDARI); 
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<String> listaError = new ArrayList<>();
		if (resultGFB.hasErrors()) {
			for (ObjectError error : resultGFB.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add(error.getDefaultMessage());
			}
			modelGFB.addAttribute(LISTAERROR, listaError);
			modelGFB.addAttribute(DESCRIPCION, cceFechaFeriadoBancarioDto.getDescripcion());
			return URLFORMFECHAFERIADOBANCARIOEDIT;
		}
		
		asignarValores(cceFechaFeriadoBancarioDto);
		if(libreriaUtil.isFechaValidaDesdeHastaDate(fechaHoy(), cceFechaFeriadoBancarioDto.getFechaFeriado())) {
			
			CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDtoGuardar = service.save(cceFechaFeriadoBancarioDto);
			if(cceFechaFeriadoBancarioDtoGuardar != null) {
				guardarAuditoriaFechaFeriadoBancario("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceFechaFeriadoBancarioDtoGuardar, request);
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
				guardarAuditoriaFechaFeriadoBancario("guardar", false, "0001",  MENSAJEOPERACIONFALLO, cceFechaFeriadoBancarioDto, request);
			}
			LOGGER.info(FECHAFERIADOBANCARIOCONTROLLERGUARDARF);
			return REDIRECTINDEX;
			
		}else {
			modelGFB.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAFERIADO);
			modelGFB.addAttribute(DESCRIPCION, cceFechaFeriadoBancarioDto.getDescripcion());
			return URLFORMFECHAFERIADOBANCARIOEDIT;
		}
			
		
	}	
	
	
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(FECHAFERIADOBANCARIOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDtoEliminar = service.findById(id);
		if(cceFechaFeriadoBancarioDtoEliminar != null) {
			service.delete(id);
			redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			guardarAuditoriaId("eliminar", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("eliminar", false, "0001",  MENSAJEOPERACIONFALLO+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(FECHAFERIADOBANCARIOCONTROLLEREDITF);
			
		}
		
		return REDIRECTINDEX;
	}
	
	public void asignarValores(CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		cceFechaFeriadoBancarioDto.setUsuario(userName.toLowerCase());
		cceFechaFeriadoBancarioDto.setFechaFeriado(libreriaUtil.getFechaDate(cceFechaFeriadoBancarioDto.getFechaFeriadoString()));
	}
	
	public Date fechaHoy() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	public String obtenerFechaFormateada(Date fecha, String formato) {
	    SimpleDateFormat sdf = new SimpleDateFormat(formato);
	    return sdf.format(fecha);
	}
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = CALENDARIOBANCARIOTITULO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CALENDARIOBANCARIO, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CALENDARIOBANCARIO, accion, codRespuesta, resultado, respuesta+" FechaFeriadoBancario:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaFechaFeriadoBancario(String accion, boolean resultado, String codRespuesta,  String respuesta, CceFechaFeriadoBancarioDto cceFechaFeriadoBancarioDto, HttpServletRequest request) {
		try {
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CALENDARIOBANCARIO, accion, codRespuesta, resultado, respuesta+" FechaFeriadoBancario:[descripcion="+cceFechaFeriadoBancarioDto.getDescripcion()+", "
							+ "fecha="+cceFechaFeriadoBancarioDto.getFechaFeriado()+"]", request.getRemoteAddr());
			LOGGER.info(FECHAFERIADOBANCARIOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
