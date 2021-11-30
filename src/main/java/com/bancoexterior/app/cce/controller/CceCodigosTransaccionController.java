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

import com.bancoexterior.app.cce.dto.CceCodigosTransaccionDto;
import com.bancoexterior.app.cce.model.CceCodigosTransaccion;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;
import com.bancoexterior.app.cce.service.ICceCodigosTransaccionService;
import com.bancoexterior.app.cce.service.ICceLbtrTransaccionService;
import com.bancoexterior.app.cce.service.ICceTipoTransaccionService;
import com.bancoexterior.app.cce.service.ICceTransaccionService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/codigoTransaccion")
public class CceCodigosTransaccionController {

	private static final Logger LOGGER = LogManager.getLogger(CceCodigosTransaccionController.class);
	
	@Autowired
	private ICceCodigosTransaccionService service;
	
	@Autowired
	private ICceTransaccionService cceTransaccionService;
	
	@Autowired
	private ICceLbtrTransaccionService cceLbtrTransaccionService;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.codigoTransaccion.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Autowired
	private ICceTipoTransaccionService tipoTransaccionService;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERINDEXI = "[==== INICIO Index CodigosTrasaccion Consultas - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERINDEXF = "[==== FIN Index CodigosTrasaccion Consultas - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MENSAJEERRORELIMINAR = "No se puede eliminar el código de transacción ya que tiene transacciones asociadas.";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERFORMI = "[==== INICIO Form CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERFORMF = "[==== FIN Form CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERSAVEI = "[==== INICIO Save CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERSAVEF = "[==== FIN Save CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLEREDITI = "[==== INICIO Edit CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLEREDITF = "[==== FIN Edit CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERGUARDARI = "[==== INICIO Guardar CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERGUARDARF = "[==== FIN Guardar CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERELIMINARI = "[==== INICIO Eliminar CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONCONTROLLERELIMINARF = "[==== FIN Eliminar CodigosTrasaccion - Controller ====]";
	
	private static final String REDIRECTINDEX = "redirect:/codigoTransaccion/index";
	
	private static final String MENSAJECODIGOEXISTE = "El código a insertar ya existe.";
	
	private static final String MENSAJEERRORSELECCIONTIPO = "Debe seleccionar Tipo Transacción.";
	
	private static final String CODIGOTRANSACCION = "codigoTransaccion";
	
	private static final String CODIGOTRANSACCIONTITULO = "CCE - Código Transacción";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";
	
	private static final String CODIGOSTRANSACCIONFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  CodigosTrasaccion - Controller ====]";
	
	private static final String CODIGOSTRANSACCIONFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  CodigosTrasaccion - Controller ====]";
	 
	private static final String LISTATIPOTRANSACCION = "listaTipoTransaccion";
	
	private static final String DESCRIPCION = "descripcion";
	
	private static final String URLFORMCODIGOTRANSACCION = "cce/codigosTransaccion/formCodigoTransaccion";
	
	private static final String URLFORMCODIGOTRANSACCIONEDIT = "cce/codigosTransaccion/formCodigoTransaccionEdit";
	
	private static final String URLLISTACODIGOTRANSACCION = "cce/codigosTransaccion/listaCodigosTransaccion";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operación Fallida.";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String LISTAERROR = "listaError";
	
	@GetMapping("/index")
	public String index(Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceCodigosTransaccion> listaCodigosTransaccion = service.consultarTodosCodigosConTipo();
		
		
		if(listaCodigosTransaccion.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}
		
		model.addAttribute("listaCodigosTransaccion", listaCodigosTransaccion); 
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERINDEXF);
		return URLLISTACODIGOTRANSACCION;
	}
	
	
	@GetMapping("/formCodigoTransaccion")
	public String formCodigoTransaccion(Model model, CceCodigosTransaccionDto cceCodigosTransaccionDto, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
	
		List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
		model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
		guardarAuditoria("formCodigoTransaccion", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERFORMF);
		return URLFORMCODIGOTRANSACCION;
	
	}
	
	@PostMapping("/save")
	public String save(@Valid  CceCodigosTransaccionDto cceCodigosTransaccionDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERSAVEI);
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
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDto.getDescripcion());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMCODIGOTRANSACCION;
		}
		
		
		if(cceCodigosTransaccionDto.getIdTipo() == 0) {
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDto.getDescripcion());
			model.addAttribute(MENSAJEERROR, MENSAJEERRORSELECCIONTIPO);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERSAVEF);
			return URLFORMCODIGOTRANSACCION;
		}
		
		CceCodigosTransaccionDto cceCodigosTransaccionDtoBuscar = service.codigoTransaccionById(cceCodigosTransaccionDto.getCodTransaccion());
		if(cceCodigosTransaccionDtoBuscar == null) {
			service.crearCodigoTransaccion(cceCodigosTransaccionDto.getCodTransaccion(), cceCodigosTransaccionDto.getDescripcion(), cceCodigosTransaccionDto.getIdTipo());
			redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			guardarAuditoriaCodigoTransaccion("save", true, "0000", MENSAJEOPERACIONEXITOSA, cceCodigosTransaccionDto, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERSAVEF);
			return REDIRECTINDEX;
			
		}else {
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDto.getDescripcion());
			model.addAttribute(MENSAJEERROR, MENSAJECODIGOEXISTE);
			guardarAuditoriaCodigoTransaccion("save", false, "0001", MENSAJECODIGOEXISTE, cceCodigosTransaccionDto, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERSAVEF);
			return URLFORMCODIGOTRANSACCION;
		}
			
	}	
	
	@GetMapping("/edit")
	public String edit(@RequestParam("codTransaccion") String codTransaccion, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		CceCodigosTransaccionDto cceCodigosTransaccionDtoEdit = service.codigoTransaccionById(codTransaccion);
		if(cceCodigosTransaccionDtoEdit != null) {
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute("cceCodigosTransaccionDto", cceCodigosTransaccionDtoEdit);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDtoEdit.getDescripcion());
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, codTransaccion, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLEREDITF);
			return URLFORMCODIGOTRANSACCIONEDIT;
			
			
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, codTransaccion, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}	
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceCodigosTransaccionDto cceCodigosTransaccionDto, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERGUARDARI);
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
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDto.getDescripcion());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMCODIGOTRANSACCIONEDIT;
		}
		
		if(cceCodigosTransaccionDto.getIdTipo() == 0) {
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			model.addAttribute(DESCRIPCION, cceCodigosTransaccionDto.getDescripcion());
			model.addAttribute(MENSAJEERROR, MENSAJEERRORSELECCIONTIPO);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERSAVEF);
			return URLFORMCODIGOTRANSACCIONEDIT;
		}
		
		service.actualizarCodigoTransaccion(cceCodigosTransaccionDto.getCodTransaccion(), cceCodigosTransaccionDto.getDescripcion(), cceCodigosTransaccionDto.getIdTipo());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaCodigoTransaccion("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceCodigosTransaccionDto, request);
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERGUARDARF);
		return REDIRECTINDEX;
	}	
	
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("codTransaccion") String codTransaccion,Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CODIGOSTRANSACCIONCONTROLLERELIMINARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		int cantidad = cceTransaccionService.countTransaccionByCodTransaccion(codTransaccion);
		
		int cantidadLbtr = cceLbtrTransaccionService.countCodigoLbtrTransaccionByTipo(codTransaccion);
		
		if(cantidad == 0 && cantidadLbtr == 0) {
			service.eliminarCodigoTransaccion(codTransaccion);
			redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			guardarAuditoriaId("eliminar", true, "0000", MENSAJEOPERACIONEXITOSA, codTransaccion, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERELIMINARF);
			return REDIRECTINDEX;
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEERRORELIMINAR);
			guardarAuditoriaId("eliminar", false, "0001", MENSAJEOPERACIONFALLIDA+MENSAJEERRORELIMINAR, codTransaccion, request);
			LOGGER.info(CODIGOSTRANSACCIONCONTROLLERELIMINARF);
			return REDIRECTINDEX;
		}
	}
	
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = CODIGOTRANSACCIONTITULO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CODIGOTRANSACCION, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, String codTransaccion, HttpServletRequest request) {
		try {
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CODIGOTRANSACCION, accion, codRespuesta, resultado, respuesta+" CodigoTransaccion:[codTransaccion="+codTransaccion+"]", request.getRemoteAddr());
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaCodigoTransaccion(String accion, boolean resultado, String codRespuesta,  String respuesta, CceCodigosTransaccionDto cceCodigosTransaccionDto, HttpServletRequest request) {
		try {
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CODIGOTRANSACCION, accion, codRespuesta, resultado, respuesta+" CodigoTransaccion:[codTransaccion="+cceCodigosTransaccionDto.getCodTransaccion()+""
							+ ", descripcion="+cceCodigosTransaccionDto.getDescripcion()+", codTipoTransaccion="+cceCodigosTransaccionDto.getIdTipo()+"]", request.getRemoteAddr());
			LOGGER.info(CODIGOSTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
}
