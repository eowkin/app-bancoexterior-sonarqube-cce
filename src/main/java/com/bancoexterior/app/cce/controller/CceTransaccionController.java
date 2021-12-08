package com.bancoexterior.app.cce.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.cce.dto.AprobacionRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.CceTipoTransaccionDto;
import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.dto.Sglbtr;
import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.model.CceTipoTransaccion;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.model.Cuenta;
import com.bancoexterior.app.cce.model.DatosAprobacion;
import com.bancoexterior.app.cce.model.DatosPaginacion;
import com.bancoexterior.app.cce.model.FIToFICstmrCdtTrfInitnDetalle;
import com.bancoexterior.app.cce.model.Filtros;
import com.bancoexterior.app.cce.model.GrpHdrObject;
import com.bancoexterior.app.cce.model.Identificacion;
import com.bancoexterior.app.cce.model.Moneda;
import com.bancoexterior.app.cce.model.ParamIdentificacion;
import com.bancoexterior.app.cce.model.PmtInfObject;
import com.bancoexterior.app.cce.service.IBancoService;
import com.bancoexterior.app.cce.service.IBcvlbtService;
import com.bancoexterior.app.cce.service.ICceCuentasUnicasBcvService;
import com.bancoexterior.app.cce.service.ICceMontoMaximoAproAutoService;
import com.bancoexterior.app.cce.service.ICceTipoTransaccionService;
import com.bancoexterior.app.cce.service.ICceTransaccionService;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;


@Controller
@RequestMapping("/ccetransacciones")
public class CceTransaccionController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceTransaccionController.class);
	
	@Autowired
	private ICceTransaccionService service;
	
	@Autowired
	private IBancoService bancoService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Autowired
	private Mapper mapper;
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	
	
	@Value("${${app.ambiente}"+".trasacciones.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	@Value("${${app.ambiente}"+".trasacciones.montoTopeMaximoAproAuto}")
    private BigDecimal montoTopeMaximoAproAuto;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	private static final String URLFORMCONSULTAMOVIMIENTOS = "cce/formConsultarMovimientosAltoBajoValor";
	
	@Autowired
	private ICceMontoMaximoAproAutoService montoMaximoAproAutoService; 
	
	@Autowired
	private ICceTipoTransaccionService tipoTransaccionService;
	
	@Autowired
	private ICceCuentasUnicasBcvService cceCuentasUnicasBcvService;
	
	private static final String LISTATRANSACCIONES = "listaTransacciones";
	
	@Autowired
	private IBcvlbtService bcvlbtService;
	
	@Value("${${app.ambiente}"+".cce.consultaMovimientos.valorBD}")
    private int valorConsultaMovimientosBD;
	
	@Value("${${app.ambiente}"+".cce.aprobacionOperacionAltoValor.valorBD}")
    private int valorAprobacionOperacionAltoValorBD;
	
	@Value("${${app.ambiente}"+".cce.aprobacionOperacionLoteAutomatico.valorBD}")
    private int valorOperacionLoteAutomaticoBD;
	
	private static final String LISTABANCOS = "listaBancos";
	
	private static final String LISTATIPOTRANSACCION = "listaTipoTransaccion"; 
	
	private static final String LISTAERROR = "listaError";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAMOVIMIENTOSI = "[==== INICIO FormConsultaMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAMOVIMIENTOSF = "[==== FIN FormConsultaMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String TIPOTRANSACCION = "tipoTransaccion";
	
	private static final String BANCODESTINO = "bancoDestino";
	
	private static final String NUMEROIDENTIFICACION = "numeroIdentificacion";
	
	private static final String FECHADESDE = "fechaDesde";
	
	private static final String FECHAHASTA = "fechaHasta";	
	
	private static final String SELECCION = "seleccion";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAMOVIMIENTOSI = "[==== INICIO ProcesarConsultaMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAMOVIMIENTOSF = "[==== FIN ProcesarConsultaMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORI = "[==== INICIO ConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORF = "[==== FIN ConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String LISTATRANSACCIONESEXCEL = "listaTransacciones";
	
	private static final String MENSAJENORESULTADO = "Operación Exitosa.La consulta no arrojo resultado.";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son inválidos";
	
	private static final String URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE = "cce/listaMovimientosConsultaAltoBajoValorPaginate";
	
	private static final String CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSI = "[==== INICIO VerDeatlleMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSF = "[==== FIN VerDetalleMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String URLFORMMOVIMIENTOSALTOBAJOVALORDETALLEFECHAS = "cce/formMovimientoAltoBajoValorDetalleFechas";
	
	private static final String CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOI = "[==== INICIO FormAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOF = "[==== FIN FormAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String MENSAJENORESULTADOLOTE = "No se encontraron operaciones Alto Valor Lote que procesar.";
	
	private static final String LISTABCVLBTPORAPROBAR = "listaBCVLBTPorAprobar";
	
	private static final String URLFORMAPROBARALTOVALORLOTEAUTOMATICO = "cce/formAprobarAltoValorLoteAutomatico";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO FormConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN FormConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR = "cce/formConsultarOperacionesAprobarAltoBajoValor";
	
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO ProcesarConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN ProcesarConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String NUMEROAPROBACIONESLOTES = "numeroAprobacionesLotes";
	
	private static final String MONTOAPROBACIONESLOTES = "montoAprobacionesLotes";
	
	private static final String MENSAJEFUERARANGO = "El monto a consultar esta fuera de rango Alto Valor Lote Automático.";
	
	private static final String MENSAJEMONTOSINVALIDAS = "Los valores de los montos son inválidos";
	
	private static final String DATOSPAGINACION = "datosPaginacion";
	
	private static final String MONTODESDE = "montoDesde";
	
	private static final String MONTOHASTA = "montoHasta";
	
	private static final String BANCOEMISOR = "bancoEmisor";
	
	private static final String NROIDEMISOR = "nroIdEmisor";
	
	private static final String LISTABCVLBTPORAPROBARSELECCION = "listaBCVLBTPorAprobarSeleccion";
	
	private static final String URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE = "cce/listaOperacionesPorAporbarAltoValorPaginate";
	
	private static final String SELECCIONADOS = "selecionados";
	
	private static final String SELECCIONADOSBOTON = "selecionadosBoton";
	
	private static final String MONTOAPROBAROPERACIONESSELECCIONADOS = "montoAprobarOperacionesSeleccionadas";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO ConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN ConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERSELECCIONARTODASAPROBARALTOVALORI = "[==== INICIO SeleccionarTodasOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERSELECCIONARTODASAPROBARALTOVALORF = "[==== FIN SeleccionarTodasOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERDESELECCIONARTODASAPROBARALTOVALORI = "[==== INICIO DeseleccionarTodasOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERDESELECCIONARTODASAPROBARALTOVALORF = "[==== FIN DeseleccionarTodasOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERSELECCIONARUNAAPROBARALTOVALORI = "[==== INICIO SeleccionarUnaOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERSELECCIONARUNAAPROBARALTOVALORF = "[==== FIN SeleccionarUnaOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERDESELECCIONARUNAAPROBARALTOVALORI = "[==== INICIO DeseleccionarUnaOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERDESELECCIONARUNAAPROBARALTOVALORF = "[==== FIN DeseleccionarUnaOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOI = "[==== INICIO ProcesarAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOF = "[==== FIN ProcesarAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTESELECCIONI = "[==== INICIO ProcesarAprobarAltoValorLoteSeleccion CceTransaccion - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTESELECCIONF = "[==== FIN ProcesarAprobarAltoValorLoteSeleccion CceTransaccion - Controller ====]";
	
	private static final String BALNK = "";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAI = "[==== INICIO Guardar Auditoria  Cce - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAF = "[==== FIN Guardar Auditoria  Cce - Controller ====]";
	
	private static final String CCE = "Cce";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operación Exitosa.";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operación Fallida.";
	
	private static final String PROCESARCONSULTAMOVIMIENTOS = "procesarConsultaMovimientos";
	
	private static final String CONSULTAMOVIMIENTOSPAGE = "consultaMovimientosPage";
	
	private static final String DETALLEMOVIMIENTO = "detalleMovimiento";
	
	private static final String FORMCONSULTAMOVIMIENTOS = "formConsultaMovimientos";
	
	private static final String FORMCONSULTAOPERACIONESAPROBAR = "formConsultaOperacionesAprobar";
	
	private static final String FORMAPROBARAUTOMATICO = "formAprobarLoteAutomatico";
	
	private static final String URLRESULTADOAPROBARSELECCION = "cce/resultadoAprobarSeleccion";
	
	private static final String URLRESULTADOAPROBARAUTOMATO = "cce/resultadoAprobarAutomatico";
	
	private static final String CONSULTAOPERACIONESAPROBARPAGE = "consultaOperacionesAprobarPage";
	
	private static final String SELECCIONARTODOSAPROBAROPERACIONES = "seleccionarTodosAprobarOperaciones";
	
	private static final String DESELECCIONARTODOSAPROBAROPERACIONES = "deseleccionarTodosAprobarOperaciones";
	
	private static final String SELECCIONARUNAAPROBAROPERACIONES = "seleccionarUnaAprobarOperaciones";
	
	private static final String DESELECCIONARUNAAPROBAROPERACIONES = "deseleccionarUnaAprobarOperaciones";
	
	private static final String PROCESARCONSULTAOPERACIONESAPROBAR = "procesarConsultaOperacionesAprobar";
		
	private static final String PROCESARAPROBARLOTEAUTOMATICO = "procesarAprobarLoteAutomatico";
	
	private static final String PROCESARAPROBARLOTESELECCION = "procesarAprobarLoteSeleccion";
	
	private static final String TRANSACCIONREFERENCIA = "Transaccion referencia: ";
	
	private static final String FLECHA = " --> ";
	
	
	@GetMapping("/cargarLoader")
	public String cargarLoaderPaginado() {
		
		LOGGER.info("Cargar");   
		return "/index";
	}
	
	@GetMapping("/formConsultaMovimientos")
	public String formConsultaMovimientosAltoBajoValor(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAMOVIMIENTOSI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaMovimientosBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			
			List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
			model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute(LISTABANCOS, listaBancos);
			guardarAuditoria(FORMCONSULTAMOVIMIENTOS, true, "0000", MENSAJEOPERACIONEXITOSA, request);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
			guardarAuditoria(FORMCONSULTAMOVIMIENTOS, false, "0001", MENSAJEOPERACIONFALLIDA+" "+e.getMessage(), request);
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAMOVIMIENTOSF);
		return URLFORMCONSULTAMOVIMIENTOS;
		
	}
	
	@GetMapping("/procesarConsultaMovimientos")
	public String procesarConsultaMovimientosAltoBajoValorPageable(CceTransaccionDto cceTransaccionDto, 
			Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAMOVIMIENTOSI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaMovimientosBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			
			List<String> listaError = new ArrayList<>();
			Page<CceTransaccion> listaTransacciones;
			
			
			if(libreriaUtil.isFechaValidaDesdeHasta(cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta())){
				
				listaTransacciones = service.consultaMovimientosConFechasPageFinal(cceTransaccionDto.getTipoTransaccionInt(), cceTransaccionDto.getBancoDestino(),
						cceTransaccionDto.getNumeroIdentificacion(),cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta(), 0);
				listaTransacciones = convertirLista(listaTransacciones);
				
				
				
				if(listaTransacciones.isEmpty()) {
					model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
					List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
					model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
					List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
					model.addAttribute(LISTABANCOS, listaBancos);
					guardarAuditoriaCceTransaccionDto(PROCESARCONSULTAMOVIMIENTOS, true, "0001",  MENSAJENORESULTADO, cceTransaccionDto, request);
					return URLFORMCONSULTAMOVIMIENTOS;
				}
				
				Page<CceTransaccion> listaTransaccionesExcelPageable = service.consultaMovimientosConFechasPageExcel(cceTransaccionDto.getTipoTransaccionInt(), cceTransaccionDto.getBancoDestino(),
						cceTransaccionDto.getNumeroIdentificacion(),cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta(), 0);
				
				List<CceTransaccion> listaTransaccionesExcel = listaTransaccionesExcelPageable.getContent();
				
				List<CceTransaccionDto> listaTransaccionesExcelDto = getListaTransaccionesDto(listaTransaccionesExcel);
				httpSession.setAttribute(LISTATRANSACCIONESEXCEL, listaTransaccionesExcelDto);
				
				
				model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
				model.addAttribute(TIPOTRANSACCION, cceTransaccionDto.getTipoTransaccionInt());
				model.addAttribute(BANCODESTINO, cceTransaccionDto.getBancoDestino());
				model.addAttribute(NUMEROIDENTIFICACION, cceTransaccionDto.getNumeroIdentificacion());
				model.addAttribute(FECHADESDE, cceTransaccionDto.getFechaDesde());
				model.addAttribute(FECHAHASTA, cceTransaccionDto.getFechaHasta());
				model.addAttribute(SELECCION, getSeleccion2(cceTransaccionDto.getTipoTransaccionInt()));
				guardarAuditoriaCceTransaccionDto(PROCESARCONSULTAMOVIMIENTOS, true, "0000",  MENSAJEOPERACIONEXITOSA, cceTransaccionDto, request);
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAMOVIMIENTOSF);
				return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
					
			}else {
				listaError.add(MENSAJEFECHASINVALIDAS);
				model.addAttribute(LISTAERROR, listaError);
				List<CceTipoTransaccion> listaTipoTransaccion = tipoTransaccionService.findAll();
				model.addAttribute(LISTATIPOTRANSACCION, listaTipoTransaccion);
				List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
				model.addAttribute(LISTABANCOS, listaBancos);
				return URLFORMCONSULTAMOVIMIENTOS;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
			guardarAuditoriaCceTransaccionDto(PROCESARCONSULTAMOVIMIENTOS, false, "0001",  e.getMessage(), cceTransaccionDto, request);
			return URLFORMCONSULTAMOVIMIENTOS;
		}
		
		
		
	}
	
	
	
	public String getSeleccion2(int tipoTransaccion) {
		CceTipoTransaccionDto cceTipoTransaccionDto = tipoTransaccionService.findById(tipoTransaccion);
		if(cceTipoTransaccionDto != null) {
			return cceTipoTransaccionDto.getDescripcion();
		}else {
			return "Todas";
		}
		
	}
	
	
	
	@GetMapping("/consultaMovimientosPage")
	public String consultaMovimientosAltoBajoValorPageable(@RequestParam("tipoTransaccion") int tipoTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, int page, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaMovimientosBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		Page<CceTransaccion> listaTransacciones;
		
		if(libreriaUtil.isFechaValidaDesdeHasta(fechaDesde, fechaHasta)){
			listaTransacciones = service.consultaMovimientosConFechasPageFinal(tipoTransaccion, bancoDestino, numeroIdentificacion,
							                                          fechaDesde, fechaHasta, page);
			
			listaTransacciones = convertirLista(listaTransacciones);
			
			if(listaTransacciones.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			}
			model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
			model.addAttribute(TIPOTRANSACCION, tipoTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			model.addAttribute(SELECCION, getSeleccion2(tipoTransaccion));
				
		}else {
			LOGGER.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORF);
		return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
	}
	
	
	
	
	
	@GetMapping("/detalleMovimiento")
	public String verMovimineto(@RequestParam("endtoendId") String endtoendId,@RequestParam("tipoTransaccion") int tipoTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, int page, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaMovimientosBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		CceTransaccionDto cceTransaccionDto = service.findByEndtoendId(endtoendId);
		if(cceTransaccionDto != null) {	
			cceTransaccionDto.setEndtoendId(endtoendId);
			if(!cceTransaccionDto.isEnvio()) {
				LOGGER.info(cceTransaccionDto.getCodTransaccion());
				String cuentaOrigen = cceTransaccionDto.getCuentaOrigen();
				String cuentaDestino = cceTransaccionDto.getCuentaDestino();
				cceTransaccionDto.setCuentaOrigen(cuentaDestino);
				cceTransaccionDto.setCuentaDestino(cuentaOrigen);
				String numeroIdentificacionCce = cceTransaccionDto.getNumeroIdentificacion();
				String numeroIdentificacionDestinoCce = cceTransaccionDto.getNumeroIdentificacionDestino();
				cceTransaccionDto.setNumeroIdentificacion(numeroIdentificacionDestinoCce);
				cceTransaccionDto.setNumeroIdentificacionDestino(numeroIdentificacionCce);
				String beneficiarioOrigen = cceTransaccionDto.getBeneficiarioOrigen();
				String beneficiarioDestino = cceTransaccionDto.getBeneficiarioDestino();
				cceTransaccionDto.setBeneficiarioOrigen(beneficiarioDestino);
				cceTransaccionDto.setBeneficiarioDestino(beneficiarioOrigen);
			}
			
			cceTransaccionDto.setNombreTransaccion(nombreTransaccion(cceTransaccionDto.getCodTransaccion())+"-"+cceTransaccionDto.getCodTransaccion());
			cceTransaccionDto.setNombreEstadoBcv(nombreEstadoBcv(cceTransaccionDto.getEstadobcv()));
			cceTransaccionDto.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto())));
			cceTransaccionDto.setMontoString(libreriaUtil.formatNumber(cceTransaccionDto.getMonto()));
			model.addAttribute("cceTransaccionDto", cceTransaccionDto);
			model.addAttribute(TIPOTRANSACCION, tipoTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			model.addAttribute("endtoendId", endtoendId);
			model.addAttribute("page", page);
			guardarAuditoriaId(DETALLEMOVIMIENTO, true, "0000", MENSAJEOPERACIONEXITOSA, endtoendId, request);
			LOGGER.info(CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSF);
			return URLFORMMOVIMIENTOSALTOBAJOVALORDETALLEFECHAS;
		}else {
			Page<CceTransaccion> listaTransacciones;
			listaTransacciones = service.consultaMovimientosConFechasPageFinal(tipoTransaccion, bancoDestino, numeroIdentificacion,
					fechaDesde, fechaHasta, page);
			listaTransacciones = convertirLista(listaTransacciones);
			model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
			model.addAttribute(TIPOTRANSACCION, tipoTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			guardarAuditoriaId(DETALLEMOVIMIENTO, true, "0001", MENSAJENORESULTADO, endtoendId, request);
			return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
			 
		}
		
		
		
	}
	
	
	
	@GetMapping("/formConsultaOperacionesAprobar")
	public String formConsultaOperacionesAprobarAltoBajoValor(CceTransaccionDto cceTransaccionDto, Model model, 
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute(LISTABANCOS, listaBancos);
			guardarAuditoria(FORMCONSULTAOPERACIONESAPROBAR, true, "0000", MENSAJEOPERACIONEXITOSA, request);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			guardarAuditoria(FORMCONSULTAOPERACIONESAPROBAR, false, "0001", MENSAJEOPERACIONFALLIDA+" "+e.getMessage(), request);
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORF);
		return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		
	}
	
	
	@GetMapping("/procesarConsultaOperacionesAprobar")
	public String procesarConsultaOperacionesAprobarAltoBajoValorPageable(CceTransaccionDto cceTransaccionDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<Banco> listaBancos = new ArrayList<>();
		
		try {
			CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
			BancoRequest bancoRequest = getBancoRequest();
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
			if (result.hasErrors()) {
				hasError(model, result, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
				
				
			if(libreriaUtil.isMontoDesdeMontoHastaDistintoNull(cceTransaccionDto.getMontoDesde(), cceTransaccionDto.getMontoHasta())) {
				if(libreriaUtil.montoSerch(cceTransaccionDto.getMontoDesde()).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					setModelErrorMensaje(model, MENSAJEFUERARANGO, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(cceTransaccionDto.getMontoHasta().compareTo(cceTransaccionDto.getMontoDesde()) < 0) { 
					setModelErrorMensaje(model, MENSAJEMONTOSINVALIDAS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				
				filtros.setMontoDesde(cceTransaccionDto.getMontoDesde());
				filtros.setMontoHasta(cceTransaccionDto.getMontoHasta());
			}else {
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			if(libreriaUtil.isFechaHoraValidaDesdeHasta(cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta())) {
				filtros.setFechaDesde(getFechaHoraDesdeFormato(cceTransaccionDto.getFechaDesde()));
				filtros.setFechaHasta(getFechaHoraHastaFormato(cceTransaccionDto.getFechaHasta()));	
			}else {
				setModelErrorMensaje(model, MENSAJEFECHASINVALIDAS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
				
			setFiltrosBancoDestino(cceTransaccionDto.getBancoDestino(), filtros);
			setFiltrosNumeroIdentificacion(cceTransaccionDto.getNumeroIdentificacion(), filtros);
			
			
			List<BCVLBT> listaBCVLBTPorAprobar;
			DatosPaginacion datosPaginacion;
			AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
			aprobacionesConsultasRequest.setNumeroPagina(1);   
			aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setFiltros(filtros);
			
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, PROCESARCONSULTAOPERACIONESAPROBAR, request);
			
			listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				setModelConvertirListaBCVLT(model, listaBCVLBTPorAprobar, datosPaginacion, httpSession);
				modelAprobacionesConsultasResponsePagina(model, false, false);
				modelAprobacionesConsultasResponseConsulta(model, cceTransaccionDto.getMontoDesde(), cceTransaccionDto.getMontoHasta(), cceTransaccionDto.getBancoDestino(), 
						cceTransaccionDto.getNumeroIdentificacion(), cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta());
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				setModelErrorMensaje(model, MENSAJENORESULTADO, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
				
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setModelErrorMensaje(model, e.getMessage(), listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
				
		
	}
	
	public void setModelErrorMensaje(Model model, String mjs, List<Banco> listaBancos) {
		List<String> listaError = new ArrayList<>();
		listaError.add(mjs);
		model.addAttribute(LISTAERROR, listaError);
		model.addAttribute(LISTABANCOS, listaBancos);
	}
	
	public void setModelConvertirListaBCVLT(Model model, List<BCVLBT> listaBCVLBTPorAprobar, DatosPaginacion datosPaginacion, HttpSession httpSession) {
		listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
		listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosFalse(listaBCVLBTPorAprobar);
		httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
		BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
		model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
		model.addAttribute(DATOSPAGINACION,datosPaginacion);
		model.addAttribute(MONTOAPROBAROPERACIONESSELECCIONADOS, libreriaUtil.formatNumber(montoAprobarOperacionesSeleccionadas));
	}
	
	public void setModelCceTransaccionDtoMostrar(Model model, HttpServletRequest request) throws CustomException{
		CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar(request);
		model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
		model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
	}
	
	
	@GetMapping("/consultaOperacionesAprobarPage")
	public String consultaOperacionesAprobarAltoBajoValorPageable(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		BancoRequest bancoRequest = getBancoRequest();
		List<Banco> listaBancos = new ArrayList<>();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					setModelErrorMensaje(model, MENSAJEFUERARANGO, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					setModelErrorMensaje(model, MENSAJEMONTOSINVALIDAS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}

				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
					filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));	    
				
			}else {
				setModelErrorMensaje(model, MENSAJEFECHASINVALIDAS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
					
			setFiltrosBancoDestino(bancoEmisor, filtros);
			setFiltrosNumeroIdentificacion(nroIdEmisor, filtros);		
	
			
			AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setFiltros(filtros);
			
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, CONSULTAOPERACIONESAPROBARPAGE, request);
			List<BCVLBT> listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			DatosPaginacion datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				setModelConvertirListaBCVLT(model, listaBCVLBTPorAprobar, datosPaginacion, httpSession);
				modelAprobacionesConsultasResponsePagina(model, false, false);
				modelAprobacionesConsultasResponseConsulta(model, montoDesde, montoHasta, bancoEmisor, nroIdEmisor, fechaDesde, fechaHasta);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				setModelErrorMensaje(model, MENSAJENORESULTADO, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
				
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setModelErrorMensaje(model, e.getMessage(), listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	@GetMapping("/seleccionarTodosAprobarOperaciones")
	public String seleccionarTodosAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARTODASAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		BancoRequest bancoRequest = getBancoRequest();
		List<Banco> listaBancos = new ArrayList<>();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					setModelErrorMensaje(model, MENSAJEFUERARANGO, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					setModelErrorMensaje(model, MENSAJEMONTOSINVALIDAS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			
			if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
				filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
				filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));
			}else {
				setModelErrorMensaje(model, MENSAJEFECHASINVALIDAS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
					
			setFiltrosBancoDestino(bancoEmisor, filtros);
			setFiltrosNumeroIdentificacion(nroIdEmisor, filtros);
					
			AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setFiltros(filtros);
			
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, SELECCIONARTODOSAPROBAROPERACIONES, request);
			List<BCVLBT> listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			DatosPaginacion datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosTrue(listaBCVLBTPorAprobar);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				modelAprobacionesConsultasResponsePagina(model, datosPaginacion, true, true,montoAprobarOperacionesSeleccionadas);
				modelAprobacionesConsultasResponseConsulta(model, montoDesde, montoHasta, bancoEmisor, nroIdEmisor, fechaDesde, fechaHasta);
				LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARTODASAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				setModelErrorMensaje(model, MENSAJENORESULTADO, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARTODASAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}							
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setModelErrorMensaje(model, e.getMessage(), listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	@GetMapping("/deseleccionarTodosAprobarOperaciones")
	public String deseleccionarTodosAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARTODASAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		BancoRequest bancoRequest = getBancoRequest();
		List<Banco> listaBancos = new ArrayList<>();
		
		try {
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
			
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					setModelErrorMensaje(model, MENSAJEFUERARANGO, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					setModelErrorMensaje(model, MENSAJEMONTOSINVALIDAS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				setFiltrosMontosNotNull(montoDesde, montoHasta, filtros);
				
			}else {
				setFiltrosMontosNull(cceMontoMaximoAproAuto, filtros);
				
			}
			

			if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
				setFiltrosFechas(fechaDesde, fechaHasta, filtros);
			}else {
				setModelErrorMensaje(model, MENSAJEFECHASINVALIDAS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
					
			setFiltrosBancoDestino(bancoEmisor, filtros);
			setFiltrosNumeroIdentificacion(nroIdEmisor, filtros);
					
			AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
			setAprobacionesConsultasRequestFiltrosPage(page, filtros, aprobacionesConsultasRequest);
			
			
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, DESELECCIONARTODOSAPROBAROPERACIONES, request);
			List<BCVLBT> listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			DatosPaginacion datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosFalse(listaBCVLBTPorAprobar);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				modelAprobacionesConsultasResponsePagina(model, datosPaginacion, false, false,montoAprobarOperacionesSeleccionadas);
				modelAprobacionesConsultasResponseConsulta(model, montoDesde, montoHasta, bancoEmisor, nroIdEmisor, fechaDesde, fechaHasta);
				LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARTODASAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				setModelErrorMensaje(model, MENSAJENORESULTADO, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARTODASAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}				
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setModelErrorMensaje(model, e.getMessage(), listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	@GetMapping("/seleccionarUnaAprobarOperaciones")
	public String seleccionarUnaAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, @RequestParam("referencia") String referencia, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARUNAAPROBARALTOVALORI);
		List<Banco> listaBancos = new ArrayList<>();
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		BancoRequest bancoRequest = getBancoRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
					
			if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
				filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
				filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));
			}else {
				setModelErrorMensaje(model, MENSAJEFECHASINVALIDAS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
			
			if(montoDesde != null && montoHasta != null) {
				if(montoHasta.compareTo(montoDesde) < 0) { 
					setModelErrorMensaje(model, MENSAJEMONTOSINVALIDAS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					setModelErrorMensaje(model, MENSAJEFUERARANGO, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			
			
			
			setFiltrosBancoDestino(bancoEmisor, filtros);
			setFiltrosNumeroIdentificacion(nroIdEmisor, filtros);
					
			AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
			setAprobacionesConsultasRequestFiltrosPage(page, filtros, aprobacionesConsultasRequest);
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, SELECCIONARUNAAPROBAROPERACIONES, request);
			
			
			List<BCVLBT> listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			DatosPaginacion datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosUnaTrue(listaBCVLBTPorAprobar, referencia, httpSession);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				modelAprobacionesConsultasResponsePagina(model, datosPaginacion, buscarValorListaBCVLTSeleccionadosBotonSelecionar(listaBCVLBTPorAprobar), 
				buscarValorListaBCVLTSeleccionadosBoton(listaBCVLBTPorAprobar),montoAprobarOperacionesSeleccionadas);
				modelAprobacionesConsultasResponseConsulta(model, montoDesde, montoHasta, bancoEmisor, nroIdEmisor, fechaDesde, fechaHasta);
				LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARUNAAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			
			}else {
				setModelErrorMensaje(model, MENSAJENORESULTADO, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERSELECCIONARUNAAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			setModelErrorMensaje(model, e.getMessage(), listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	@GetMapping("/deseleccionarUnaAprobarOperaciones")
	public String deseleccionarUnaAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, @RequestParam("referencia") String referencia, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARUNAAPROBARALTOVALORI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		
		
		try {
			listaBancos  = bancoService.listaBancos(bancoRequest);
			setModelCceTransaccionDtoMostrar(model, request);
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			Filtros filtros = new Filtros();
			filtros.setStatus("I");
				
			if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
				filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
				filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));
				        
			}else {
				listaError.add(MENSAJEFECHASINVALIDAS);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
			
			if(montoDesde != null && montoHasta != null) {
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			
			
			setFiltrosBancoDestino(bancoEmisor, filtros);
			setFiltrosNumeroIdentificacion(nroIdEmisor, filtros);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			
			AprobacionesConsultasResponse aprobacionesConsultasResponse = aprobacionesConsultasResponse(aprobacionesConsultasRequest, DESELECCIONARUNAAPROBAROPERACIONES, request);
			listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			
			if(!listaBCVLBTPorAprobar.isEmpty()) {
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosUnaFalse(listaBCVLBTPorAprobar, referencia, httpSession);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				modelAprobacionesConsultasResponsePagina(model, datosPaginacion, buscarValorListaBCVLTSeleccionadosBotonSelecionar(listaBCVLBTPorAprobar), 
				buscarValorListaBCVLTSeleccionadosBoton(listaBCVLBTPorAprobar),montoAprobarOperacionesSeleccionadas);
				modelAprobacionesConsultasResponseConsulta(model, montoDesde, montoHasta, bancoEmisor, nroIdEmisor, fechaDesde, fechaHasta);
				LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARUNAAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				LOGGER.info(CCETRANSACCIONCONTROLLERDESELECCIONARUNAAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
			
							
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	
	
	@GetMapping("/export/all")
	public ResponseEntity<InputStreamResource> exportAllData(HttpSession httpSession) throws IOException {
		List<CceTransaccionDto> listaTransacciones =(List<CceTransaccionDto>)httpSession.getAttribute(LISTATRANSACCIONESEXCEL);
		ByteArrayInputStream stream = service.exportAllData(listaTransacciones);
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		HttpHeaders headers = new HttpHeaders();
		
		
		
		headers.add("Content-Disposition", "attachment; filename=transaccionesconsultaCCE_" + currentDateTime + ".xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
	}

	
	public List<CceTransaccionDto> getListaTransaccionesDto(List<CceTransaccion> listaTransacciones){
		 
		List<CceTransaccionDto> listaTransaccionesDto = new ArrayList<>();
		
		for (CceTransaccion cceTransaccion : listaTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaTransaccionesDto.add(cceTransaccionDto);
		}
		
		return listaTransaccionesDto;
	}
	
	
	@GetMapping("/export/pdf")
	public void exportToPdf(@RequestParam("endtoendId") String endtoendId, HttpServletResponse response) throws DocumentException {
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=detalle_"+dateString()+".pdf";
		
		LOGGER.info(endtoendId);
		CceTransaccionDto cceTransaccionDtoDetalle = service.findByEndtoendId(endtoendId);
		setValoresDetalleTransaccion(cceTransaccionDtoDetalle);
		response.setHeader(headerKey, headerValue);
		
		service.export(response, cceTransaccionDtoDetalle);
	}
	
	public void setValoresDetalleTransaccion(CceTransaccionDto cceTransaccionDtoDetalle) {
		if(!cceTransaccionDtoDetalle.isEnvio()) {
			LOGGER.info(cceTransaccionDtoDetalle.getCodTransaccion());
			String cuentaOrigen = cceTransaccionDtoDetalle.getCuentaOrigen();
			String cuentaDestino = cceTransaccionDtoDetalle.getCuentaDestino();
			cceTransaccionDtoDetalle.setCuentaOrigen(cuentaDestino);
			cceTransaccionDtoDetalle.setCuentaDestino(cuentaOrigen);
			String numeroIdentificacionCce = cceTransaccionDtoDetalle.getNumeroIdentificacion();
			String numeroIdentificacionDestinoCce = cceTransaccionDtoDetalle.getNumeroIdentificacionDestino();
			cceTransaccionDtoDetalle.setNumeroIdentificacion(numeroIdentificacionDestinoCce);
			cceTransaccionDtoDetalle.setNumeroIdentificacionDestino(numeroIdentificacionCce);
			String beneficiarioOrigen = cceTransaccionDtoDetalle.getBeneficiarioOrigen();
			String beneficiarioDestino = cceTransaccionDtoDetalle.getBeneficiarioDestino();
			cceTransaccionDtoDetalle.setBeneficiarioOrigen(beneficiarioDestino);
			cceTransaccionDtoDetalle.setBeneficiarioDestino(beneficiarioOrigen);
		}
		
		cceTransaccionDtoDetalle.setNombreTransaccion(nombreTransaccion(cceTransaccionDtoDetalle.getCodTransaccion())+"-"+cceTransaccionDtoDetalle.getCodTransaccion());
		cceTransaccionDtoDetalle.setNombreEstadoBcv(nombreEstadoBcv(cceTransaccionDtoDetalle.getEstadobcv()));
		cceTransaccionDtoDetalle.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDtoDetalle.getMonto())));
		cceTransaccionDtoDetalle.setMontoString(libreriaUtil.formatNumber(cceTransaccionDtoDetalle.getMonto()));
	}
	
	public String dateString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:hh:mm:ss");
		return dateFormat.format(new Date());
	}
	
	
	public void setAprobacionesConsultasRequestFiltrosPage(int numeroPagina, Filtros filtros, AprobacionesConsultasRequest aprobacionesConsultasRequest) {
		aprobacionesConsultasRequest.setNumeroPagina(numeroPagina);   
		aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
		aprobacionesConsultasRequest.setFiltros(filtros);
	 
	}
	
	public void setFiltrosMontosNotNull(BigDecimal montoDesde, BigDecimal montoHasta,Filtros filtros) {
		filtros.setMontoDesde(montoDesde);
		filtros.setMontoHasta(montoHasta);
	}
	
	public void setFiltrosMontosNull(CceMontoMaximoAproAuto cceMontoMaximoAproAuto, Filtros filtros) {
		filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
		filtros.setMontoHasta(montoTopeMaximoAproAuto);
	}
	
	public void setFiltrosFechas(String fechaDesde, String fechaHasta,Filtros filtros) {
		filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
		filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));
	}
	
	public void setFiltrosBancoDestino(String bancoDestino, Filtros filtros) {
		if(!bancoDestino.equals(""))
			filtros.setBancoBeneficiario(bancoDestino);
	}
	
	public void setFiltrosNumeroIdentificacion(String numeroIdentificacion, Filtros filtros) {
		if(!numeroIdentificacion.equals(""))
			filtros.setNroIdEmisor(numeroIdentificacion);
	}
	
	public void hasError(Model model, BindingResult result, List<Banco> listaBancos) {
		List<String> listaError = new ArrayList<>();
		for (ObjectError error : result.getAllErrors()) {
			LOGGER.info(error.getDefaultMessage());
			listaError.add("El valor del monto debe ser numerico");
			
		}
		model.addAttribute(LISTAERROR, listaError);
		model.addAttribute(LISTABANCOS, listaBancos);
	}
	
	public AprobacionesConsultasResponse aprobacionesConsultasResponse(AprobacionesConsultasRequest aprobacionesConsultasRequest, 
			String accion, HttpServletRequest request) throws CustomException{
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasResponse response = new AprobacionesConsultasResponse();
		AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest, 
				accion, request);
		
		if(aprobacionesConsultasResponse != null) {
			listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
			datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
		}
		response.setOperaciones(listaBCVLBTPorAprobar);
		response.setDatosPaginacion(datosPaginacion);
		return response;
	}
	
	
	public void modelAprobacionesConsultasResponsePagina(Model model, boolean seleccionados, boolean seleccionadosBoton){
			model.addAttribute(SELECCIONADOS, seleccionados);
			model.addAttribute(SELECCIONADOSBOTON, seleccionadosBoton);
	}
	
	public void modelAprobacionesConsultasResponsePagina(Model model, DatosPaginacion datosPaginacion, 
			boolean seleccionados, boolean seleccionadosBoton, BigDecimal montoAprobarOperacionesSeleccionadas){
			model.addAttribute(DATOSPAGINACION,datosPaginacion);
			model.addAttribute(SELECCIONADOS, seleccionados);
			model.addAttribute(SELECCIONADOSBOTON, seleccionadosBoton);
			model.addAttribute(MONTOAPROBAROPERACIONESSELECCIONADOS, libreriaUtil.formatNumber(montoAprobarOperacionesSeleccionadas));
		
	}
	
	public void modelAprobacionesConsultasResponseConsulta(Model model, BigDecimal montoDesde, BigDecimal montoHasta, 
			String bancoDestino, String numeroIdentificacion, String fechaDesde, String fechaHasta){
			model.addAttribute(MONTODESDE, montoDesde);
			model.addAttribute(MONTOHASTA, montoHasta);
			model.addAttribute(BANCOEMISOR, bancoDestino);
			model.addAttribute(NROIDEMISOR, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			
		
	}
	
	
	@GetMapping("/formAprobarLoteAutomatico")
	public String formAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionLoteAutomaticoBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}		
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request); 
		
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
	
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
		filtros.setStatus("I");
		filtros.setMontoDesde(new BigDecimal(0));
		filtros.setMontoHasta(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest, 
					FORMAPROBARAUTOMATICO, request);
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					cceTransaccionDto.setNumeroAprobacionesLotes(0);
					cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
					model.addAttribute(LISTAERROR, MENSAJENORESULTADOLOTE);
					model.addAttribute(SELECCIONADOSBOTON, false);
				}else {
					httpSession.setAttribute(LISTABCVLBTPORAPROBAR, listaBCVLBTPorAprobar);
					cceTransaccionDto.setNumeroAprobacionesLotes(listaBCVLBTPorAprobar.size());
					cceTransaccionDto.setMontoAprobacionesLotes(libreriaUtil.montoAprobacionesLotes(listaBCVLBTPorAprobar));
					model.addAttribute(SELECCIONADOSBOTON, true);
				}
				
			}else {
				cceTransaccionDto.setNumeroAprobacionesLotes(0);
				cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
				model.addAttribute(LISTAERROR, MENSAJENORESULTADOLOTE);
				model.addAttribute(SELECCIONADOSBOTON, false);
			}
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			cceTransaccionDto.setNumeroAprobacionesLotes(0);
			cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOF);
		return URLFORMAPROBARALTOVALORLOTEAUTOMATICO;
		
	}
	
	@GetMapping("/procesarAprobarAltoValorLoteAutomatico")
	public String procesarAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionLoteAutomaticoBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		List<String> listaAprobado = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBAR);
	
		httpSession.setMaxInactiveInterval(3600);
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobar) {
			LOGGER.info(bcvlbt);
			String transaccion = "Transaccion ref:"+bcvlbt.getReferencia();  
			
			
			try {
				Resultado resultadoAprobarResponse = procesarAprobarAltoValor(bcvlbt, PROCESARAPROBARLOTEAUTOMATICO, request);
				listaAprobado.add(aprobarActualizar(resultadoAprobarResponse, bcvlbt, PROCESARAPROBARLOTEAUTOMATICO, request));
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				String valor = transaccion +" -> "+e.getMessage();
				listaError.add(valor);
			}
			
		}
		
		
		model.addAttribute(LISTAERROR, listaError);
		model.addAttribute("listaAprobado", listaAprobado);
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOF);
		httpSession.setMaxInactiveInterval(180);
		return URLRESULTADOAPROBARAUTOMATO;
	}	
	
	@GetMapping("/procesarAprobarAltoValorLoteSeleccion")
	public String procesarAprobarAltoValorLoteSeleccion(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTESELECCIONI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorAprobacionOperacionAltoValorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		List<String> listaAprobado = new ArrayList<>();
		httpSession.setMaxInactiveInterval(3600);
		List<BCVLBT> listaBCVLBTPorAprobar = getListaBCVLTSeleccionadosTrue((List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBARSELECCION));
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobar) {
			LOGGER.info(bcvlbt);
			String transaccion = TRANSACCIONREFERENCIA+bcvlbt.getReferencia();  
			
			try {
				Resultado resultadoAprobarResponse = procesarAprobarAltoValor(bcvlbt, PROCESARAPROBARLOTESELECCION, request);
				listaAprobado.add(aprobarActualizar(resultadoAprobarResponse, bcvlbt, PROCESARAPROBARLOTESELECCION, request));
			} catch (CustomException e) {
				LOGGER.error(e.getMessage());
				String valor = transaccion +" -> "+e.getMessage();
				try {
					listaError.add(aprobarActualizarError(valor, bcvlbt, PROCESARAPROBARLOTESELECCION, request));
				} catch (CustomException e1) {
					LOGGER.error(e.getMessage());
				}
			}
			
		}
		
		
		//
		String mensajeError = "El total de Transacciones Enviadas No satisfactorias "+listaError.size()+ " / "+listaBCVLBTPorAprobar.size()+ ".";
		model.addAttribute(MENSAJEERROR, mensajeError);
		model.addAttribute(LISTAERROR, listaError);
		String mensaje = "El total de Transacciones Envidas Satisfactorias son "+listaAprobado.size()+ " / "+listaBCVLBTPorAprobar.size()+ ".";
		model.addAttribute("mensaje", mensaje);
		model.addAttribute("listaAprobado", listaAprobado);
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTESELECCIONF);
		httpSession.setMaxInactiveInterval(180);
		return URLRESULTADOAPROBARSELECCION;
	}
	
	public AprobacionRequest getAprobacionRequest(HttpServletRequest request) {
		AprobacionRequest aprobacionRequest = new AprobacionRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobacionRequest.setIdUsuario(userName);
		aprobacionRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobacionRequest.setIdCanal(canal);
		aprobacionRequest.setIp(request.getRemoteAddr());
		aprobacionRequest.setOrigen("03");
		return aprobacionRequest;
	}
	
	
	public DatosAprobacion getDatosAprobacion(BCVLBT bcvlbt, Resultado resultadoAprobarResponse) {
		DatosAprobacion datosAprobacion = new DatosAprobacion();
		datosAprobacion.setReferencia(bcvlbt.getReferencia());
		datosAprobacion.setNroIdEmisor(bcvlbt.getNroIdEmisor());
		datosAprobacion.setStatus(getStatus(resultadoAprobarResponse.getCodigo()));
		datosAprobacion.setUsuarioAprobador(SecurityContextHolder.getContext().getAuthentication().getName());
		return datosAprobacion;
	}
	
	public String getStatus(String codigo) {
		String status;
		if(codigo.equals("ACCP")) {
			status = "PA";
		}else {
			if(codigo.equals("0000") || codigo.equals("CC05")) {
				status = "PR";
			}else {
				status = "PP";
			}
		}
		return status;
	}
	
	public String aprobarActualizar(Resultado resultadoAprobarResponse, BCVLBT bcvlbt, String accion, HttpServletRequest request) throws CustomException{
		
		String transaccion = TRANSACCIONREFERENCIA+bcvlbt.getReferencia();
		
		String descripcionAprobar = resultadoAprobarResponse.getCodigo() + " - " + resultadoAprobarResponse.getDescripcion();
		String descripcionActualizar;
		
		AprobacionRequest aprobacionRequest = getAprobacionRequest(request);
		DatosAprobacion datosAprobacion = getDatosAprobacion(bcvlbt, resultadoAprobarResponse);
		aprobacionRequest.setDatosAprobacion(datosAprobacion);
		
		Resultado resultadoActualizarResponse = bcvlbtService.aporbarActualizar(aprobacionRequest, accion, request);
		descripcionActualizar = resultadoActualizarResponse.getCodigo() + " - " + resultadoActualizarResponse.getDescripcion();
		
		return transaccion+ FLECHA + descripcionAprobar + FLECHA +descripcionActualizar;
	}
	
	
	public String aprobarActualizarError(String error, BCVLBT bcvlbt, String accion, HttpServletRequest request) throws CustomException{
		
		String transaccion = TRANSACCIONREFERENCIA+bcvlbt.getReferencia();
		
		String descripcionAprobar = error;
		String descripcionActualizar;
		
		AprobacionRequest aprobacionRequest = getAprobacionRequest(request);
		DatosAprobacion datosAprobacion = getDatosAprobacionError(bcvlbt);
		aprobacionRequest.setDatosAprobacion(datosAprobacion);
		
		Resultado resultadoActualizarResponse = bcvlbtService.aporbarActualizar(aprobacionRequest, accion, request);
		descripcionActualizar = resultadoActualizarResponse.getCodigo() + " - " + resultadoActualizarResponse.getDescripcion();
		
		return transaccion+ FLECHA + descripcionAprobar + FLECHA +descripcionActualizar;
	}
	
	public DatosAprobacion getDatosAprobacionError(BCVLBT bcvlbt) {
		DatosAprobacion datosAprobacion = new DatosAprobacion();
		datosAprobacion.setReferencia(bcvlbt.getReferencia());
		datosAprobacion.setNroIdEmisor(bcvlbt.getNroIdEmisor());
		datosAprobacion.setStatus("PP");
		
		return datosAprobacion;
	}
	
	public Resultado procesarAprobarAltoValor(BCVLBT bcvlbt, String accion, HttpServletRequest request) throws CustomException{
		
		
		FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest = new FiToFiCustomerCreditTransferRequest(); 
		
		Sglbtr sglbtr = new Sglbtr();
		FIToFICstmrCdtTrfInitnDetalle fIToFICstmrCdtTrfInitnDetalle = new FIToFICstmrCdtTrfInitnDetalle(); 
		GrpHdrObject grpHdr = new GrpHdrObject();
		List<PmtInfObject> pmtInf = new ArrayList<>();
		Moneda moneda = new Moneda();
		
		//creando el ParamIdentificacion de la esctructura
		ParamIdentificacion paramIdentificacion = getParamIdentificacion();
		paramIdentificacion.setCodTransaccion(bcvlbt.getCodTransaccion());
		paramIdentificacion.setBancoReceptor(getBancoReceptor(bcvlbt.getBancoReceptor()).getNbBanco());
		
		//creando el grpHdr de la esctructura
		grpHdr.setMsgId(getMsgId());
		grpHdr.setCreDtTm(libreriaUtil.fechayhora());
		grpHdr.setNbOfTxs(1);
		
		moneda.setCcy(bcvlbt.getCodMoneda());
		moneda.setAmt(bcvlbt.getMonto().doubleValue());
		grpHdr.setCtrlSum(moneda);
		grpHdr.setIntrBkSttlmDt(libreriaUtil.obtenerFechaYYYYMMDD());
		grpHdr.setLclInstrm(libreriaUtil.getProducto(bcvlbt.getProducto()));
		grpHdr.setChannel(libreriaUtil.getChannel(bcvlbt.getIdCanal()));
		
		fIToFICstmrCdtTrfInitnDetalle.setGrpHdr(grpHdr);
		
		PmtInfObject pmtInfObject = new PmtInfObject();
		pmtInfObject.setRegId(1);
		pmtInfObject.setEndToEndId(libreriaUtil.getEndToEndId(bcvlbt.getBancoEmisor(), bcvlbt.getReferencia().toString()));
		pmtInfObject.setClrSysRef(BALNK);
		pmtInfObject.setTxId(BALNK);
		pmtInfObject.setAmount(moneda);
		pmtInfObject.setPurp(bcvlbt.getSubProducto());
		pmtInfObject.setDbtrAgt(bcvlbt.getBancoEmisor());
		pmtInfObject.setCdtrAgt(bcvlbt.getBancoReceptor());
		
		Identificacion dbtr = new Identificacion();
		dbtr.setNm(bcvlbt.getNombreEmisor());
		dbtr.setId(bcvlbt.getNroIdEmisor());
		dbtr.setSchmeNm(libreriaUtil.getSchmeNm(bcvlbt.getNroIdEmisor()));
		pmtInfObject.setDbtr(dbtr);
		
		
		Cuenta dbtrAcct = new Cuenta();
		dbtrAcct.setTp("CNTA");
		dbtrAcct.setId(bcvlbt.getNroCuentaOrigen());
		pmtInfObject.setDbtrAcct(dbtrAcct);
		
		Cuenta dbtrAgtAcct = new Cuenta();
		dbtrAgtAcct.setTp("CNTA");
		dbtrAgtAcct.setId(cceCuentasUnicasBcvService.consultaCuentasUnicasBcvByCodigoBic(bcvlbt.getBancoEmisor()).getCuenta());
		pmtInfObject.setDbtrAgtAcct(dbtrAgtAcct);
		
		Identificacion cdtr = new Identificacion();
		cdtr.setNm(bcvlbt.getNombreReceptor());
		cdtr.setId(bcvlbt.getNroIdReceptor());
		cdtr.setSchmeNm(libreriaUtil.getSchmeNm(bcvlbt.getNroIdReceptor()));
		pmtInfObject.setCdtr(cdtr);
		
		Cuenta cdtrAcct = new Cuenta();
		cdtrAcct.setTp("CNTA");
		cdtrAcct.setId(bcvlbt.getNroCuentaDestino());
		pmtInfObject.setCdtrAcct(cdtrAcct);
		
		Cuenta cdtrAgtAcct = new Cuenta();
		cdtrAgtAcct.setTp("CNTA");
		cdtrAgtAcct.setId(cceCuentasUnicasBcvService.consultaCuentasUnicasBcvByCodigoBic(bcvlbt.getBancoReceptor()).getCuenta());
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
	
	public String getMsgId() {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < 28; i++) {
			bld.append("0");
		}
		return bld.toString();
	}
	
    public CceTransaccionDto getTransaccionesMostrar(HttpServletRequest request) throws CustomException{
		
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(request);
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
		filtros.setStatus("I");
		filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto().add(new BigDecimal("0.01")))));
		filtros.setMontoHasta(montoTopeMaximoAproAuto);
		
		
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					cceTransaccionDto.setNumeroAprobacionesLotes(0);
					cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
					
				}else {
					
					cceTransaccionDto.setNumeroAprobacionesLotes(listaBCVLBTPorAprobar.size());
					cceTransaccionDto.setMontoAprobacionesLotes(libreriaUtil.montoAprobacionesLotes(listaBCVLBTPorAprobar));
				}
				
			}else {
				cceTransaccionDto.setNumeroAprobacionesLotes(0);
				cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
				
			}
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			cceTransaccionDto.setNumeroAprobacionesLotes(0);
			cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
			
		}
		return cceTransaccionDto;
		
		
	}
    
    public Banco getBancoReceptor(String codBanco) throws CustomException{
		
		BancoRequest bancoRequest = getBancoRequest();
		bancoRequest.setCodBanco(codBanco);
		return bancoService.buscarBanco(bancoRequest);
		
	}
	
    public String getFechaHoraDesdeFormato(String fechaHoraDesde) {
		String[] arrOfFecha = fechaHoraDesde.split("T");
		String fechaDesde = arrOfFecha[0];
		String horaDesde = arrOfFecha[1];
		
		fechaDesde = getFechaDiaMesAno(fechaDesde);
		horaDesde = getHora(horaDesde);
		
		fechaDesde = fechaDesde+" "+horaDesde+":00.000000";
		
		return fechaDesde;
	}
	
	public String getFechaHoraHastaFormato(String fechaHoraHasta) {
		String[] arrOfFecha = fechaHoraHasta.split("T");
		String fechaHasta = arrOfFecha[0];
		String horaHasta = arrOfFecha[1];
		
		fechaHasta = getFechaDiaMesAno(fechaHasta);
		horaHasta = getHora(horaHasta);
		
		fechaHasta = fechaHasta+" "+horaHasta+":59.000000";
		
		return fechaHasta;
	}
    
	public String getFechaDiaMesAno(String fecha) {
		String[] arrOfFecha = fecha.split("-");
		
		String ano = arrOfFecha[0];
		String mes = arrOfFecha[1];
		String dia = arrOfFecha[2];
		
		return dia+"-"+mes+"-"+ano;
	}
	
	
	public String getHora(String hora) {
		String[] arrOfHora = hora.split(":");
		String horaCambio = arrOfHora[0];
		String minutos = arrOfHora[1];
		return horaCambio+":"+minutos;
	}
	
    public List<BCVLBT> convertirListaBCVLT(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			bcvlbt.setMontoString(libreriaUtil.formatNumber(bcvlbt.getMonto()));
			bcvlbt.setFechaTransaccion(convetirFecha(bcvlbt.getFechaTransaccion()));	
		}
	
		return listaTransacciones;
	}
    
    public String convetirFecha(String fechaTransaccion) {
    	if(fechaTransaccion != null) {
			return fechaTransaccion.substring(0,19);
		}else {
			return fechaTransaccion;
		}
    }
    
    public List<BCVLBT> convertirListaBCVLTSeleccionadosTrue(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			bcvlbt.setSeleccionado(true);
		}
	
		return listaTransacciones;
	} 
    
    public List<BCVLBT> getListaBCVLTSeleccionadosTrue(List<BCVLBT> listaTransacciones){
    	List<BCVLBT> listaBCVLTSeleccionadosTrue = new ArrayList<>();
    	for (BCVLBT bcvlbt : listaTransacciones) {
			if(bcvlbt.isSeleccionado())
				listaBCVLTSeleccionadosTrue.add(bcvlbt);
		}
	
		return listaBCVLTSeleccionadosTrue;
	}
    
    
    public List<BCVLBT> convertirListaBCVLTSeleccionadosFalse(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			bcvlbt.setSeleccionado(false);
		}
	
		return listaTransacciones;
	}
    
    public List<BCVLBT> convertirListaBCVLTSeleccionadosUnaTrue(List<BCVLBT> listaTransacciones, String referencia, HttpSession httpSession){
		
		int referenciaInt = Integer.parseInt(referencia);
		for (BCVLBT bcvlbt : listaTransacciones) {
			if(bcvlbt.getReferencia() == referenciaInt) {
				bcvlbt.setSeleccionado(true);
			}else {
				bcvlbt.setSeleccionado(buscarvalorListaBCVLTSeleccionados(bcvlbt.getReferencia(), httpSession));
			}
		}
		
		
	
		return listaTransacciones;
	}
    
    public List<BCVLBT> convertirListaBCVLTSeleccionadosUnaFalse(List<BCVLBT> listaTransacciones, String referencia, HttpSession httpSession){
		int referenciaInt = Integer.parseInt(referencia);
		for (BCVLBT bcvlbt : listaTransacciones) {
			if(bcvlbt.getReferencia() == referenciaInt) {
				bcvlbt.setSeleccionado(false);
			}else {
				bcvlbt.setSeleccionado(buscarvalorListaBCVLTSeleccionados(bcvlbt.getReferencia(), httpSession));
			}
		}
		return listaTransacciones;
	}
	
	
	public boolean buscarvalorListaBCVLTSeleccionados(int referencia, HttpSession httpSession){
		LOGGER.info("buscarvalorListaBCVLTSeleccionadosInicio");
		List<BCVLBT> listaBCVLBTPorAprobarSesion =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBARSELECCION);
		boolean valor = false;
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobarSesion) {
			if(bcvlbt.getReferencia() == referencia) {
				valor = bcvlbt.isSeleccionado();
			}
			
		}
		
		LOGGER.info("buscarvalorListaBCVLTSeleccionadosFin");
		return valor;
	}
	
	
	public boolean buscarValorListaBCVLTSeleccionadosBoton(List<BCVLBT> listaTransacciones){
		LOGGER.info("buscarValorListaBCVLTSeleccionadosBotonInicio");
		
		boolean valor = false;
		for (BCVLBT bcvlbt : listaTransacciones) {
			if(bcvlbt.isSeleccionado())
				valor = true;	
		}
		
		LOGGER.info("buscarValorListaBCVLTSeleccionadosBotonFin");
		return valor;
	}
	
	public boolean buscarValorListaBCVLTSeleccionadosBotonSelecionar(List<BCVLBT> listaTransacciones){
		LOGGER.info("buscarValorListaBCVLTSeleccionadosBotonSelecionarInicio");
		
		
		
		boolean encontrado = true;
		Iterator<BCVLBT> it = listaTransacciones.iterator();
        while(it.hasNext() && encontrado) {
        	BCVLBT bcvlbt = it.next();
        	if(!bcvlbt.isSeleccionado()) {
        		encontrado = false;
        	}
				
        }
		
		
		
		LOGGER.info("buscarValorListaBCVLTSeleccionadosSelecionarBotonFin");
		return encontrado;
	}
	
	
	
	public ParamIdentificacion getParamIdentificacion() {
		ParamIdentificacion paramIdentificacion = new ParamIdentificacion();
		paramIdentificacion.setIdSesion(libreriaUtil.obtenerIdSesionCce());
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		paramIdentificacion.setIdUsuario(userName);
		
		return paramIdentificacion;
	}
	
	public BancoRequest getBancoRequest() {
		BancoRequest bancoRequest = new BancoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bancoRequest.setIdUsuario(userName);
		bancoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		return bancoRequest;
	}
	
	public AprobacionesConsultasRequest getAprobacionesConsultasRequest(HttpServletRequest request) {
		AprobacionesConsultasRequest aprobacionesConsultasRequest = new AprobacionesConsultasRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobacionesConsultasRequest.setIdUsuario(userName);
		aprobacionesConsultasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobacionesConsultasRequest.setIdCanal(canal);
		aprobacionesConsultasRequest.setIp(request.getRemoteAddr());
		aprobacionesConsultasRequest.setOrigen("03");
		return aprobacionesConsultasRequest;
	}
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		CceTransaccionDto cceTransaccionDtoSearch = new CceTransaccionDto();
		model.addAttribute("cceTransaccionDto", cceTransaccionDto);
		model.addAttribute("cceTransaccionDtoSearch", cceTransaccionDtoSearch);
		
		LOGGER.info(request.getRequestURI());
        String titulo = getTitulo(request.getRequestURI());
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "CCE - "+titulo;
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	public String getTitulo(String str) {
		String titulo= "No Asignado";
		String[] arrOfStr = str.split("/");
		 
        
		
        int ultimo = arrOfStr.length - 1;
        
        if(ultimo > 0) {
        	String metodo = arrOfStr[ultimo];
        	if(metodo.equals(FORMCONSULTAMOVIMIENTOS) || metodo.equals(PROCESARCONSULTAMOVIMIENTOS) || metodo.equals(CONSULTAMOVIMIENTOSPAGE) || 
        			metodo.equals(DETALLEMOVIMIENTO)) {
        		titulo = "Consulta de Movimientos";
        	}else {
        		if(metodo.equals(FORMAPROBARAUTOMATICO)) {
            		titulo = "Aprobación automática Alto Valor";
            	}else {
            		titulo = "Aprobación Alto Valor (Canales Digitales)";
            	}	
        	}
        	
        }
		
        return titulo;
		
	}
	
	
	
	public Page<CceTransaccion> convertirLista(Page<CceTransaccion> listaTransacciones){
		for (CceTransaccion cceTransaccion : listaTransacciones) {
			cceTransaccion.setMontoString(libreriaUtil.formatNumber(cceTransaccion.getMonto()));
		}
	
		return listaTransacciones;
	}
	
	public String nombreTransaccion(String codTransaccion) {
		String nombreTransaccion="";
		if(codTransaccion.equals("5724") || codTransaccion.equals("9734") || codTransaccion.equals("9742") || codTransaccion.equals("9743")) {
			nombreTransaccion = "Credito Inmediato Recibido";
		}else {
			if(codTransaccion.equals("5723") || codTransaccion.equals("9733") || codTransaccion.equals("9740") || codTransaccion.equals("9741") || codTransaccion.equals("5760") || codTransaccion.equals("5759")) {
				nombreTransaccion = "Credito Inmediato Enviado";
			}else {
				if(codTransaccion.equals("5728") || codTransaccion.equals("9738")) {
					nombreTransaccion = "Alto valor Recibido";
				}else {
					nombreTransaccion = "Alto Valor Enviado";
				}
			}
		}
		
		return nombreTransaccion;
	}
	
	public String nombreEstadoBcv(String estadobcv) {
		String nombreEstadoBcv="";
		
		if(estadobcv == null) {
			nombreEstadoBcv = "Incompleta";
		}else {
			if(estadobcv.equals("ACCP")) {
				nombreEstadoBcv = "Aprobada";
			}else {
				nombreEstadoBcv = "Rechazada";
			}
		}
		
	
		return nombreEstadoBcv;
	}
	
	
	
	
	
	
	
	
	
	public void guardarAuditoria(String accion, boolean resultado, String codRespuesta,  String respuesta, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta, request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaCceTransaccionDto(String accion, boolean resultado, String codRespuesta,  String respuesta, CceTransaccionDto cceTransaccionDto, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta+" CceTransaccion:[tipoTransaccion="+cceTransaccionDto.getTipoTransaccionInt()+", bancoDestino="+cceTransaccionDto.getBancoDestino()+""
							+ ", numeroIdentificacion="+cceTransaccionDto.getNumeroIdentificacion()+", fechaDesde="+cceTransaccionDto.getFechaDesde()+", fechaHasta="+cceTransaccionDto.getFechaHasta()+"]", request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public void guardarAuditoriaId(String accion, boolean resultado, String codRespuesta,  String respuesta, String endtoendId, HttpServletRequest request) {
		try {
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAI);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					CCE, accion, codRespuesta, resultado, respuesta+" CceTransaccion:[endtoendId="+endtoendId+"]", request.getRemoteAddr());
			LOGGER.info(CCETRANSACCIONCONTROLLERFUNCIONAUDITORIAF);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
