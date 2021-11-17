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

import com.bancoexterior.app.cce.dto.CceMontoMaximoAproAutoDto;
import com.bancoexterior.app.cce.model.CceHistorialMontoMaximoAproAuto;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.service.ICceHistorialMontoMaximoAproAutoService;
import com.bancoexterior.app.cce.service.ICceMontoMaximoAproAutoService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/montoMaximoAprobacionAuto")
public class CceMontoMaximoAproAutoController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceMontoMaximoAproAutoController.class);
	
	@Autowired
	private ICceMontoMaximoAproAutoService service;
	
	@Autowired
	private ICceHistorialMontoMaximoAproAutoService serviceHistorial;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	@Value("${${app.ambiente}"+".cce.aprobacionAutomatica.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLERINDEXI = "[==== INICIO Index MontoMaximoAproAuto Consultas - Controller ====]";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLERINDEXF = "[==== FIN Index MontoMaximoAproAuto Consultas - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLEREDITI = "[==== INICIO Edit MontoMaximoAproAuto - Controller ====]";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLEREDITF = "[==== FIN Edit MontoMaximoAproAuto - Controller ====]";
	
	private static final String REDIRECTINDEX = "redirect:/montoMaximoAprobacionAuto/index";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLERGUARDARI = "[==== INICIO Guardar MontoMaximoAproAuto - Controller ====]";
	
	private static final String MONTOMAXIMOAPROAUTOCONTROLLERGUARDARF = "[==== FIN Guardar MontoMaximoAproAuto - Controller ====]";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operacion Exitosa.";
	
	private static final String MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  MontoMaximoAproAuto - Controller ====]";
	
	private static final String MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  MontoMaximoAproAuto - Controller ====]";
	
	private static final String MONTOMAXIMOAPROAUTO = "montoMaximoAproAuto";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operacion Fallida.";
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceMontoMaximoAproAuto> listaCceMontoMaximoAproAuto = service.findAll();
		if(listaCceMontoMaximoAproAuto.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}else {
			convertirLista(listaCceMontoMaximoAproAuto);
		}
		
		model.addAttribute("listaCceMontoMaximoAproAuto", listaCceMontoMaximoAproAuto); 
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERINDEXF);
		return "cce/aprobacionAutomatica/listaMontosAprobacionAutomatica";
		
	}	
	
	@GetMapping("/historial")
	public String historial(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceHistorialMontoMaximoAproAuto> listaCceHistorialMontoMaximoAproAuto = serviceHistorial.findAll();
		if(listaCceHistorialMontoMaximoAproAuto.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}else {
			convertirListaHistorial(listaCceHistorialMontoMaximoAproAuto);
		}
		
		model.addAttribute("listaCceHistorialMontoMaximoAproAuto", listaCceHistorialMontoMaximoAproAuto); 
		guardarAuditoria("historial", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERINDEXF);
		return "cce/aprobacionAutomatica/listaHistorialMontosAprobacionAutomatica";
		
	}
	
	
	@GetMapping("/edit")
	public String editMontoComisionCastigo(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		CceMontoMaximoAproAutoDto cceMontoMaximoAproAutoEdit = service.findById(id);
		
		if(cceMontoMaximoAproAutoEdit != null) {
			cceMontoMaximoAproAutoEdit.setMontoString(cceMontoMaximoAproAutoEdit.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
			model.addAttribute("cceMontoMaximoAproAutoDto", cceMontoMaximoAproAutoEdit);
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLEREDITF);
			return "cce/aprobacionAutomatica/formEditMontoAprobacionAutomatica";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}	
	
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceMontoMaximoAproAutoDto cceMontoMaximoAproAutoDto, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERGUARDARI);
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
			return "cce/aprobacionAutomatica/formEditMontoAprobacionAutomatica";
		}
		
		cceMontoMaximoAproAutoDto.setMonto(new BigDecimal(cceMontoMaximoAproAutoDto.getMontoString()).setScale(2, RoundingMode.HALF_UP));
		service.updateMontoMaximoAproAuto(cceMontoMaximoAproAutoDto.getMonto().setScale(2, RoundingMode.HALF_UP), SecurityContextHolder.getContext().getAuthentication().getName(), cceMontoMaximoAproAutoDto.getId());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaMontoMaximoAproAuto("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceMontoMaximoAproAutoDto, request);
		LOGGER.info(MONTOMAXIMOAPROAUTOCONTROLLERGUARDARF);
		return REDIRECTINDEX;
		
	}
	
	public List<CceMontoMaximoAproAuto> convertirLista(List<CceMontoMaximoAproAuto> listaCceMontoMaximoAproAuto){
		for (CceMontoMaximoAproAuto cceMontoMaximoAproAuto : listaCceMontoMaximoAproAuto) {
			cceMontoMaximoAproAuto.setMontoString(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()));
		}
		
		return listaCceMontoMaximoAproAuto;
	}
	
	public List<CceHistorialMontoMaximoAproAuto> convertirListaHistorial(List<CceHistorialMontoMaximoAproAuto> listaCceHistorialMontoMaximoAproAuto){
		for (CceHistorialMontoMaximoAproAuto cceHistorialMontoMaximoAproAuto : listaCceHistorialMontoMaximoAproAuto) {
			cceHistorialMontoMaximoAproAuto.setMontoString(libreriaUtil.formatNumber(cceHistorialMontoMaximoAproAuto.getMonto()));
		}
		
		return listaCceHistorialMontoMaximoAproAuto;
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "montoMaximoAprobacionAuto";
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMAXIMOAPROAUTO, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMAXIMOAPROAUTO, accion, codRespuesta, resultado, respuesta+" MontoMaximoAproAuto:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaMontoMaximoAproAuto(String accion, boolean resultado, String codRespuesta,  String respuesta, CceMontoMaximoAproAutoDto cceMontoMaximoAproAutoDto, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOMAXIMOAPROAUTO, accion, codRespuesta, resultado, respuesta+" MontoMaximoAproAuto:[id="+cceMontoMaximoAproAutoDto.getId()+""
							+ ", monto="+cceMontoMaximoAproAutoDto.getMonto()+"]", request.getRemoteAddr());
			LOGGER.info(MONTOMAXIMOAPROAUTOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
