package com.bancoexterior.app.cce.controller;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.bancoexterior.app.cce.model.CceMontoMinimoComision;
import com.bancoexterior.app.cce.service.ICceMontoMinimoComisionService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/montoMinimoComision")
public class CceMontoMinimoComisionController {

	private static final Logger LOGGER = LogManager.getLogger(CceMontoMinimoComisionController.class);
	
	@Autowired
	private ICceMontoMinimoComisionService service;
	
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
	
	private static final String MENSAJEERROR = "mensajeError";
	
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
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operacion Fallida.";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceMontoMinimoComision> listCceMontoMinimoComision = service.findAll();
		
		if(listCceMontoMinimoComision.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}else {
			convertirLista(listCceMontoMinimoComision);
		}
		
		model.addAttribute("listCceMontoMinimoComision", listCceMontoMinimoComision);
		LOGGER.info(MONTOMINIMOCOMISIONCONTROLLERINDEXF);
		return "cce/comisionMinima/listaMontosComisionMinima";
		
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
			cceMontoMinimoComisionEdit.setMonto(cceMontoMinimoComisionEdit.getMonto().setScale(2, RoundingMode.HALF_UP));
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
	public String guardar(CceMontoMinimoComisionDto cceMontoMinimoComisionDto, BindingResult result,
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
			listaError.add("Los valores de los montos debe ser numerico");
		}
		model.addAttribute(LISTAERROR, listaError);
		return model;
	}
	
	public List<CceMontoMinimoComision> convertirLista(List<CceMontoMinimoComision> listCceMontoMinimoComision){
		
		for (CceMontoMinimoComision cceMontoMinimoComision : listCceMontoMinimoComision) {
			cceMontoMinimoComision.setMontoString(libreriaUtil.formatNumber(cceMontoMinimoComision.getMonto()));
		}
		
		return listCceMontoMinimoComision;
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = MONTOMINIMOCOMISION;
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
