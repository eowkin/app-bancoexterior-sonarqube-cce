package com.bancoexterior.app.convenio.controller;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.service.IAgenciaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;





@Controller
@RequestMapping("/agencias")
public class AgenciaController {

	private static final Logger LOGGER = LogManager.getLogger(AgenciaController.class);
	
	@Autowired 
	private IAgenciaServiceApiRest agenciaServiceApiRest; 
	
	
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	
	
	@Value("${${app.ambiente}"+".agencia.valorBD}")
    private int valorBD;
	
	private static final String URLINDEX = "convenio/agencia/listaAgencias";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String URLFORMAGENCIA = "convenio/agencia/formAgencia";
	
	private static final String URLFORMAGENCIABUSCAR = "convenio/agencia/formBuscarAgencia";
	
	private static final String LISTAAGENCIAS = "listaAgencias";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/agencias/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String AGENCIA = "agencia";
	
	private static final String MENSAJENORESULTADO = "La consulta no arrojo resultado.";
	
	private static final String AGENCIACONTROLLERINDEXI = "[==== INICIO Index Agencia Consultas - Controller ====]";
	
	private static final String AGENCIACONTROLLERINDEXF = "[==== FIN Index Agencia Consultas - Controller ====]";
	
	private static final String AGENCIACONTROLLERACTIVARI = "[==== INICIO Activar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERACTIVARF = "[==== FIN Activar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERDESACTIVARI = "[==== INICIO Desactivar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERDESACTIVARF = "[==== FIN Desactivar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERFORMBUSCARI = "[==== INICIO FormBuscar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERFORMBUSCARF = "[==== FIN FormBuscar Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHCREARI = "[==== INICIO SearchCrear Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHCREARF = "[==== FIN SearchCrear Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSAVEI = "[==== INICIO Save Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSAVEF = "[==== FIN Save Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHI = "[==== INICIO Search Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHF = "[==== FIN Search Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHNOMBREI = "[==== INICIO SearchNombre Agencia - Controller ====]";
	
	private static final String AGENCIACONTROLLERSEACRHNOMBREF = "[==== FIN SearchNombre Agencia - Controller ====]";
	
	private static final String SEARCH = "search";
	
	private static final String SAVE = "save";
	
	private static final String SEARCHCREAR = "searchCrear";
	
	private static final String ACTIVAR = "Activar";
	
	private static final String DESACTIVAR = "Desactivar";
	

	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest, "index", request);
			
			for (Agencia agencia2 : listaAgencias) {
				if(agencia2.getFechaModificacion() != null) {
					String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
					agencia2.setFechaModificacion(arrOfStr[0]);
				}
			}
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(AGENCIACONTROLLERINDEXF);
		return URLINDEX;
	}	
	
	@GetMapping("/activar/{codAgencia}")
	public String activarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setFlagDivisa(true);
		agencia.setCodAgencia(codAgencia);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(true);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest, ACTIVAR, request);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(AGENCIACONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}	
	
	
	@GetMapping("/desactivar/{codAgencia}")
	public String desactivarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERDESACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(false);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest, DESACTIVAR, request);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(AGENCIACONTROLLERDESACTIVARF);
		return REDIRECTINDEX;
	}
	
	
	
	
	
	
	
	@GetMapping("/formBuscarAgencia")
	public String fromBuscarAgencia(Agencia agencia, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERFORMBUSCARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest, "formBuscarAgencia", request);
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			LOGGER.info(AGENCIACONTROLLERFORMBUSCARF);
			return URLFORMAGENCIABUSCAR;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
	}
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model, HttpSession httpSession) {
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		return URLFORMAGENCIA;
	}
	
	@GetMapping("/searchCrear")
	public String searchCrear(Agencia agencia, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERSEACRHCREARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest, SEARCHCREAR, request);
			if(agenciaEdit != null) {
				model.addAttribute(AGENCIA, agenciaEdit);
				LOGGER.info(AGENCIACONTROLLERSEACRHCREARF);
				return URLFORMAGENCIA;
			}else {
				Agencia agenciaBuscarCargar = new Agencia();
				agenciaBuscarCargar.setFlagDivisa(false);
				agenciaRequest.setAgencia(agenciaBuscarCargar);
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return URLFORMAGENCIABUSCAR;
			}
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			Agencia agenciaBuscarCargar = new Agencia();
			agenciaBuscarCargar.setFlagDivisa(false);
			agenciaRequest.setAgencia(agenciaBuscarCargar);
			try {
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, e.getMessage());
				return URLFORMAGENCIABUSCAR;
			} catch (CustomException e1) {
				model.addAttribute(MENSAJEERROR, e1.getMessage());
				return URLFORMAGENCIABUSCAR;
			}
			
		}
		
		
	}	
	
	
	@PostMapping("/save")
	public String saveWs(Agencia agencia, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERSAVEI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		
		
		try {
			String respuesta = agenciaServiceApiRest.crear(agenciaRequest, SAVE, request);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
		}
		LOGGER.info(AGENCIACONTROLLERSAVEF);
		return REDIRECTINDEX;
	}	
	
	
	@GetMapping("/search")
	public String search(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERSEACRHI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getCodAgencia().equals("")){
			agenciaBuscar.setCodAgencia(agenciaSearch.getCodAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest, SEARCH, request);
			if(!listaAgencias.isEmpty()) {
				for (Agencia agencia2 : listaAgencias) {
					if(agencia2.getFechaModificacion() != null) {
						String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
						agencia2.setFechaModificacion(arrOfStr[0]);
					}
				}
				
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				
			}else {
				model.addAttribute(MENSAJE, MENSAJENORESULTADO);
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(AGENCIACONTROLLERSEACRHF);
		return URLINDEX;
	}	
	
	
	@GetMapping("/searchNombre")
	public String searchNombre(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(AGENCIACONTROLLERSEACRHNOMBREI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getNombreAgencia().equals("")){
			agenciaBuscar.setNombreAgencia(agenciaSearch.getNombreAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest, "searchNombre", request);
			if(!listaAgencias.isEmpty()) {
				for (Agencia agencia2 : listaAgencias) {
					if(agencia2.getFechaModificacion() != null) {
						String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
						agencia2.setFechaModificacion(arrOfStr[0]);
					}
				}
				
				
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
			}else {
				
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJE, MENSAJENORESULTADO);
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			
		}
		LOGGER.info(AGENCIACONTROLLERSEACRHNOMBREF);
		return URLINDEX;
		
		
		
		
		
	}
		
	public AgenciaRequest getAgenciaRequest() {
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		agenciaRequest.setIdUsuario(userName);
		agenciaRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		agenciaRequest.setCodUsuario(userName);
		agenciaRequest.setCanal(canal);
		return agenciaRequest;
	}
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		Agencia agenciaSearch = new Agencia();
		model.addAttribute("agenciaSearch", agenciaSearch);
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = AGENCIA;
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	
	
	
}
