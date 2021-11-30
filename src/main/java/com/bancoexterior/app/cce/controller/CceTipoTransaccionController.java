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

import com.bancoexterior.app.cce.dto.CceTipoTransaccionDto;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;
import com.bancoexterior.app.cce.service.ICceCodigosTransaccionService;
import com.bancoexterior.app.cce.service.ICceTipoTransaccionService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/tipoTransaccion")
public class CceTipoTransaccionController {

	private static final Logger LOGGER = LogManager.getLogger(CceTipoTransaccionController.class);
	
	
	@Autowired
	private ICceTipoTransaccionService service;
	
	@Autowired
	private ICceCodigosTransaccionService cceCodigosTransaccionService;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.tipoTransaccion.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String TIPOTRANSACCIONCONTROLLERINDEXI = "[==== INICIO Index TipoTrasaccion Consultas - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERINDEXF = "[==== FIN Index TipoTrasaccion Consultas - Controller ====]";

	private static final String TIPOTRANSACCIONCONTROLLERFORMI = "[==== INICIO Form TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERFORMF = "[==== FIN Form TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERSAVEI = "[==== INICIO Save TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERSAVEF = "[==== FIN Save TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLEREDITI = "[==== INICIO Edit TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLEREDITF = "[==== FIN Edit TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERGUARDARI = "[==== INICIO Guardar TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERGUARDARF = "[==== FIN Guardar TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONCONTROLLERELIMINARI = "[==== INICIO Eliminar TipoTrasaccion - Controller ====]";	
	
	private static final String TIPOTRANSACCIONCONTROLLERELIMINARF = "[==== FIN Eliminar TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCION = "tipoTransaccion";
	
	private static final String TIPOTRANSACCIONTITULO = "CCE - Tipo Transacción";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";
	
	private static final String TIPOTRANSACCIONFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  TipoTrasaccion - Controller ====]";
	
	private static final String TIPOTRANSACCIONFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  TipoTrasaccion - Controller ====]";
	
	private static final String URLLISTATIPOTRANSACCION = "cce/tipoTransaccion/listaTipoTransaccion";
	
	private static final String URLFORMTIPOTRANSACCION = "cce/tipoTransaccion/formTipoTransaccion";

	private static final String URLFORMTIPOTRANSACCIONEDIT = "cce/tipoTransaccion/formTipoTransaccionEdit";
	
	private static final String REDIRECTINDEX = "redirect:/tipoTransaccion/index";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operación Fallida.";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEERRORELIMINAR = "No se puede eliminar el tipo de transacción ya que tiene códigos de transacción asociadas.";
	
	private static final String LISTAERROR = "listaError";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		List<CceTipoTransaccion>  listaTipoTransaccion = service.findAll();
		
		if(listaTipoTransaccion.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}
		
		model.addAttribute("listaTipoTransaccion", listaTipoTransaccion); 
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(TIPOTRANSACCIONCONTROLLERINDEXF);
		return URLLISTATIPOTRANSACCION;
	}
	
	
	@GetMapping("/formTipoTransaccion")
	public String formTipoTransaccion(Model model, CceTipoTransaccionDto cceTipoTransaccionDto, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		cceTipoTransaccionDto.setEnvio(false);
		guardarAuditoria("formTipoTransaccion", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(TIPOTRANSACCIONCONTROLLERFORMF);
		return URLFORMTIPOTRANSACCION;
	}
	
	
	@PostMapping("/save")
	public String save(@Valid CceTipoTransaccionDto cceTipoTransaccionDto, BindingResult result, 
			Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLERSAVEI); 
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
			return URLFORMTIPOTRANSACCION;
		}
		
		LOGGER.info(cceTipoTransaccionDto.getDescripcion());
		LOGGER.info(cceTipoTransaccionDto.isEnvio());
		
		CceTipoTransaccionDto cceTipoTransaccionDtoSave = service.save(cceTipoTransaccionDto);
		guardarAuditoriaTipoTransaccion("save", true, "0000", MENSAJEOPERACIONEXITOSA, cceTipoTransaccionDtoSave, request);
		LOGGER.info(TIPOTRANSACCIONCONTROLLERSAVEF);
		return REDIRECTINDEX;
		
	}	
	
	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		CceTipoTransaccionDto cceTipoTransaccionDtoEdit = service.findById(id);
		
		if(cceTipoTransaccionDtoEdit != null) {
			model.addAttribute("cceTipoTransaccionDto", cceTipoTransaccionDtoEdit);
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(TIPOTRANSACCIONCONTROLLEREDITF);
			return URLFORMTIPOTRANSACCIONEDIT;
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(TIPOTRANSACCIONCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceTipoTransaccionDto cceTipoTransaccionDto, BindingResult result, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLERGUARDARI);
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
			return URLFORMTIPOTRANSACCIONEDIT;
		}
		
		service.save(cceTipoTransaccionDto);
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaTipoTransaccion("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceTipoTransaccionDto, request);
		LOGGER.info(TIPOTRANSACCIONCONTROLLERGUARDARF);
		return REDIRECTINDEX;
		
	}	
	
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int id,Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TIPOTRANSACCIONCONTROLLERELIMINARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		int cantidad = cceCodigosTransaccionService.countCodigoTransaccionByTipo(id);
		
		if(cantidad == 0) {
			service.deleteById(id);
			redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
			guardarAuditoriaId("eliminar", true, "0000", MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(TIPOTRANSACCIONCONTROLLERELIMINARF);
			return REDIRECTINDEX;
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEERRORELIMINAR);
			guardarAuditoriaId("eliminar", false, "0001", MENSAJEOPERACIONFALLIDA+MENSAJEERRORELIMINAR, id, request);
			LOGGER.info(TIPOTRANSACCIONCONTROLLERELIMINARF);
			return REDIRECTINDEX;
		}
		
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = TIPOTRANSACCIONTITULO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					TIPOTRANSACCION, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					TIPOTRANSACCION, accion, codRespuesta, resultado, respuesta+" TipoTransaccion:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	
	public void guardarAuditoriaTipoTransaccion(String accion, boolean resultado, String codRespuesta,  String respuesta, CceTipoTransaccionDto cceTipoTransaccionDto, HttpServletRequest request) {
		try {
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					TIPOTRANSACCION, accion, codRespuesta, resultado, respuesta+" TipoTransaccion:[id="+cceTipoTransaccionDto.getId()+""
							+ ", descripcion="+cceTipoTransaccionDto.getDescripcion()+", envio="+cceTipoTransaccionDto.isEnvio()+"]", request.getRemoteAddr());
			LOGGER.info(TIPOTRANSACCIONFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
