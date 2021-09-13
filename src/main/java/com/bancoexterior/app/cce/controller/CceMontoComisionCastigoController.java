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

import com.bancoexterior.app.cce.dto.CceMontoComisionCastigoDto;
import com.bancoexterior.app.cce.model.CceMontoComisionCastigo;
import com.bancoexterior.app.cce.service.ICceMontoComisionCastigoService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;

@Controller
@RequestMapping("/montoComisionCastigo")
public class CceMontoComisionCastigoController {

	private static final Logger LOGGER = LogManager.getLogger(CceMontoComisionCastigoController.class);
	
	@Autowired
	private ICceMontoComisionCastigoService service;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".cce.montoComisionCastigo.valorBD}")
    private int valorBD;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLERINDEXI = "[==== INICIO Index MontoComisionCastigo Consultas - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLERINDEXF = "[==== FIN Index MontoComisionCastigo Consultas - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLEREDITI = "[==== INICIO Edit MontoComisionCastigo - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLEREDITF = "[==== FIN Edit MontoComisionCastigo - Controller ====]";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String REDIRECTINDEX = "redirect:/montoComisionCastigo/index";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLERGUARDARI = "[==== INICIO Guardar MontoComisionCastigo - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGOCONTROLLERGUARDARF = "[==== FIN Guardar MontoComisionCastigo - Controller ====]";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operacion Exitosa.";
	
	private static final String MONTOCOMISIONCASTIGOFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  MontoComisionCastigo - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGOFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  MontoComisionCastigo - Controller ====]";
	
	private static final String MONTOCOMISIONCASTIGO = "montoComisionCastigo";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operacion Fallida.";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<CceMontoComisionCastigo> listaCceMontoComisionCastigo = service.findAll();
		if(listaCceMontoComisionCastigo.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
		}else {
			convertirLista(listaCceMontoComisionCastigo);
		}
		
		guardarAuditoria("index", true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		model.addAttribute("listaCceMontoComisionCastigo", listaCceMontoComisionCastigo); 
		LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLERINDEXF);
		return "cce/comisionCastigo/listaMontosComisionCastigo";
	}
	
	@GetMapping("/edit")
	public String editMontoComisionCastigo(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceMontoComisionCastigoDto cceMontoComisionCastigoEdit = service.findById(id);
		
		if(cceMontoComisionCastigoEdit != null) {
			cceMontoComisionCastigoEdit.setMonto(cceMontoComisionCastigoEdit.getMonto().setScale(2, RoundingMode.HALF_UP));
			model.addAttribute("cceMontoComisionCastigoDto", cceMontoComisionCastigoEdit);
			guardarAuditoriaId("edit", true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLEREDITF);
			return "cce/comisionCastigo/formEditMontoComisionCastigo";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			guardarAuditoriaId("edit", false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
		
	}
	
	@PostMapping("/guardar")
	public String guardar(CceMontoComisionCastigoDto cceMontoComisionCastigoDto, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<String> listaError = new ArrayList<>();
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add("Los valores de los montos debe ser numerico");
			}
			model.addAttribute(LISTAERROR, listaError);
			return "cce/comisionCastigo/formEditMontoComisionCastigo";
		}	
		
		service.updateMontoCastigoTipoTransaccion(cceMontoComisionCastigoDto.getMonto().setScale(2, RoundingMode.HALF_UP), SecurityContextHolder.getContext().getAuthentication().getName(),
				cceMontoComisionCastigoDto.getTipoCliente(), cceMontoComisionCastigoDto.getId());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		guardarAuditoriaMontoComisionCastigo("guardar", true, "0000", MENSAJEOPERACIONEXITOSA, cceMontoComisionCastigoDto, request);
		LOGGER.info(MONTOCOMISIONCASTIGOCONTROLLERGUARDARF);
		return REDIRECTINDEX;
		
	}
	
	public List<CceMontoComisionCastigo> convertirLista(List<CceMontoComisionCastigo> listaCceMontoComisionCastigo){
		for (CceMontoComisionCastigo cceMontoComisionCastigo : listaCceMontoComisionCastigo) {
			cceMontoComisionCastigo.setMontoString(libreriaUtil.formatNumber(cceMontoComisionCastigo.getMonto()));
		}
		
		return listaCceMontoComisionCastigo;
	}
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = MONTOCOMISIONCASTIGO;
		model.addAttribute("arrUri", arrUriP);
	}
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOCOMISIONCASTIGO, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, int id, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOCOMISIONCASTIGO, accion, codRespuesta, resultado, respuesta+" MontoComisionCastigo:[id="+id+"]", request.getRemoteAddr());
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaMontoComisionCastigo(String accion, boolean resultado, String codRespuesta,  String respuesta, CceMontoComisionCastigoDto cceMontoComisionCastigoDto, HttpServletRequest request) {
		try {
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					MONTOCOMISIONCASTIGO, accion, codRespuesta, resultado, respuesta+" MontoComisionCastigo:[id="+cceMontoComisionCastigoDto.getId()+""
							+ ", monto="+cceMontoComisionCastigoDto.getMonto()+", tipoCliente="+cceMontoComisionCastigoDto.getTipoCliente()+"]", request.getRemoteAddr());
			LOGGER.info(MONTOCOMISIONCASTIGOFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
