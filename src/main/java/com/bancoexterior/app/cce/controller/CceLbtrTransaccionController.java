package com.bancoexterior.app.cce.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
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
import org.springframework.data.domain.Page;
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

import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.CceCodigosTransaccionDto;
import com.bancoexterior.app.cce.dto.CceLbtrTransaccionDto;
import com.bancoexterior.app.cce.dto.CuentasConsultaRequest;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.dto.Sglbtr;
import com.bancoexterior.app.cce.dto.TransaccionRequest;
import com.bancoexterior.app.cce.dto.TransaccionResponse;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.cce.model.CceCodigosTransaccion;
import com.bancoexterior.app.cce.model.CceCuentasUnicasBcv;
import com.bancoexterior.app.cce.model.CceFechaFeriadoBancario;
import com.bancoexterior.app.cce.model.CceLbtrTransaccion;
import com.bancoexterior.app.cce.model.CceSubProducto;
import com.bancoexterior.app.cce.model.Cuenta;
import com.bancoexterior.app.cce.model.CuentaCliente;
import com.bancoexterior.app.cce.model.FIToFICstmrCdtTrfInitnDetalle;
import com.bancoexterior.app.cce.model.GrpHdrObject;
import com.bancoexterior.app.cce.model.Identificacion;
import com.bancoexterior.app.cce.model.Moneda;
import com.bancoexterior.app.cce.model.ParamIdentificacion;
import com.bancoexterior.app.cce.model.PmtInfObject;
import com.bancoexterior.app.cce.service.IBancoService;
import com.bancoexterior.app.cce.service.IBcvlbtService;
import com.bancoexterior.app.cce.service.ICceCodigosTransaccionService;
import com.bancoexterior.app.cce.service.ICceCuentasUnicasBcvService;
import com.bancoexterior.app.cce.service.ICceFechaFeriadoBancarioService;
import com.bancoexterior.app.cce.service.ICceLbtrTransaccionService;
import com.bancoexterior.app.cce.service.ICceSubProductoService;
import com.bancoexterior.app.cce.service.ICuentasConsultaService;
import com.bancoexterior.app.cce.service.ITransaccionService;
import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.service.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;
import com.google.gson.Gson;

@Controller
@RequestMapping("/cceTransaccionManual")
public class CceLbtrTransaccionController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceLbtrTransaccionController.class);
	
	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;
	
	@Autowired
	private ICuentasConsultaService cuentasConsultaService; 
	
	@Autowired
	private ICceSubProductoService cceSubProductoService;
	
	@Autowired
	private ICceCodigosTransaccionService cceCodigosTransaccionService; 
	
	@Autowired
	private IBancoService bancoService;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Autowired
	private ICceCuentasUnicasBcvService cceCuentasUnicasBcvService;
	
	@Autowired
	private ICceLbtrTransaccionService cceLbtrTransaccionService;
	
	
	@Autowired
	private ICceFechaFeriadoBancarioService cceFechaFeriadoBancarioService;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Autowired
	private ITransaccionService transaccionService;
	
	@Autowired
	private IBcvlbtService bcvlbtService;
	
	@Value("${${app.ambiente}"+".cce.transaccionManual.valorBD}")
    private int valorBD;
	
	@Value("${${app.ambiente}"+".cce.transaccionManualAprobar.valorBD}")
    private int valorBDAprobar;
	
	@Value("${trasaccionesManuales.codTransaccion.PersonaNatural}")
    private String codTransaccionPersonaNatural;
	
	@Value("${trasaccionesManuales.codTransaccion.PersonaJuridica}")
    private String codTransaccionPersonaJuridica;
	
	@Value("${trasaccionesManuales.bancoEmisor}")
    private String bancoEmisorConfiguracion;
	
	@Value("${trasaccionesManuales.codBancoEmisor}")
    private String codBancoEmisorConfiguracion;
	
	@Value("${trasaccionesManuales.referencia}")
    private String referenciaConfiguracion;
	
	@Value("${trasaccionesManuales.moneda}")
    private String monedaConfiguracion;
	
	@Value("${trasaccionesManuales.codProducto}")
    private String codProdcutoConfiguracion;
	
	@Value("${trasaccionesManuales.canalJuridico}")
    private String canalJuridicoConfiguracion;
	
	@Value("${trasaccionesManuales.canalNatural}")
    private String canalNaturalConfiguracion;
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;
	
	private static final String URLFORMTRASACCIONALTOVALOR = "cce/transaccionAltoValor/formTransaccionAltoValor"; 
	
	private static final String URLFORMTRASACCIONALTOVALOREDIT = "cce/transaccionAltoValor/formTransaccionAltoValorEdit";
	
	private static final String URLFORMBUSCARORDENANTE = "cce/transaccionAltoValor/formBuscarOrdenante";
	
	private static final String URLFORMINFORMACIONERRORCODIGOS = "cce/transaccionAltoValor/formInformacionErrorCodigos";
	
	private static final String URLFORMCONSULTARTRASACCIONESALTOVALOR = "cce/transaccionAltoValor/formConsultarTransaccionesAltoValor";
	
	private static final String URLLISTAAPROBARTRASACCIONESALTOVALOR = "cce/transaccionAltoValor/listaAprobarTransaccionesAltoValorPaginate";
	
	private static final String URLFORMTRASACCIONALTOVALORDETALLERECHAZAR =  "cce/transaccionAltoValor/formTransaccionAltoValorDetalleRechazar";
	
	private static final String URLFORMTRASACCIONALTOVALORDETALLEAPROBAR =  "cce/transaccionAltoValor/formTransaccionAltoValorDetalleAprobar";
	
	private static final String TRANSACCIONMANUALCONTROLLERINDEXI = "[==== INICIO Index TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONTROLLERINDEXF = "[==== FIN Index TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALSEARCHCREARCONTROLLERINDEXI = "[==== INICIO SearchCrear TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALSEARCHCREARCONTROLLERINDEXF = "[==== FIN SearchCrear TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALSAVECONTROLLERINDEXI = "[==== INICIO Save TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALSAVECONTROLLERINDEXF = "[==== FIN Save TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALGUARDARCONTROLLERINDEXI = "[==== INICIO Guardar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALGUARDARCONTROLLERINDEXF = "[==== FIN Guardar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONTROLLEREDITI = "[==== INICIO Edit TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONTROLLEREDITF = "[==== FIN Edit TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONTROLLERELIMINARI = "[==== INICIO Eliminar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONTROLLERELIMINARF = "[==== FIN Eliminar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFUNCIONAUDITORIAF = "[==== INICIO Guardar Auditoria  TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFORMBUSCARIDORDENANTECONTROLLERINDEXI = "[==== INICIO FormBuscarIdOrdenante TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFORMBUSCARIDORDENANTECONTROLLERINDEXF = "[==== FIN FormBuscarIdOrdenante TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFORMCONSULTARTRANSACCIONESCONTROLLERINDEXI = "[==== INICIO FormConsultarTransaccionesAltoValor TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALFORMCONSULTARTRANSACCIONESCONTROLLERINDEXF = "[==== FIN FormConsultarTransaccionesAltoValor TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALPROCESARCONSULTATRANSACCIONESCONTROLLERINDEXI = "[==== INICIO ProcesarConsultaTransaccionAltoValor TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALPROCESARCONSULTATRANSACCIONESCONTROLLERINDEXF = "[==== FIN ProcesarConsultaTransaccionAltoValor TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONSULTAPAGEAPROBARCONTROLLERINDEXI = "[==== INICIO consultaPageAprobar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALCONSULTAPAGEAPROBARCONTROLLERINDEXF = "[==== FIN consultaPageAprobar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALDETALLERECHAZARCONTROLLERINDEXI = "[==== INICIO DetalleRechazar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALDETALLERECHAZARCONTROLLERINDEXF = "[==== FIN DetalleRechazar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALGUARDARCHAZARCONTROLLERINDEXI = "[==== INICIO GuardarRechazar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALGUARDARCHAZARCONTROLLERINDEXF = "[==== FIN GuardarRechazar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALDETALLEAPROBARCONTROLLERINDEXI = "[==== INICIO DetalleAprobar TrasaccionManual - Controller ====]";
	
	private static final String TRANSACCIONMANUALDETALLEAPROBARCONTROLLERINDEXF = "[==== FIN DetalleAprobar TrasaccionManual - Controller ====]";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String LISTABANCOS = "listaBancos";
	
	private static final String LISTACUENTACLIENTE = "listaCuentaCliente";
	
	private static final String LISTABANCOSEMISOR = "listaBancosEmisor";
	
	private static final String LISTACCESUBPRODUCTO = "listaCceSubProducto";
	
	private static final String LISTACCECODIGOSTRANSACCION = "listaCceCodigosTransaccion";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String TRANSACCIONMANUAL = "cceTransaccionManual";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJENORESULTADO = "La consulta no arrojo resultado.";
	
	private static final String MENSAJENORESULTADOCUENTAS = "La consulta de las cuentas del id Ordenante no arrojo resultado.";
	
	private static final String URLINDEX = "cce/transaccionAltoValor/listaTransaccionesAltoValorPaginate";
	
	private static final String REDIRECTINDEX = "redirect:/cceTransaccionManual/index";
	
	private static final String REDIRECTINTERNO = "redirect:/cceTransaccionManual/consultaPageAprobar?page=";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";
	
	private static final String MENSAJEOPERACIONFALLO = "Operación fallida, ocurrio un error.";
	
	private static final String CONSULTACUENTAS = "consultaCuentas";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operación Fallida.";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado.";
	
	private static final String SEARCHCREAR = "searchCrear";
	
	private static final String DESCRIPCION = "descripcion";
	
	private static final String ELIMINAR = "eliminar";
	
	private static final String DETALLE = "detalle";
	
	private static final String BANCORECEPTOR = "bancoReceptor";
	
	private static final String PROCESARCONSULTATRANSACCIONALTOVALOR = "procesarConsultaTransaccionAltoValor";
	
	private static final String FORMCONSULTATRANSACCIONALTOVALOR = "formConsultaTransaccionAltoValor";
	
	private static final String LISTACCELBTRTRANSACCION = "listaCceLbtrTransaccion";
	
	private static final String CCELBTRTRANSACCIONDTO = "cceLbtrTransaccionDto";
	
	private static final String DETALLERECHAZAR = "detalleRechazar";
	
	private static final String DETALLEAPROBAR =  "detalleAprobar";
	
	private static final String GUARDARRECHAZAR =  "guardarRechazar";
	
	private static final String EDIT = "edit";
	
	private static final String SAVE =  "save";
	
	private static final String INDEX = "index";
	
	private static final String CONSULTAPAGE = "consultaPage";
	
	private static final String FORMBUSCARORDENANTE =  "formBuscarOrdenante";
	
	private static final String BALNK = "";
	
	private static final String MENSAJEERRORFECHAVALOR = "Fecha Valor Inválida, debe ser igual o mayor al dia actual.";
	
	private static final String MENSAJEERRORFECHAVALORFINDESEMANA = "Fecha Valor Inválida, no puede ser ni sábado ni domingo.";
	
	private static final String MENSAJEERRORFECHAVALORFERIADOBANCARIO = "Fecha Valor Inválida, no puede ser una fecha feriado bancario.";
	
	private static final String MENSAJEERRORTRANSACCIONEDIT = "La transacción no se puede editar, informacion inválida";
	
	private static final String MENSAJEERRORTRANSACCIONRECHAZAR = "La transacción no se puede rechazar, información inválida";
	
	private static final String MENSAJEERRORTRANSACCIONAPROBAR = "La transacción no se puede aprobar, información inválida";
	
	private static final String FORMATOFECHAINICIALIZAR =  "yyyy-MM-dd";
	
	
	
	@GetMapping("/index")
	public String index(Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Page<CceLbtrTransaccion> listaCceLbtrTransaccion =  cceLbtrTransaccionService.consultaLbtrTransacciones(0, userName.toLowerCase());
		if(listaCceLbtrTransaccion.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
		}else {
			convertirLista(listaCceLbtrTransaccion);
		}
		model.addAttribute(LISTACCELBTRTRANSACCION, listaCceLbtrTransaccion);
		guardarAuditoria(INDEX, true, "0000",  MENSAJEOPERACIONEXITOSA, request);
		LOGGER.info(TRANSACCIONMANUALCONTROLLERINDEXF);
		return URLINDEX;
		
	}	
	
	
	@GetMapping("/consultaPage")
	public String consultaPage(Model model, int page, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Page<CceLbtrTransaccion> listaCceLbtrTransaccion =  cceLbtrTransaccionService.consultaLbtrTransacciones(page, userName.toLowerCase());
		
		if(listaCceLbtrTransaccion.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
		}else {
			convertirLista(listaCceLbtrTransaccion);
		}
		LOGGER.info(TRANSACCIONMANUALCONTROLLERINDEXF);
		model.addAttribute(LISTACCELBTRTRANSACCION, listaCceLbtrTransaccion);
		return URLINDEX;
	}	
	
	@GetMapping("/formBuscarOrdenante")
	public String formClientePersonalizadoBuscar(CceLbtrTransaccionDto cceLbtrTransaccionDto, Model model,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALFORMBUSCARIDORDENANTECONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		String mensajeError = "Los codigos para crear Transacciones manuales no existen. "
				+ "Se deben crear primero en el modulo Codigo Transaccion. El codigo "+codTransaccionPersonaNatural+" para Personas Naturales y "
				+ "el codigo "+codTransaccionPersonaJuridica+" para Personas Juridicas";
		
		if(validarCodigosTransaccionManualExitan()) {
			guardarAuditoria(FORMBUSCARORDENANTE, true, "0000",  MENSAJEOPERACIONEXITOSA, request);
			LOGGER.info(TRANSACCIONMANUALFORMBUSCARIDORDENANTECONTROLLERINDEXF);
			return URLFORMBUSCARORDENANTE;
		}else {
			guardarAuditoria(FORMBUSCARORDENANTE, false, "0001",  mensajeError, request);
			
			model.addAttribute(MENSAJEERROR, mensajeError);
			LOGGER.info(TRANSACCIONMANUALFORMBUSCARIDORDENANTECONTROLLERINDEXF);
			return URLFORMINFORMACIONERRORCODIGOS;
		}
		
		
	}
	
	public boolean validarCodigosTransaccionManualExitan() {
		boolean respuesta = false;
		CceCodigosTransaccionDto codigoTransaccionNaturalDto = cceCodigosTransaccionService.codigoTransaccionById(codTransaccionPersonaNatural);
		CceCodigosTransaccionDto codigoTransaccionJuridicaDto = cceCodigosTransaccionService.codigoTransaccionById(codTransaccionPersonaJuridica);
		
		if(codigoTransaccionNaturalDto != null && codigoTransaccionJuridicaDto != null)
			respuesta =  true;
		else
			respuesta = false;
		
		return respuesta;
	}
	
	@GetMapping("/searchCrear")
	public String searchCrear(CceLbtrTransaccionDto cceLbtrTransaccionDto,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(TRANSACCIONMANUALSEARCHCREARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = getClienteDatosBasicoRequest();
		clienteDatosBasicoRequest.setIp(request.getRemoteAddr());
		clienteDatosBasicoRequest.setNroIdCliente(cceLbtrTransaccionDto.getIdemEmisor());
		
		BancoRequest bancoRequest = getBancoRequest();
		CuentasConsultaRequest cuentasConsultaRequest = getCuentasConsultaRequest();
		cuentasConsultaRequest.setIpOrigen(request.getRemoteAddr());
		List<CuentaCliente> listaCuentaCliente;
		List<Banco> listaBancos;
		List<Banco> listaBancosEmisor;
		List<CceSubProducto> listaCceSubProducto;
		
		try {
			DatosClientes datosClientes = clientePersonalizadoServiceApiRest.buscarDatosBasicos(clienteDatosBasicoRequest);
			if(datosClientes != null) {
				cceLbtrTransaccionDto.setIdemEmisor(colocarIdOrdenanteMayuscula(cceLbtrTransaccionDto.getIdemEmisor()));
				cuentasConsultaRequest.setIdCliente(cceLbtrTransaccionDto.getIdemEmisor());
				listaCuentaCliente = cuentasConsultaService.consultaCuentasCliente(cuentasConsultaRequest, CONSULTACUENTAS, request);
				if(listaCuentaCliente.isEmpty()) {
					guardarAuditoriaIdCliente(SEARCHCREAR, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, cceLbtrTransaccionDto.getIdemEmisor(), request);
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADOCUENTAS);
					return URLFORMBUSCARORDENANTE;
				}else {
					model.addAttribute(LISTACUENTACLIENTE, listaCuentaCliente);
					cceLbtrTransaccionDto.setFechaValorString(obtenerFechaFormateada(new Date(), FORMATOFECHAINICIALIZAR));
					cceLbtrTransaccionDto.setMoneda(monedaConfiguracion);
					cceLbtrTransaccionDto.setIdemEmisor(datosClientes.getNroIdCliente());
					cceLbtrTransaccionDto.setNomEmisor(datosClientes.getNombreLegal());
					listaBancos  = bancoService.listaBancos(bancoRequest);
					model.addAttribute(LISTABANCOS, listaBancos);
					listaBancosEmisor = getListaBancosEmisor();
					cceLbtrTransaccionDto.setBancoEmisor(codBancoEmisorConfiguracion);
					cceLbtrTransaccionDto.setCuentaUnicaEmisor(getCuentaUnicaEmisor(codBancoEmisorConfiguracion));
					model.addAttribute(LISTABANCOSEMISOR, listaBancosEmisor);
					listaCceSubProducto = cceSubProductoService.listaSubproductos();
					model.addAttribute(LISTACCESUBPRODUCTO, listaCceSubProducto);
					guardarAuditoriaIdCliente(SEARCHCREAR, true, "0000",  MENSAJEOPERACIONEXITOSA, cceLbtrTransaccionDto.getIdemEmisor(), request);
					LOGGER.info(TRANSACCIONMANUALSEARCHCREARCONTROLLERINDEXF);
					return URLFORMTRASACCIONALTOVALOR;
				}	
			}else {
				guardarAuditoriaIdCliente(SEARCHCREAR, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, cceLbtrTransaccionDto.getIdemEmisor(), request);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return URLFORMBUSCARORDENANTE;
			}
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaIdCliente(SEARCHCREAR, false, "0001",  e.getMessage(), cceLbtrTransaccionDto.getIdemEmisor(), request);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMBUSCARORDENANTE;
		}
		
	}	
	
	
	public String getCuentaUnicaEmisor(String codBancoEmisor) {
		
		CceCuentasUnicasBcv cceCuentasUnicasBcv =  cceCuentasUnicasBcvService.consultaCuentasUnicasBcvByCodigoBic(codBancoEmisor);
		if(cceCuentasUnicasBcv != null) {
			return cceCuentasUnicasBcv.getCuenta();
		}else {
			return "Informacion no Encontrada";
		}
	}
	
	public String colocarIdOrdenanteMayuscula(String idOrdenate) {
		char[] arr = idOrdenate.toCharArray();
	    arr[0] = Character.toUpperCase(arr[0]);
	    return new String(arr);
	}
	
	
	@PostMapping("/save")
	public String save(@Valid CceLbtrTransaccionDto cceLbtrTransaccionDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(TRANSACCIONMANUALSAVECONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		if (result.hasErrors()) {
			try {
				setModelError(model, cceLbtrTransaccionDto, request);
				setError(model, result);
				return URLFORMTRASACCIONALTOVALOR;
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				model.addAttribute(MENSAJEERROR, e.getMessage());
				return URLFORMTRASACCIONALTOVALOR;
			}	
		}
		asignarValores(cceLbtrTransaccionDto);
		LOGGER.info(libreriaUtil.isFechaFinDeSemana(cceLbtrTransaccionDto.getFechaValor()));
		try {	
			if(libreriaUtil.isFechaValidaDesdeHastaDate(fechaHoy(), cceLbtrTransaccionDto.getFechaValor())) {
				if(isFechaIgualFechaFeriadoBancario(cceLbtrTransaccionDto.getFechaValor())) {
					setModelError(model, cceLbtrTransaccionDto, request);
					model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALORFERIADOBANCARIO);
					return URLFORMTRASACCIONALTOVALOR;
				}
				
				if(libreriaUtil.isFechaFinDeSemana(cceLbtrTransaccionDto.getFechaValor())) {
					setModelError(model, cceLbtrTransaccionDto, request);
					model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALORFINDESEMANA);
					return URLFORMTRASACCIONALTOVALOR;
				}
				
				CceLbtrTransaccionDto cceLbtrTransaccionDtoSave = cceLbtrTransaccionService.save(cceLbtrTransaccionDto); 
				if(cceLbtrTransaccionDtoSave != null) {
					redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
					guardarAuditoriaTransaccionManual(SAVE, true, "0000",  MENSAJEOPERACIONEXITOSA, cceLbtrTransaccionDtoSave, request);
				}else {
					redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
					guardarAuditoriaTransaccionManual(SAVE, false, "0001",  MENSAJEOPERACIONFALLIDA, cceLbtrTransaccionDto, request);
				}
				LOGGER.info(TRANSACCIONMANUALSAVECONTROLLERINDEXF);
				return REDIRECTINDEX;	
				
			}else {
				setModelError(model, cceLbtrTransaccionDto, request);
				model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALOR);
				return URLFORMTRASACCIONALTOVALOR;
				
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMTRASACCIONALTOVALOR;
		}
			
			
	}
	
	public boolean isFechaIgualFechaFeriadoBancario(Date fecha) {
		boolean respuesta = false;
		List<CceFechaFeriadoBancario> listaCceFechaFeriadoBancario = cceFechaFeriadoBancarioService.listaFechasFeriado();
		
		for (CceFechaFeriadoBancario cceFechaFeriadoBancario : listaCceFechaFeriadoBancario) {
			if(libreriaUtil.isFechaIgualDate(fecha, cceFechaFeriadoBancario.getFechaFeriado())){
				respuesta = true;
				break;
			}
		}
		return respuesta;
	}
	
	
	@PostMapping("/guardar")
	public String guardar(@Valid CceLbtrTransaccionDto cceLbtrTransaccionDto, BindingResult result,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(TRANSACCIONMANUALGUARDARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		
		if (result.hasErrors()) {
			try {
				setModelError(model, cceLbtrTransaccionDto, request);
				setError(model, result);
				return URLFORMTRASACCIONALTOVALOREDIT;
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				model.addAttribute(MENSAJEERROR, e.getMessage());
				return URLFORMTRASACCIONALTOVALOREDIT;
			}	
		}
		
		try {
			asignarValores(cceLbtrTransaccionDto);
			
			
			
			if(libreriaUtil.isFechaValidaDesdeHastaDate(fechaHoy(), cceLbtrTransaccionDto.getFechaValor())) {
				if(isFechaIgualFechaFeriadoBancario(cceLbtrTransaccionDto.getFechaValor())) {
					setModelError(model, cceLbtrTransaccionDto, request);
					model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALORFERIADOBANCARIO);
					return URLFORMTRASACCIONALTOVALOREDIT;
				}
				
				if(libreriaUtil.isFechaFinDeSemana(cceLbtrTransaccionDto.getFechaValor())) {
					setModelError(model, cceLbtrTransaccionDto, request);
					model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALORFINDESEMANA);
					return URLFORMTRASACCIONALTOVALOREDIT;
				}
				CceLbtrTransaccionDto cceLbtrTransaccionDtoSave = cceLbtrTransaccionService.save(cceLbtrTransaccionDto); 
				if(cceLbtrTransaccionDtoSave != null) {
					redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
					guardarAuditoriaTransaccionManual("guardar", true, "0000",  MENSAJEOPERACIONEXITOSA, cceLbtrTransaccionDtoSave, request);
				}else {
					redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
					guardarAuditoriaTransaccionManual("guardar", false, "0001",  MENSAJEOPERACIONFALLIDA, cceLbtrTransaccionDto, request);
				}
				LOGGER.info(TRANSACCIONMANUALGUARDARCONTROLLERINDEXF);
				return REDIRECTINDEX;	
				
			}else {
				
					setModelError(model, cceLbtrTransaccionDto, request);
					model.addAttribute(MENSAJEERROR, MENSAJEERRORFECHAVALOR);
					return URLFORMTRASACCIONALTOVALOREDIT;
				
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMTRASACCIONALTOVALOREDIT;
		}
			
			
	}
	
	
	
	@GetMapping("/edit")
	public String edit(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceLbtrTransaccionDto cceLbtrTransaccionDtoEdit = cceLbtrTransaccionService.findById(id); 
		
		if(cceLbtrTransaccionDtoEdit != null) {
			if(!cceLbtrTransaccionDtoEdit.getStatus().equals("I")) {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEERRORTRANSACCIONEDIT);
				guardarAuditoriaIdTransaccion(EDIT, false, "0001",  MENSAJEERRORTRANSACCIONEDIT, id, request);
				LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
				return REDIRECTINDEX;
			}
			
			ClienteDatosBasicoRequest clienteDatosBasicoRequest = getClienteDatosBasicoRequest();
			clienteDatosBasicoRequest.setIp(request.getRemoteAddr());
			clienteDatosBasicoRequest.setNroIdCliente(cceLbtrTransaccionDtoEdit.getIdemEmisor());
			
			BancoRequest bancoRequestED = getBancoRequest();
			CuentasConsultaRequest cuentasConsultaRequestED = getCuentasConsultaRequest();
			cuentasConsultaRequestED.setIpOrigen(request.getRemoteAddr());
			List<CuentaCliente> listaCuentaClienteED;
			List<Banco> listaBancosED;
			List<Banco> listaBancosEmisorED;
			List<CceSubProducto> listaCceSubProductoED;
			try {
				DatosClientes datosClientes = clientePersonalizadoServiceApiRest.buscarDatosBasicos(clienteDatosBasicoRequest);
				if(datosClientes != null) {
					cuentasConsultaRequestED.setIdCliente(cceLbtrTransaccionDtoEdit.getIdemEmisor());
					listaCuentaClienteED = cuentasConsultaService.consultaCuentasCliente(cuentasConsultaRequestED, CONSULTACUENTAS, request);
					if(listaCuentaClienteED.isEmpty()) {
						redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADOCUENTAS);
						guardarAuditoriaIdTransaccion(EDIT, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
						return REDIRECTINDEX;
					}else {
						cceLbtrTransaccionDtoEdit.setMontoString(cceLbtrTransaccionDtoEdit.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
						cceLbtrTransaccionDtoEdit.setFechaValorString(obtenerFechaFormateada(cceLbtrTransaccionDtoEdit.getFechaValor(), FORMATOFECHAINICIALIZAR));
						model.addAttribute(DESCRIPCION, cceLbtrTransaccionDtoEdit.getDescripcion());
						model.addAttribute(CCELBTRTRANSACCIONDTO, cceLbtrTransaccionDtoEdit);
						model.addAttribute(LISTACUENTACLIENTE, listaCuentaClienteED);
						cceLbtrTransaccionDtoEdit.setMoneda("VES");
						cceLbtrTransaccionDtoEdit.setIdemEmisor(datosClientes.getNroIdCliente());
						cceLbtrTransaccionDtoEdit.setNomEmisor(datosClientes.getNombreLegal());
						listaBancosED  = bancoService.listaBancos(bancoRequestED);
						model.addAttribute(LISTABANCOS, listaBancosED);
						listaBancosEmisorED = getListaBancosEmisor();
						cceLbtrTransaccionDtoEdit.setBancoEmisor(codBancoEmisorConfiguracion);
						cceLbtrTransaccionDtoEdit.setCuentaUnicaEmisor(getCuentaUnicaEmisor(codBancoEmisorConfiguracion));
						model.addAttribute(LISTABANCOSEMISOR, listaBancosEmisorED);
						listaCceSubProductoED = cceSubProductoService.listaSubproductos();
						model.addAttribute(LISTACCESUBPRODUCTO, listaCceSubProductoED);
						guardarAuditoriaIdTransaccion(EDIT, true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
						LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
						return URLFORMTRASACCIONALTOVALOREDIT;
						
					}	
				}else {
					guardarAuditoriaIdTransaccion(EDIT, false, "0001", MENSAJENORESULTADO, id, request);
					redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					return REDIRECTINDEX;
				}
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				guardarAuditoriaIdTransaccion(EDIT, false, "0001",  e.getMessage(), id, request);
				redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
				return REDIRECTINDEX;
			}		
			
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaIdTransaccion(EDIT, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}	
	
	@GetMapping("/detalle")
	public String detalle(@RequestParam("id") int id, Model model, int page, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceLbtrTransaccionDto cceLbtrTransaccionDto = cceLbtrTransaccionService.findById(id); 
		
		if(cceLbtrTransaccionDto != null) {
			try {
			 	cceLbtrTransaccionDto.setBancoEmisor(codBancoEmisorConfiguracion +" - "+bancoEmisorConfiguracion);
				cceLbtrTransaccionDto.setBancoReceptor(cceLbtrTransaccionDto.getBancoReceptor() +" - "+getBancoReceptor(cceLbtrTransaccionDto.getBancoReceptor()).getNbBanco());
				cceLbtrTransaccionDto.setMontoString(cceLbtrTransaccionDto.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
				cceLbtrTransaccionDto.setFechaValorString(convertirFechaString(cceLbtrTransaccionDto.getFechaValor()));
				cceLbtrTransaccionDto.setSubProducto(cceLbtrTransaccionDto.getSubProducto()+" - "+ cceSubProductoService.buscarSubProductoPorCodProductoAndCodSubProducto("801", cceLbtrTransaccionDto.getSubProducto()).getNombreSubProducto());
				cceLbtrTransaccionDto.setCodTransaccion(cceLbtrTransaccionDto.getCodTransaccion()+" - "+cceCodigosTransaccionService.codigoTransaccionById(cceLbtrTransaccionDto.getCodTransaccion()).getDescripcion());
				
				model.addAttribute(CCELBTRTRANSACCIONDTO, cceLbtrTransaccionDto);
				model.addAttribute(DESCRIPCION, cceLbtrTransaccionDto.getDescripcion());
				model.addAttribute("page", page);
				guardarAuditoriaIdTransaccion(DETALLE, true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
				LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
				return "cce/transaccionAltoValor/formTransaccionAltoValorDetalle";
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				guardarAuditoriaIdTransaccion(DETALLE, false, "0001", e.getMessage(), id, request);
				redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
				return REDIRECTINDEX;
			}	
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaIdTransaccion(DETALLE, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}	
	
	
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONTROLLERELIMINARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		CceLbtrTransaccionDto cceLbtrTransaccionDto = cceLbtrTransaccionService.findById(id); 
		
		if(cceLbtrTransaccionDto != null) {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			cceLbtrTransaccionDto.setUsuarioAprobador(userName.toLowerCase());
			cceLbtrTransaccionDto.setStatus("E");
			CceLbtrTransaccionDto cceLbtrTransaccionDtoSave = cceLbtrTransaccionService.save(cceLbtrTransaccionDto); 
			if(cceLbtrTransaccionDtoSave != null) {
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
				guardarAuditoriaIdTransaccion(ELIMINAR, true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
				guardarAuditoriaIdTransaccion(ELIMINAR, false, "0001",  MENSAJEOPERACIONFALLO, id, request);
			}
			
			LOGGER.info(TRANSACCIONMANUALCONTROLLERELIMINARF);
			return REDIRECTINDEX;	
			
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaIdTransaccion(ELIMINAR, false, "0001",  MENSAJEOPERACIONFALLIDA+MENSAJECONSULTANOARROJORESULTADOS, id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}
	
	
	@GetMapping("/formConsultaTransaccionAltoValor")
	public String formConsultaTransaccionAltoValor(CceLbtrTransaccionDto cceLbtrTransaccionDto, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALFORMCONSULTARTRANSACCIONESCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		BancoRequest bancoRequestED = getBancoRequest();
		List<Banco> listaBancosED;
		
		try {
			listaBancosED  = bancoService.listaBancos(bancoRequestED);
			model.addAttribute(LISTABANCOS, listaBancosED);
			guardarAuditoria(FORMCONSULTATRANSACCIONALTOVALOR, true, "0000",  MENSAJEOPERACIONEXITOSA, request);
			LOGGER.info(TRANSACCIONMANUALFORMCONSULTARTRANSACCIONESCONTROLLERINDEXF);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			guardarAuditoria(FORMCONSULTATRANSACCIONALTOVALOR, false, "0001",  e.getMessage(), request);
		}	
		return URLFORMCONSULTARTRASACCIONESALTOVALOR;
	}
	
	@GetMapping("/procesarConsultaTransaccionAltoValor")
	public String procesarConsultaTransaccionAltoValor(CceLbtrTransaccionDto cceLbtrTransaccionDto, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALPROCESARCONSULTATRANSACCIONESCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		BancoRequest bancoRequestED = getBancoRequest();
		List<Banco> listaBancosED;
		Page<CceLbtrTransaccion> listaCceLbtrTransaccion;
		try {
			String fechaHoy = obtenerFechaFormateada(new Date(), FORMATOFECHAINICIALIZAR);
			listaCceLbtrTransaccion = cceLbtrTransaccionService.consultaLbtrTransaccionesAprobarFechas(cceLbtrTransaccionDto.getBancoReceptor(), 
					fechaHoy, 0);
			if(listaCceLbtrTransaccion.isEmpty()) {
				model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
				listaBancosED  = bancoService.listaBancos(bancoRequestED);
				model.addAttribute(LISTABANCOS, listaBancosED);
				guardarAuditoriaTransaccionManualConsulta(PROCESARCONSULTATRANSACCIONALTOVALOR, false, "0001",  MENSAJENORESULTADO, cceLbtrTransaccionDto, request);
				return URLFORMCONSULTARTRASACCIONESALTOVALOR;
			}else {
				convertirLista(listaCceLbtrTransaccion);
				model.addAttribute(LISTACCELBTRTRANSACCION, listaCceLbtrTransaccion);
				httpSession.setAttribute(BANCORECEPTOR, cceLbtrTransaccionDto.getBancoReceptor());
				guardarAuditoriaTransaccionManualConsulta(PROCESARCONSULTATRANSACCIONALTOVALOR, true, "0000",  MENSAJEOPERACIONEXITOSA, cceLbtrTransaccionDto, request);
				LOGGER.info(TRANSACCIONMANUALPROCESARCONSULTATRANSACCIONESCONTROLLERINDEXF);
				return URLLISTAAPROBARTRASACCIONESALTOVALOR;
					
			}
				
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoriaTransaccionManualConsulta(PROCESARCONSULTATRANSACCIONALTOVALOR, false, "0001", e.getMessage(), cceLbtrTransaccionDto, request);
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMCONSULTARTRASACCIONESALTOVALOR;
			
		}	
		
	}
	
	
	
	
	
	@GetMapping("/consultaPageAprobar")
	public String consultaPageAprobarSesion(Model model, int page, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALCONSULTAPAGEAPROBARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		Page<CceLbtrTransaccion> listaCceLbtrTransaccion;
		String bancoReceptor = (String)httpSession.getAttribute(BANCORECEPTOR);
		String fechaHoy = obtenerFechaFormateada(new Date(), FORMATOFECHAINICIALIZAR);
		listaCceLbtrTransaccion =  cceLbtrTransaccionService.consultaLbtrTransaccionesAprobarFechas(bancoReceptor, fechaHoy, page);
		convertirLista(listaCceLbtrTransaccion);
		model.addAttribute(LISTACCELBTRTRANSACCION, listaCceLbtrTransaccion);
		LOGGER.info(TRANSACCIONMANUALCONSULTAPAGEAPROBARCONTROLLERINDEXF);
		return URLLISTAAPROBARTRASACCIONESALTOVALOR;		
	}
	
	@GetMapping("/detalleRechazar")
	public String detalleRechazarSesion(@RequestParam("id") int id, Model model, int page, 
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALDETALLERECHAZARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		String redirectINterno = REDIRECTINTERNO+page;
		CceLbtrTransaccionDto cceLbtrTransaccionDtoDR = cceLbtrTransaccionService.findById(id); 
		if(cceLbtrTransaccionDtoDR != null) {
			try {
				if(cceLbtrTransaccionDtoDR.getStatus().equals("I")) {
					cceLbtrTransaccionDtoDR.setBancoEmisor(codBancoEmisorConfiguracion +" - "+bancoEmisorConfiguracion);
					cceLbtrTransaccionDtoDR.setBancoReceptor(cceLbtrTransaccionDtoDR.getBancoReceptor() +" - "+getBancoReceptor(cceLbtrTransaccionDtoDR.getBancoReceptor()).getNbBanco());
					cceLbtrTransaccionDtoDR.setMontoString(cceLbtrTransaccionDtoDR.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
					cceLbtrTransaccionDtoDR.setFechaValorString(convertirFechaString(cceLbtrTransaccionDtoDR.getFechaValor()));
					cceLbtrTransaccionDtoDR.setSubProducto(cceLbtrTransaccionDtoDR.getSubProducto()+" - "+ cceSubProductoService.buscarSubProductoPorCodProductoAndCodSubProducto("801", cceLbtrTransaccionDtoDR.getSubProducto()).getNombreSubProducto());
					cceLbtrTransaccionDtoDR.setCodTransaccion(cceLbtrTransaccionDtoDR.getCodTransaccion()+" - "+cceCodigosTransaccionService.codigoTransaccionById(cceLbtrTransaccionDtoDR.getCodTransaccion()).getDescripcion());
					
					model.addAttribute(CCELBTRTRANSACCIONDTO, cceLbtrTransaccionDtoDR);
					model.addAttribute(DESCRIPCION, cceLbtrTransaccionDtoDR.getDescripcion());
					model.addAttribute("page", page);
					model.addAttribute("id", id);
					guardarAuditoriaIdTransaccion(DETALLERECHAZAR, true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
					LOGGER.info(TRANSACCIONMANUALDETALLERECHAZARCONTROLLERINDEXF);
					return URLFORMTRASACCIONALTOVALORDETALLERECHAZAR;
				}else {
					redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEERRORTRANSACCIONRECHAZAR);
					guardarAuditoriaIdTransaccion(DETALLERECHAZAR, false, "0001",  MENSAJEERRORTRANSACCIONRECHAZAR, id, request);
					return redirectINterno;
				}
			 	
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				guardarAuditoriaIdTransaccion(DETALLERECHAZAR, false, "0001",  e.getMessage(), id, request);
				LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
				return redirectINterno;
			}	
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaIdTransaccion(DETALLERECHAZAR, false, "0001",  MENSAJENORESULTADO, id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return redirectINterno;
		}
		
	}
	
	
	@GetMapping("/guardarRechazar")
	public String guardarRechazarSesion(@RequestParam("id") int id, Model model, int page, 
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALGUARDARCHAZARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		String bancoReceptor = (String)httpSession.getAttribute(BANCORECEPTOR);
		String fechaHoy = obtenerFechaFormateada(new Date(), FORMATOFECHAINICIALIZAR);
		CceLbtrTransaccionDto cceLbtrTransaccionDto = cceLbtrTransaccionService.findById(id); 
		
		if(cceLbtrTransaccionDto != null) {
			setUsuarioAprobador(cceLbtrTransaccionDto);
			cceLbtrTransaccionDto.setStatus("RT");
			CceLbtrTransaccionDto cceLbtrTransaccionDtoSave = cceLbtrTransaccionService.save(cceLbtrTransaccionDto); 
			if(cceLbtrTransaccionDtoSave != null) {
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
				guardarAuditoriaTransaccionManual(GUARDARRECHAZAR, true, "0000",  MENSAJEOPERACIONEXITOSA, cceLbtrTransaccionDtoSave, request);
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
				guardarAuditoriaTransaccionManual(GUARDARRECHAZAR, false, "0001",  MENSAJEOPERACIONFALLO, cceLbtrTransaccionDtoSave, request);
			}
			
			LOGGER.info(TRANSACCIONMANUALGUARDARCHAZARCONTROLLERINDEXF);
			return REDIRECTINTERNO+getPage(page, bancoReceptor, fechaHoy);	
			
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaTransaccionManual(GUARDARRECHAZAR, false, "0001",  MENSAJEOPERACIONFALLO, cceLbtrTransaccionDto, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
	}
	
	
		
	
	
	@GetMapping("/detalleAprobar")
	public String detalleAprobar(@RequestParam("id") int id, Model model, int page, 
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALDETALLEAPROBARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
								 
		String redirectINterno = REDIRECTINTERNO+page;
		CceLbtrTransaccionDto cceLbtrTransaccionDtoDA = cceLbtrTransaccionService.findById(id); 
		if(cceLbtrTransaccionDtoDA != null) {
			try {
				if(cceLbtrTransaccionDtoDA.getStatus().equals("I")) {
					cceLbtrTransaccionDtoDA.setBancoEmisor(codBancoEmisorConfiguracion +" - "+bancoEmisorConfiguracion);
					cceLbtrTransaccionDtoDA.setBancoReceptor(cceLbtrTransaccionDtoDA.getBancoReceptor() +" - "+getBancoReceptor(cceLbtrTransaccionDtoDA.getBancoReceptor()).getNbBanco());
					cceLbtrTransaccionDtoDA.setMontoString(cceLbtrTransaccionDtoDA.getMonto().setScale(2, RoundingMode.HALF_UP).toString());
					cceLbtrTransaccionDtoDA.setFechaValorString(convertirFechaString(cceLbtrTransaccionDtoDA.getFechaValor()));
					cceLbtrTransaccionDtoDA.setSubProducto(cceLbtrTransaccionDtoDA.getSubProducto()+" - "+ cceSubProductoService.buscarSubProductoPorCodProductoAndCodSubProducto("801", cceLbtrTransaccionDtoDA.getSubProducto()).getNombreSubProducto());
					cceLbtrTransaccionDtoDA.setCodTransaccion(cceLbtrTransaccionDtoDA.getCodTransaccion()+" - "+cceCodigosTransaccionService.codigoTransaccionById(cceLbtrTransaccionDtoDA.getCodTransaccion()).getDescripcion());
					
					model.addAttribute(CCELBTRTRANSACCIONDTO, cceLbtrTransaccionDtoDA);
					model.addAttribute(DESCRIPCION, cceLbtrTransaccionDtoDA.getDescripcion());
					model.addAttribute("page", page);
					model.addAttribute("id", id);
					guardarAuditoriaIdTransaccion(DETALLEAPROBAR, true, "0000",  MENSAJEOPERACIONEXITOSA, id, request);
					LOGGER.info(TRANSACCIONMANUALDETALLEAPROBARCONTROLLERINDEXF);
					return URLFORMTRASACCIONALTOVALORDETALLEAPROBAR;
				}else {
					redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEERRORTRANSACCIONAPROBAR);
					guardarAuditoriaIdTransaccion(DETALLEAPROBAR, false, "0001",  MENSAJEERRORTRANSACCIONAPROBAR, id, request);
					return redirectINterno;
				}
			 	
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				guardarAuditoriaIdTransaccion(DETALLEAPROBAR, false, "0001",  e.getMessage(), id, request);
				LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
				return redirectINterno;
			}	
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaIdTransaccion(DETALLEAPROBAR, false, "0001",  MENSAJENORESULTADO, id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			return redirectINterno;
		}
		
	}
	
	
	@GetMapping("/guardarAprobar")
	public String guardarAprobar(@RequestParam("id") int id, Model model, int page, 
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALGUARDARCHAZARCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		String bancoReceptor = (String)httpSession.getAttribute(BANCORECEPTOR);
		String fechaHoy = obtenerFechaFormateada(new Date(), FORMATOFECHAINICIALIZAR);
		CceLbtrTransaccionDto cceLbtrTransaccionDto = cceLbtrTransaccionService.findById(id); 
		TransaccionRequest transaccionRequest;
		TransaccionResponse transaccionResponse;
		try {
			transaccionRequest = getTransaccionRequest(cceLbtrTransaccionDto);  
			transaccionResponse = transaccionService.procesar(transaccionRequest, "guardarAprobar", request);
			if(transaccionResponse == null) {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLO);
			}else {
				if(transaccionResponse.getResultado().getCodigo().equals("0000")) {
					cceLbtrTransaccionDto.setReferencia(libreriaUtil.getReferenciaUltimosOchoDigitos(transaccionResponse.getDatosTransaccion().getReferencia()));
					setUsuarioAprobador(cceLbtrTransaccionDto);
					Resultado resultadoResponse = procesarAprobarAltoValor(cceLbtrTransaccionDto, "guardarAprobar", request);
					redirectAttributes.addFlashAttribute(MENSAJE, aprobarActualizar(resultadoResponse, cceLbtrTransaccionDto));
				}else {
					setUsuarioAprobador(cceLbtrTransaccionDto);
					cceLbtrTransaccionDto.setStatus("RC");
					redirectAttributes.addFlashAttribute(MENSAJEERROR, "Transaccion Rechazada por el Core. "+ transaccionResponse.getResultado().getDescripcion());
					cceLbtrTransaccionService.save(cceLbtrTransaccionDto);
					
					
				}
			}
			
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setUsuarioAprobador(cceLbtrTransaccionDto);
			cceLbtrTransaccionDto.setStatus("PP");
			cceLbtrTransaccionService.save(cceLbtrTransaccionDto);
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			guardarAuditoriaIdTransaccion(DETALLEAPROBAR, false, "0001",  e.getMessage(), id, request);
			LOGGER.info(TRANSACCIONMANUALCONTROLLEREDITF);
			
		}	
		
		return REDIRECTINTERNO+getPage(page, bancoReceptor, fechaHoy);	
			
		
	}
	
	public void setUsuarioAprobador(CceLbtrTransaccionDto cceLbtrTransaccionDto) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		cceLbtrTransaccionDto.setUsuarioAprobador(userName.toLowerCase());
	}
	
	
	public String aprobarActualizar(Resultado resultadoAprobarResponse, CceLbtrTransaccionDto cceLbtrTransaccionDto) {
		
		String mensaje = "Transaccion Id: "+cceLbtrTransaccionDto.getId();
		cceLbtrTransaccionDto.setStatus(getStatus(resultadoAprobarResponse.getCodigo()));
		CceLbtrTransaccionDto cceLbtrTransaccionDtoSave =  cceLbtrTransaccionService.save(cceLbtrTransaccionDto);
		if(cceLbtrTransaccionDtoSave != null) {
			mensaje = mensaje + " Respuesta: " + getMensaje(resultadoAprobarResponse.getCodigo()) + " Finalizado correctamente.";
			return mensaje;
		}else {
			mensaje = mensaje + " Respuesta: " + getMensaje(resultadoAprobarResponse.getCodigo()) + " Error al Actualzar Transaccion.";
			return mensaje;
		}
	}
	
	public String getStatus(String codigoGA) {
		String status;
		if(codigoGA.equals("ACCP")) {
			status = "PA";
		}else {
			if(codigoGA.equals("0000") || codigoGA.equals("CC05")) {
				status = "PR";
			}else {
				status = "PP";
			}
		}
		return status;
	}
	
	public String getMensaje(String codigoGA) {
		String mensaje;
		if(codigoGA.equals("ACCP")) {
			mensaje = "PROCESADA APROBADA";
		}else {
			if(codigoGA.equals("0000") || codigoGA.equals("CC05")) {
				mensaje = "PROCESADA RECHAZADA";
			}else {
				mensaje = "PROCESADA PENDIENTE";
			}
		}
		return mensaje;
	}
	
	
	public Resultado procesarAprobarAltoValor(CceLbtrTransaccionDto cceLbtrTransaccionDto, String accion, HttpServletRequest request) throws CustomException{
		
		
		FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest = new FiToFiCustomerCreditTransferRequest(); 
		
		Sglbtr sglbtr = new Sglbtr();
		FIToFICstmrCdtTrfInitnDetalle fIToFICstmrCdtTrfInitnDetalle = new FIToFICstmrCdtTrfInitnDetalle(); 
		GrpHdrObject grpHdr = new GrpHdrObject();
		List<PmtInfObject> pmtInf = new ArrayList<>();
		Moneda moneda = new Moneda();
		
		//creando el ParamIdentificacion de la esctructura
		ParamIdentificacion paramIdentificacion = getParamIdentificacion();
		paramIdentificacion.setCodTransaccion(cceLbtrTransaccionDto.getCodTransaccion());
		paramIdentificacion.setBancoReceptor(getBancoReceptor(cceLbtrTransaccionDto.getBancoReceptor()).getNbBanco());
		
		//creando el grpHdr de la esctructura
		grpHdr.setMsgId(getMsgId());
		grpHdr.setCreDtTm(libreriaUtil.fechayhora());
		grpHdr.setNbOfTxs(1);
		
		moneda.setCcy(cceLbtrTransaccionDto.getMoneda());
		moneda.setAmt(cceLbtrTransaccionDto.getMonto().doubleValue());
		grpHdr.setCtrlSum(moneda);
		grpHdr.setIntrBkSttlmDt(libreriaUtil.obtenerFechaYYYYMMDD());
		grpHdr.setLclInstrm(cceLbtrTransaccionDto.getProducto());
		grpHdr.setChannel(libreriaUtil.getChannel(cceLbtrTransaccionDto.getCanal()));
		
		fIToFICstmrCdtTrfInitnDetalle.setGrpHdr(grpHdr);
		
		PmtInfObject pmtInfObject = new PmtInfObject();
		pmtInfObject.setRegId(1);
		pmtInfObject.setEndToEndId(libreriaUtil.getEndToEndId(cceLbtrTransaccionDto.getBancoEmisor(), cceLbtrTransaccionDto.getReferencia()));
		pmtInfObject.setClrSysRef(BALNK);
		pmtInfObject.setTxId(BALNK);
		pmtInfObject.setAmount(moneda);
		pmtInfObject.setPurp(cceLbtrTransaccionDto.getSubProducto());
		pmtInfObject.setDbtrAgt(cceLbtrTransaccionDto.getBancoEmisor());
		pmtInfObject.setCdtrAgt(cceLbtrTransaccionDto.getBancoReceptor());
		
		Identificacion dbtr = new Identificacion();
		dbtr.setNm(cceLbtrTransaccionDto.getNomEmisor());
		dbtr.setId(cceLbtrTransaccionDto.getIdemEmisor());
		dbtr.setSchmeNm(libreriaUtil.getSchmeNm(cceLbtrTransaccionDto.getIdemEmisor()));
		pmtInfObject.setDbtr(dbtr);
		
		
		Cuenta dbtrAcct = new Cuenta();
		dbtrAcct.setTp("CNTA");
		dbtrAcct.setId(cceLbtrTransaccionDto.getCuentaEmisor());
		pmtInfObject.setDbtrAcct(dbtrAcct);
		
		Cuenta dbtrAgtAcct = new Cuenta();
		dbtrAgtAcct.setTp("CNTA");
		dbtrAgtAcct.setId(cceCuentasUnicasBcvService.consultaCuentasUnicasBcvByCodigoBic(cceLbtrTransaccionDto.getBancoEmisor()).getCuenta());
		pmtInfObject.setDbtrAgtAcct(dbtrAgtAcct);
		
		Identificacion cdtr = new Identificacion();
		cdtr.setNm(cceLbtrTransaccionDto.getNomReceptor());
		cdtr.setId(cceLbtrTransaccionDto.getIdemReceptor());
		cdtr.setSchmeNm(libreriaUtil.getSchmeNm(cceLbtrTransaccionDto.getIdemReceptor()));
		pmtInfObject.setCdtr(cdtr);
		
		Cuenta cdtrAcct = new Cuenta();
		cdtrAcct.setTp("CNTA");
		cdtrAcct.setId(cceLbtrTransaccionDto.getCuentaReceptor());
		pmtInfObject.setCdtrAcct(cdtrAcct);
		
		Cuenta cdtrAgtAcct = new Cuenta();
		cdtrAgtAcct.setTp("CNTA");
		cdtrAgtAcct.setId(cceCuentasUnicasBcvService.consultaCuentasUnicasBcvByCodigoBic(cceLbtrTransaccionDto.getBancoReceptor()).getCuenta());
		pmtInfObject.setCdtrAgtAcct(cdtrAgtAcct);
		
		pmtInfObject.setRmtInf(libreriaUtil.getRmtInf());
		
		
		pmtInf.add(pmtInfObject);
		
		fIToFICstmrCdtTrfInitnDetalle.setPmtInf(pmtInf);
		
		sglbtr.setFIToFICstmrCdtTrfInitn(fIToFICstmrCdtTrfInitnDetalle);
		
		fiToFiCustomerCreditTransferRequest.setParamIdentificacion(paramIdentificacion);
		fiToFiCustomerCreditTransferRequest.setSglbtr(sglbtr);
		
		String fiToFiCustomerCreditTransferRequestJSON;
		fiToFiCustomerCreditTransferRequestJSON = new Gson().toJson(fiToFiCustomerCreditTransferRequest);
		LOGGER.info(fiToFiCustomerCreditTransferRequestJSON);
		
		Resultado resultadoResponse = bcvlbtService.aporbarAltoBajoValor(fiToFiCustomerCreditTransferRequest, accion, request);
		LOGGER.info(resultadoResponse);
		return resultadoResponse;
	
		
	}
	
	
	
	@GetMapping("/searchIdOrdenante")
	public String searchIdOrdenante(@ModelAttribute("cceLbtrTransaccionDtoSearch") CceLbtrTransaccionDto cceLbtrTransaccionDto, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(TRANSACCIONMANUALFORMCONSULTARTRANSACCIONESCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBDAprobar)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
	
		return REDIRECTINDEX; 
	}
	
	public TransaccionRequest getTransaccionRequest(CceLbtrTransaccionDto cceLbtrTransaccionDto) throws CustomException{
		TransaccionRequest transaccionRequest = new TransaccionRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		transaccionRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		transaccionRequest.setIdUsuario(userName);
		transaccionRequest.setCodigoOperacion("D");
		transaccionRequest.setCuentaDesde(cceLbtrTransaccionDto.getCuentaEmisor());
		transaccionRequest.setCuentaHasta(cceLbtrTransaccionDto.getCuentaReceptor());
		transaccionRequest.setReferencia(cceLbtrTransaccionDto.getReferencia());
		transaccionRequest.setMontoTransaccion(getMontoFormato(cceLbtrTransaccionDto.getMonto()));
		transaccionRequest.setCedulaRif(cceLbtrTransaccionDto.getIdemEmisor());
		transaccionRequest.setNombreBeneneficiario(cceLbtrTransaccionDto.getNomReceptor());
		transaccionRequest.setCedulaRifBeneficiario(cceLbtrTransaccionDto.getIdemReceptor());
		transaccionRequest.setUsuarioCanal(getUsuarioCanal(userName));//usuario del sistema estar pendiente de la longitud
		transaccionRequest.setNombreBancoBeneneficiario(getBancoReceptor(cceLbtrTransaccionDto.getBancoReceptor()).getNbBanco());
		transaccionRequest.setDescripcion(cceLbtrTransaccionDto.getDescripcion());
		transaccionRequest.setCodigoTransaccion(cceLbtrTransaccionDto.getCodTransaccion());
		transaccionRequest.setCanal(cceLbtrTransaccionDto.getCanal());
		
		
		return transaccionRequest;
	}
	
	
	public String getUsuarioCanal(String usuario) {
		String usuarioCanal = "";
		if(usuario.length() > 8) {
			usuarioCanal = usuario.substring(0, 7);
		}else {
			usuarioCanal = usuario;
		}
		
		return usuarioCanal;
	}
	
	public String getMontoFormato(BigDecimal monto) {
		String montoString = libreriaUtil.bigDecimalToString(monto);
		
		montoString = montoString.replace(".", "");
		montoString = montoString.replace(",", "");
		return montoString;
	}
	
	public ParamIdentificacion getParamIdentificacion() {
		ParamIdentificacion paramIdentificacion = new ParamIdentificacion();
		paramIdentificacion.setIdSesion(libreriaUtil.obtenerIdSesionCce());
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		paramIdentificacion.setIdUsuario(userName);
		
		return paramIdentificacion;
	}
	
	public String getMsgId() {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < 28; i++) {
			bld.append("0");
		}
		return bld.toString();
	}
	
	public int getPage(int page, String bancoReceptor, String fechaHoy) {
		int valorPage = 0;
		
		if(page == 0) {
			valorPage = 0; 
		}else {
			Page<CceLbtrTransaccion> listaCceLbtrTransaccion;
			listaCceLbtrTransaccion =  cceLbtrTransaccionService.consultaLbtrTransaccionesAprobarFechas(bancoReceptor, 
					fechaHoy, page);
			if(listaCceLbtrTransaccion.isEmpty()) {
				valorPage = page - 1;
			}else {
				valorPage = page;
			}
		}
		return valorPage;
	}
	
	public void setModelError(Model model, CceLbtrTransaccionDto cceLbtrTransaccionDto, HttpServletRequest request) throws CustomException  {
	
		ClienteDatosBasicoRequest clienteDatosBasicoRequestSV = getClienteDatosBasicoRequest();
		clienteDatosBasicoRequestSV.setIp(request.getRemoteAddr());
		clienteDatosBasicoRequestSV.setNroIdCliente(cceLbtrTransaccionDto.getIdemEmisor());
		
		BancoRequest bancoRequestSV = getBancoRequest();
		CuentasConsultaRequest cuentasConsultaRequestSV = getCuentasConsultaRequest();
		cuentasConsultaRequestSV.setIpOrigen(request.getRemoteAddr());
		List<CuentaCliente> listaCuentaClienteSV;
		List<Banco> listaBancosSV;
		List<Banco> listaBancosEmisorSV;
		List<CceSubProducto> listaCceSubProductoSV;
		List<CceCodigosTransaccion> listaCceCodigosTransaccionSV;
		
		cuentasConsultaRequestSV.setIdCliente(cceLbtrTransaccionDto.getIdemEmisor());
		listaCuentaClienteSV = cuentasConsultaService.consultaCuentasCliente(cuentasConsultaRequestSV, CONSULTACUENTAS, request);
		model.addAttribute(LISTACUENTACLIENTE, listaCuentaClienteSV);
		listaBancosSV  = bancoService.listaBancos(bancoRequestSV);
		model.addAttribute(LISTABANCOS, listaBancosSV);
		listaBancosEmisorSV = getListaBancosEmisor();
		model.addAttribute(LISTABANCOSEMISOR, listaBancosEmisorSV);
		listaCceSubProductoSV = cceSubProductoService.listaSubproductos();
		model.addAttribute(LISTACCESUBPRODUCTO, listaCceSubProductoSV);
		listaCceCodigosTransaccionSV =cceCodigosTransaccionService.consultarTodosCodigosConTipo(4);
		model.addAttribute(LISTACCECODIGOSTRANSACCION, listaCceCodigosTransaccionSV);
		model.addAttribute(DESCRIPCION, cceLbtrTransaccionDto.getDescripcion());
		
   }	
		
		
	public Model setError(Model model, BindingResult result) {
		List<String> listaError = new ArrayList<>();
		 
		for (ObjectError error : result.getAllErrors()) {
			listaError.add(error.getDefaultMessage());
		}
		model.addAttribute(LISTAERROR, listaError);
		return model;
	}
	
	public void asignarValores(CceLbtrTransaccionDto cceLbtrTransaccionDto) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		cceLbtrTransaccionDto.setUsuarioCreador(userName.toLowerCase());
		cceLbtrTransaccionDto.setReferencia(referenciaConfiguracion);
		cceLbtrTransaccionDto.setCanal(getCanal(cceLbtrTransaccionDto.getIdemEmisor()));
		cceLbtrTransaccionDto.setCodTransaccion(getCodTransaccion(cceLbtrTransaccionDto.getIdemEmisor()));
		cceLbtrTransaccionDto.setProducto(codProdcutoConfiguracion);
		cceLbtrTransaccionDto.setStatus("I");
		cceLbtrTransaccionDto.setFechaValor(libreriaUtil.getFechaDate(cceLbtrTransaccionDto.getFechaValorString()));
		cceLbtrTransaccionDto.setMonto(new BigDecimal(cceLbtrTransaccionDto.getMontoString()).setScale(2, RoundingMode.HALF_UP));
	}
		
	public String getCodTransaccion(String nroIdEmisor) {
    	char valor = nroIdEmisor.charAt(0);
    
    	if(valor == 'J' || valor == 'G' || valor == 'C') {
    		return codTransaccionPersonaJuridica;
    	}else {
    		if(valor == 'V' || valor == 'E' || valor == 'P') {
    			return codTransaccionPersonaNatural;
    		}else {
    			return "NO-ASIGNADO";
    		}
    	}
    }
	
	public String getCanal(String nroIdEmisor) {
    	char valor = nroIdEmisor.charAt(0);
    
    	if(valor == 'J' || valor == 'G' || valor == 'C') {
    		return canalJuridicoConfiguracion;
    	}else {
    		if(valor == 'V' || valor == 'E' || valor == 'P') {
    			return canalNaturalConfiguracion;
    		}else {
    			return "NO-ASIGNADO";
    		}
    	}
    }
	
	
	public String convertirFechaString(Date fecha) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(fecha);
	}
	
	public String obtenerFechaFormateada(Date fecha, String formato) {
	    SimpleDateFormat sdf = new SimpleDateFormat(formato);
	    return sdf.format(fecha);
	}
	
	public Date fechaHoy() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATOFECHAINICIALIZAR);
		try {
			return sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
		public List<Banco> getListaBancosEmisor(){
			List<Banco> listaBancosEmisor = new ArrayList<>();
			Banco bancoEmisor = new Banco();
			bancoEmisor.setCodBanco(codBancoEmisorConfiguracion);
			bancoEmisor.setNbBanco(bancoEmisorConfiguracion);
			listaBancosEmisor.add(bancoEmisor);
			
			return listaBancosEmisor;
		}
		
		public BancoRequest getBancoRequest() {
			BancoRequest bancoRequestTM = new BancoRequest();
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			bancoRequestTM.setIdUsuario(userName);
			bancoRequestTM.setIdSesion(libreriaUtil.obtenerIdSesion());
			return bancoRequestTM;
		}
		
		public Banco getBancoReceptor(String codBanco) throws CustomException{
			
			BancoRequest bancoRequest = getBancoRequest();
			bancoRequest.setCodBanco(codBanco);
			return bancoService.buscarBanco(bancoRequest);
			
		}
		
		public ClienteDatosBasicoRequest getClienteDatosBasicoRequest() {
			ClienteDatosBasicoRequest clienteDatosBasicoRequestTM = new ClienteDatosBasicoRequest();
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			clienteDatosBasicoRequestTM.setIdUsuario(userName);
			clienteDatosBasicoRequestTM.setIdSesion(libreriaUtil.obtenerIdSesion());
			clienteDatosBasicoRequestTM.setCodUsuario(userName);
			clienteDatosBasicoRequestTM.setCanal(canal);
			return clienteDatosBasicoRequestTM;
		}
		
		public CuentasConsultaRequest getCuentasConsultaRequest() {
			CuentasConsultaRequest cuentasConsultaRequest = new CuentasConsultaRequest();
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			cuentasConsultaRequest.setIdUsuario(userName);
			cuentasConsultaRequest.setIdCanal(canal);
			cuentasConsultaRequest.setIdConsumidor("12345");
			cuentasConsultaRequest.setIdTerminal("monitor_financiero");
			return cuentasConsultaRequest;
		}
		
		
		public Page<CceLbtrTransaccion> convertirLista(Page<CceLbtrTransaccion> listaCceLbtrTransaccion){
			
			for (CceLbtrTransaccion cceLbtrTransaccion : listaCceLbtrTransaccion) {
				cceLbtrTransaccion.setMontoString(libreriaUtil.formatNumber(cceLbtrTransaccion.getMonto()));
			}
			
			return listaCceLbtrTransaccion;
		}
		
		@ModelAttribute
		public void setGenericos(Model model, HttpServletRequest request) {
			
			CceLbtrTransaccionDto cceLbtrTransaccionDtoSearch = new CceLbtrTransaccionDto();
			model.addAttribute("cceLbtrTransaccionDtoSearch", cceLbtrTransaccionDtoSearch);
			
			LOGGER.info(request.getRequestURI());
	        String titulo = getTitulo(request.getRequestURI());
			
			String[] arrUriP = new String[2]; 
			arrUriP[0] = "Home";
			arrUriP[1] = "CCE - "+titulo;
			model.addAttribute("arrUri", arrUriP);
		}
		
		
		public String getTitulo(String strLbtr) {
			String tituloLbtr= "No Asignado";
			String[] arrOfStrLbtr = strLbtr.split("/");
			 
			
	        int ultimo = arrOfStrLbtr.length - 1;
	        
	        if(ultimo > 0) {
	        	String metodo = arrOfStrLbtr[ultimo];
	        	if(metodo.equals(INDEX) || metodo.equals(CONSULTAPAGE) || metodo.equals(FORMBUSCARORDENANTE) || 
	        			metodo.equals(SEARCHCREAR) || metodo.equals(DETALLE) || metodo.equals(EDIT) || metodo.equals(SAVE)) {
	        		tituloLbtr = "Transacción Alto Valor (Interbancaria)";
	        	}else {
	        		tituloLbtr = "Aprobación Alto Valor (Canales Internos)";
	        	}
	        	
	        }
			
	        return tituloLbtr;
			
		}
		
		public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
			try {
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TRANSACCIONMANUAL, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAF);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		
		public void guardarAuditoriaIdCliente(String accion, boolean resultado, String codRespuesta,  String respuesta, String idCliente, HttpServletRequest request) {
			try {
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TRANSACCIONMANUAL, accion, codRespuesta, resultado, respuesta+" Cliente:[idCliente="+idCliente+"]", request.getRemoteAddr());
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAF);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		
		public void guardarAuditoriaIdTransaccion(String accion, boolean resultado, String codRespuesta,  String respuesta, int idTransaccion, HttpServletRequest request) {
			try {
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TRANSACCIONMANUAL, accion, codRespuesta, resultado, respuesta+" LbtrTransacciones:[id="+idTransaccion+"]", request.getRemoteAddr());
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAF);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		
		public void guardarAuditoriaTransaccionManualConsulta(String accion, boolean resultado, String codRespuesta,  String respuesta, CceLbtrTransaccionDto cceLbtrTransaccionDto, HttpServletRequest request) {
			try {
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TRANSACCIONMANUAL, accion, codRespuesta, resultado, respuesta+" LbtrTransacciones:[bancoReceptor="+cceLbtrTransaccionDto.getBancoReceptor()+""
								+ ", fecha="+cceLbtrTransaccionDto.getFechaHoy()+"]", request.getRemoteAddr());
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAF);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		
		public void guardarAuditoriaTransaccionManual(String accion, boolean resultado, String codRespuesta,  String respuesta, CceLbtrTransaccionDto cceLbtrTransaccionDto, HttpServletRequest request) {
			try {
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAI);
				auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
						TRANSACCIONMANUAL, accion, codRespuesta, resultado, respuesta+" LbtrTransacciones:[id="+cceLbtrTransaccionDto.getId()+""
								+ ", referencia="+cceLbtrTransaccionDto.getReferencia()+", codTransaccion="+cceLbtrTransaccionDto.getCodTransaccion()+""
								+ ", monto="+cceLbtrTransaccionDto.getMonto()+", moneda="+cceLbtrTransaccionDto.getMoneda()+""
								+ ", producto="+cceLbtrTransaccionDto.getProducto()+", SubProducto="+cceLbtrTransaccionDto.getSubProducto()+""
								+ ", bancoEmisor="+cceLbtrTransaccionDto.getBancoEmisor()+", cuentaEmisor="+cceLbtrTransaccionDto.getCuentaEmisor()+""
								+ ", cuentaUnicaEmisor="+cceLbtrTransaccionDto.getCuentaUnicaEmisor()+", bancoReceptor="+cceLbtrTransaccionDto.getBancoReceptor()+""
								+ ", cuentaReceptor="+cceLbtrTransaccionDto.getCuentaReceptor()+", cuentaUnicaReceptor="+cceLbtrTransaccionDto.getCuentaUnicaReceptor()+""
								+ ", idemEmisor="+cceLbtrTransaccionDto.getIdemEmisor()+", nomEmisor="+cceLbtrTransaccionDto.getNomEmisor()+""
								+ ", idemReceptor="+cceLbtrTransaccionDto.getIdemReceptor()+", nomReceptor="+cceLbtrTransaccionDto.getNomReceptor()+""
								+ ", status="+cceLbtrTransaccionDto.getStatus()+", fechaValor="+cceLbtrTransaccionDto.getFechaValor()+""
								+ ", usuarioCreador="+cceLbtrTransaccionDto.getUsuarioCreador()+", fechaValor="+cceLbtrTransaccionDto.getFechaValor()+""
								+ ", descripcion="+cceLbtrTransaccionDto.getDescripcion()+"]", request.getRemoteAddr());
				LOGGER.info(TRANSACCIONMANUALFUNCIONAUDITORIAF);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
}
