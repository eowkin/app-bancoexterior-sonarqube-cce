package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Compra;
import com.bancoexterior.app.convenio.model.DatosConsulta;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.Fechas;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.model.Venta;
import com.bancoexterior.app.convenio.service.IAcumuladosServiceApiRest;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.service.IMovimientosApiRest;
import com.bancoexterior.app.util.ConsultaExcelExporter;
import com.bancoexterior.app.util.LibreriaUtil;






@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
		
	private static final Logger LOGGER = LogManager.getLogger(SolicitudController.class);
	
	@Autowired
	private IMovimientosApiRest movimientosApiRest;
	
	@Autowired
	private IAcumuladosServiceApiRest acumuladosServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;
	
	@Value("${${app.ambiente}"+".movimientos.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	@Value("${${app.ambiente}"+".movimientos.origen}")
    private String valorOrigen;
	
	@Value("${${app.ambiente}"+".movimientos.operaciones.valorBD}")
    private int valorOperacionBD;
	
	@Value("${${app.ambiente}"+".movimientos.consultas.valorBD}")
    private int valorConsultaBD;
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String VENTAACUMULADOUSD = "ventaAcumuladoUSD";
	
	private static final String VENTAACUMULADOEUR = "ventaAcumuladoEUR";
	
	private static final String MONTOBSTOTALVENTA = "montoBsTotalVenta";
	
	private static final String COMPRAACUMULADOUSD = "compraAcumuladoUSD";
	
	private static final String COMPRAACUMULADOEUR = "compraAcumuladoEUR";
	
	private static final String MONTOBSTOTALCOMPRA = "montoBsTotalCompra";
	
	private static final String VENTAPORAPROBARUSD = "ventaPorAprobarUSD";
	
	private static final String VENTAPORAPROBAREUR = "ventaPorAprobarEUR";
	
	private static final String MONTOBSTOTALPORAPROBARVENTA = "montoBsTotalPorAprobarVenta";
	
	private static final String COMPRAPORAPROBARUSD = "compraPorAprobarUSD";
	
	private static final String COMPRAPORAPROBAREUR = "compraPorAprobarEUR";
	
	private static final String MONTOBSTOTALPORAPROBARCOMPRA = "montoBsTotalPorAprobarCompra";
	
	private static final String DATOSPAGINACIONVENTA = "datosPaginacionVenta";
	
	private static final String LISTAMOVIMIENTOSVENTA = "listaMovimientosVenta";
	
	private static final String DATOSPAGINACIONCOMPRA = "datosPaginacionCompra";
	
	private static final String LISTAMOVIMIENTOSCOMPRA = "listaMovimientosCompra";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String URLLISTAMOVIMIENTOSPORAPROBARVENTA = "convenio/solicitudes/listaSolicitudesMovimientosPorAprobarVenta";
	
	private static final String URLLISTAMOVIMIENTOSVENTA = "convenio/solicitudes/listaSolicitudesMovimientosVenta";
	
	private static final String URLLISTAMOVIMIENTOSVENTASEARCHESTATUS =  "convenio/solicitudes/listaSolicitudesMovimientosVentaSearchEstatus";
	
	private static final String URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS =  "convenio/solicitudes/listaSolicitudesMovimientosCompraSearchEstatus";
	
	private static final String URLLISTAMOVIMIENTOSCOMPRA = "convenio/solicitudes/listaSolicitudesMovimientosCompra";
	
	private static final String URLFORMSOLICITUD = "convenio/solicitudes/formSolicitud";
	
	private static final String URLFORMSOLICITUDVENTA = "convenio/solicitudes/formSolicitudVenta";
	
	private static final String URLFORMSOLICITUDGENERARREPORTE = "convenio/solicitudes/formSolicitudGenerarReporte";
	
	private static final String PAGINAACTUAL = "paginaActual";
	
	private static final String ESTATUS = "estatus";
	
	private static final String FECHADESDE = "fechaDesde";
	
	private static final String FECHAHASTA = "fechaHasta";
	
	private static final String STRDATEFORMET = "yyyy-MM-dd";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJECOMPRA = "mensajeCompra";
	
	private static final String MENSAJEVENTA = "mensajeVenta";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEERRORVENTA = "mensajeErrorVenta";
	
	private static final String MENSAJEERRORCOMPRA = "mensajeErrorCompra";
	
	private static final String MENSAJENORESULTADO = "La consulta no arrojo resultado.";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA = "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/";
	
	private static final String REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS = "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/";
	
	private static final String REDIRECTINICIO = "redirect:/";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARVENTASI = "[==== INICIO ListaMovimientosAprobarVentas Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARVENTASF = "[==== FIN ListaMovimientosAprobarVentas Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARCOMPRASI = "[==== INICIO ListaMovimientosAprobarCompras Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARCOMPRASF = "[==== FIN ListaMovimientosAprobarCompras Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSVENTASI = "[==== INICIO ListaMovimientosVentas Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSVENTASF = "[==== FIN ListaMovimientosVentas Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSCOMPRASI = "[==== INICIO ListaMovimientosCompras Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTAMOVIMIENTOSCOMPRASF = "[==== FIN ListaMovimientosCompras Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERPROCESARCOMPRAI = "[==== INICIO ProcesarCompra Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERPROCESARCOMPRAF = "[==== FIN ProcesarCompra Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERGUARDARCOMPRAI = "[==== INICIO GuardarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERGUARDARCOMPRAF = "[==== FIN GuardarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERPROCESARVENTAI = "[==== INICIO ProcesarVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERPROCESARVENTAF = "[==== FIN ProcesarVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERGUARDARVENTAI = "[==== INICIO GuardarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERGUARDARVENTAF = "[==== FIN GuardarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERRECHAZARCOMPRAI = "[==== INICIO RechazarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERRECHAZARCOMPRAF = "[==== FIN RechazarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERAPROBARCOMPRAI = "[==== INICIO AprobarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERAPROBARCOMPRAF = "[==== FIN AprobarCompra Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERRECHAZARVENTAI = "[==== INICIO RechazarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERRECHAZARVENTAF = "[==== FIN RechazarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERAPROBARVENTAI = "[==== INICIO AprobarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERAPROBARVENTAF = "[==== FIN AprobarVenta Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLEREXPORTEXCELI = "[==== INICIO ExportExcel Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLEREXPORTEXCELF = "[==== FIN ExportExcel Movimientos - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERSEARCHESTATUSVENTAI = "[==== INICIO SearchEstatusVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERSEARCHESTATUSVENTAF = "[==== FIN SearchEstatusVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSVENTAI = "[==== INICIO ListaConsultaSearchEstatusVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSVENTAF = "[==== FIN ListaConsultaSearchEstatusVenta Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERSEARCHESTATUSCOMPRAI = "[==== INICIO SearchEstatusCompra Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERSEARCHESTATUSCOMPRAF = "[==== FIN SearchEstatusCompra Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSCOMPRAI = "[==== INICIO ListaConsultaSearchEstatusCompra Movimientos Consultas - Controller ====]";
	
	private static final String SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSCOMPRAF = "[==== FIN ListaConsultaSearchEstatusCompra Movimientos Consultas - Controller ====]";
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarVentas/{page}")
	public String consultaMovimientoPorAprobarVenta(@PathVariable("page") int page,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARVENTASI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		try {
			//---------------Llenado Acumulados-------------------//
			acumulados(model);
			//---------------Llenado listaMovimientos por aprobar Venta-------------------//
			
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			
			modelResponseVentaPorAprobar(model, movimientosRequest);
			
			//---------------Hasta aqui Llenado listaMovimientos por aprobar Venta-------------------//
			
			//---------------Llenado listaMovimientos por aprobar Compra-------------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			filtrosCompra.setEstatus(0);
			movimientosRequest.setFiltros(filtrosCompra);
			
			modelResponseCompraPorAprobar(model, movimientosRequest);
			
			LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARVENTASF);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
			//---------------Llenado listaMovimientos por aprobar Compra-------------------//
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORVENTA, e.getMessage());
			consultaMovimientoPorAprobarVentaError(model);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
		}
	}
	
	public void modelResponseVentaPorAprobar(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosVenta;
		DatosPaginacion datosPaginacionVenta;
		MovimientosResponse responseVenta = responsePorAprobarVenta(movimientosRequest);
		listaMovimientosVenta = responseVenta.getMovimientos();
		datosPaginacionVenta = responseVenta.getDatosPaginacion();
		if(!listaMovimientosVenta.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosVenta) {
				movimiento.setTasaClienteString(libreriaUtil.formatNumber(movimiento.getTasaCliente()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
	}
	
	public void modelResponseCompraPorAprobar(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosCompra;
		DatosPaginacion datosPaginacionCompra;
		MovimientosResponse responseCompra = responsePorAprobarCompra(movimientosRequest);
		listaMovimientosCompra = responseCompra.getMovimientos();
		datosPaginacionCompra = responseCompra.getDatosPaginacion();
		if(!listaMovimientosCompra.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosCompra) {
				movimiento.setTasaClienteString(libreriaUtil.formatNumber(movimiento.getTasaCliente()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
	}
	
	public MovimientosResponse responsePorAprobarCompra(MovimientosRequest movimientosRequest)throws CustomException{
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		MovimientosResponse response = new MovimientosResponse();
		MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
		if(responseCompra != null) {
			listaMovimientosCompra = responseCompra.getMovimientos();
			datosPaginacionCompra =  responseCompra.getDatosPaginacion();
		}	
		response.setDatosPaginacion(datosPaginacionCompra);
		response.setMovimientos(listaMovimientosCompra);
		return response;
	}
	
	public MovimientosResponse responsePorAprobarVenta(MovimientosRequest movimientosRequest)throws CustomException{
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		MovimientosResponse response = new MovimientosResponse();
		MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobarVenta(movimientosRequest);
		if(responseVenta != null) {
			listaMovimientosVenta = responseVenta.getMovimientos();
			datosPaginacion = responseVenta.getDatosPaginacion();
		}
		response.setMovimientos(listaMovimientosVenta);
		response.setDatosPaginacion(datosPaginacion);
		return response;
	}
	
	public Model acumulados(Model model) throws CustomException{
		Venta ventaAcumuladoUSD;
		Venta ventaAcumuladoEUR;
		BigDecimal montoBsTotalVenta;
		Compra compraAcumuladoUSD;
		Compra compraAcumuladoEUR;
		BigDecimal montoBsTotalCompra;
		Venta ventaPorAprobarUSD;
		Venta ventaPorAprobarEUR;
		BigDecimal montoBsTotalPorAprobarVenta;
		Compra compraPorAprobarUSD;
		Compra compraPorAprobarEUR;
		BigDecimal montoBsTotalPorAprobarCompra;
		
		ventaAcumuladoUSD = acumuladosVenta("USD");
		model.addAttribute(VENTAACUMULADOUSD, evaluarBigDecimalVenta(ventaAcumuladoUSD));
		
		ventaAcumuladoEUR = acumuladosVenta("EUR");
		model.addAttribute(VENTAACUMULADOEUR, evaluarBigDecimalVenta(ventaAcumuladoEUR));
		
		montoBsTotalVenta =  ventaAcumuladoEUR.getMontoBs().add(ventaAcumuladoUSD.getMontoBs());
		model.addAttribute(MONTOBSTOTALVENTA, libreriaUtil.formatNumber(evaluarBigDecimal(montoBsTotalVenta)));
		
		compraAcumuladoUSD = acumuladosCompra("USD");
		model.addAttribute(COMPRAACUMULADOUSD, evaluarBigDecimalCompra(compraAcumuladoUSD)); 
		
		compraAcumuladoEUR = acumuladosCompra("EUR");
		model.addAttribute(COMPRAACUMULADOEUR, evaluarBigDecimalCompra(compraAcumuladoEUR));
		
		montoBsTotalCompra =  compraAcumuladoEUR.getMontoBs().add(compraAcumuladoUSD.getMontoBs());
		model.addAttribute(MONTOBSTOTALCOMPRA, libreriaUtil.formatNumber(evaluarBigDecimal(montoBsTotalCompra)));
		
		ventaPorAprobarUSD = acumuladosPorAprobarVenta("USD");
		model.addAttribute(VENTAPORAPROBARUSD,  evaluarBigDecimalVenta(ventaPorAprobarUSD));
		
		ventaPorAprobarEUR = acumuladosPorAprobarVenta("EUR");
		model.addAttribute(VENTAPORAPROBAREUR, evaluarBigDecimalVenta(ventaPorAprobarEUR)); 
		
		montoBsTotalPorAprobarVenta =  ventaPorAprobarEUR.getMontoBs().add(ventaPorAprobarUSD.getMontoBs());
		model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, libreriaUtil.formatNumber(evaluarBigDecimal(montoBsTotalPorAprobarVenta))); 
		
		compraPorAprobarUSD = acumuladosPorAprobarCompra("USD");
		model.addAttribute(COMPRAPORAPROBARUSD, evaluarBigDecimalCompra(compraPorAprobarUSD)); 
		
		compraPorAprobarEUR = acumuladosPorAprobarCompra("EUR");
		model.addAttribute(COMPRAPORAPROBAREUR, evaluarBigDecimalCompra(compraPorAprobarEUR));
		
		montoBsTotalPorAprobarCompra =  compraPorAprobarEUR.getMontoBs().add(compraPorAprobarUSD.getMontoBs());
		model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, libreriaUtil.formatNumber(evaluarBigDecimal(montoBsTotalPorAprobarCompra))); 
		
		return model;
	}
	
	public Model consultaMovimientoPorAprobarVentaError(Model model){
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		
		Venta ventaAcumuladoUSD = new Venta();
		Venta ventaAcumuladoEUR = new Venta();
		BigDecimal montoBsTotalVenta = new BigDecimal("0.00");
		Compra compraAcumuladoUSD = new Compra();
		Compra compraAcumuladoEUR = new Compra();
		BigDecimal montoBsTotalCompra = new BigDecimal("0.00");
		Venta ventaPorAprobarUSD = new Venta();
		Venta ventaPorAprobarEUR = new Venta();
		BigDecimal montoBsTotalPorAprobarVenta = new BigDecimal("0.00");
		Compra compraPorAprobarUSD = new Compra();
		Compra compraPorAprobarEUR = new Compra();
		BigDecimal montoBsTotalPorAprobarCompra = new BigDecimal("0.00");
		
		
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
		ventaAcumuladoUSD.setMonto(new BigDecimal("0.00"));
		model.addAttribute(VENTAACUMULADOUSD, ventaAcumuladoUSD);
		ventaAcumuladoEUR.setMonto(new BigDecimal("0.00"));
		model.addAttribute(VENTAACUMULADOEUR, ventaAcumuladoEUR);
		model.addAttribute(MONTOBSTOTALVENTA, montoBsTotalVenta);
		compraAcumuladoUSD.setMonto(new BigDecimal("0.00"));
		model.addAttribute(COMPRAACUMULADOUSD, compraAcumuladoUSD);
		compraAcumuladoEUR.setMonto(new BigDecimal("0.00"));
		model.addAttribute(COMPRAACUMULADOEUR, compraAcumuladoEUR);
		model.addAttribute(MONTOBSTOTALCOMPRA, montoBsTotalCompra);
		ventaPorAprobarUSD.setMonto(new BigDecimal("0.00"));
		model.addAttribute(VENTAPORAPROBARUSD, ventaPorAprobarUSD);
		ventaPorAprobarEUR.setMonto(new BigDecimal("0.00"));
		model.addAttribute(VENTAPORAPROBAREUR, ventaPorAprobarEUR);
		model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, montoBsTotalPorAprobarVenta);
		compraPorAprobarUSD.setMonto(new BigDecimal("0.00"));
		model.addAttribute(COMPRAPORAPROBARUSD, compraPorAprobarUSD);
		compraPorAprobarEUR.setMonto(new BigDecimal("0.00"));
		model.addAttribute(COMPRAPORAPROBAREUR, compraPorAprobarEUR);
		model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, montoBsTotalPorAprobarCompra);
		
		return model;
	}
	
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarCompra/{page}")
	public String consultaMovimientoPorAprobarCompra(@PathVariable("page") int page,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARCOMPRASI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		
		try {
			acumulados(model);
			//---------------Llenado listaMovimientos por aprobar Venta-------------------//
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			
			modelResponseVentaPorAprobar(model, movimientosRequest);
			
			//---------------Hasta aqui Llenado listaMovimientos por aprobar Venta-------------------//
			
			//---------------Llenado listaMovimientos por aprobar Compra-------------------//
			
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			filtrosCompra.setEstatus(0);
			movimientosRequest.setFiltros(filtrosCompra);
			
			modelResponseCompraPorAprobar(model, movimientosRequest);
			
			LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSAPROBARCOMPRASF);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORVENTA, e.getMessage());
			consultaMovimientoPorAprobarVentaError(model);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
		}
	}
	
	
	
	@GetMapping("/listaSolicitudesMovimientosVentas/{page}")
	public String consultaMovimientoVenta(@PathVariable("page") int page,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSVENTASI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
	
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			
			//-----LLenando Lista Movimientos Ventas-------------//
			modelResponseVentaConsulta(model, movimientosRequest);
			
			//-----Fin LLenando Lista Movimientos Ventas-------------//
			
			//-----LLenando Lista Movimientos Compras-------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			movimientosRequest.setFiltros(filtrosCompra);
			
			modelResponseCompraConsulta(model, movimientosRequest);
			
			LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSVENTASF);
			return URLLISTAMOVIMIENTOSVENTA;
			//-----Fin LLenando Lista Movimientos Compras-------------//
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			consultaMovimientoError(model);
			return URLLISTAMOVIMIENTOSVENTA;
		}
	}
	
	
	public void modelResponseVentaConsulta(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosVenta;
		DatosPaginacion datosPaginacionVenta;
		MovimientosResponse responseVenta = responseConsulta(movimientosRequest);
		listaMovimientosVenta = responseVenta.getMovimientos();
		datosPaginacionVenta = responseVenta.getDatosPaginacion();
		if(!listaMovimientosVenta.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosVenta) {
				movimiento.setMontoBsOperacionString(libreriaUtil.formatNumber(movimiento.getMontoBsOperacion()));
				movimiento.setMontoBsClienteString(libreriaUtil.formatNumber(movimiento.getMontoBsCliente()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}else {
			model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
		}
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
	}
	
	public void modelResponseCompraConsulta(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosCompra;
		DatosPaginacion datosPaginacionCompra;
		MovimientosResponse responseCompra = responseConsulta(movimientosRequest);
		listaMovimientosCompra = responseCompra.getMovimientos();
		datosPaginacionCompra = responseCompra.getDatosPaginacion();
		if(!listaMovimientosCompra.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosCompra) {
				movimiento.setMontoBsOperacionString(libreriaUtil.formatNumber(movimiento.getMontoBsOperacion()));
				movimiento.setMontoBsClienteString(libreriaUtil.formatNumber(movimiento.getMontoBsCliente()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}else {
			model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
		}
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
	}
	
	public MovimientosResponse responseConsulta(MovimientosRequest movimientosRequest)throws CustomException{
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		MovimientosResponse responseVentaRespuesta = new MovimientosResponse(); 
		MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
		if(responseVenta != null) {
			listaMovimientosVenta = responseVenta.getMovimientos();
			datosPaginacionVenta = responseVenta.getDatosPaginacion(); 
		}
		
		responseVentaRespuesta.setMovimientos(listaMovimientosVenta);
		responseVentaRespuesta.setDatosPaginacion(datosPaginacionVenta);
		return responseVentaRespuesta;
	}
	
	public Model consultaMovimientoError(Model model){
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
		
		return model;
	}
	
	
	@GetMapping("/listaSolicitudesMovimientosCompras/{page}")
	public String consultaMovimientoCompra(@PathVariable("page") int page,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSCOMPRASI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			
			//-----LLenando Lista Movimientos Ventas-------------//
			modelResponseVentaConsulta(model, movimientosRequest);
			//-----Fin LLenando Lista Movimientos Ventas-------------//
			
			//-----LLenando Lista Movimientos Compras-------------//
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			movimientosRequest.setFiltros(filtrosCompra);
			
			modelResponseCompraConsulta(model, movimientosRequest);
			
			LOGGER.info(SOLICITUDCONTROLLERLISTAMOVIMIENTOSCOMPRASF);
			return URLLISTAMOVIMIENTOSCOMPRA;
			//-----Fin LLenando Lista Movimientos Compras-------------//
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			consultaMovimientoError(model);
			return URLLISTAMOVIMIENTOSCOMPRA;
		}
	}
	
	@GetMapping("/procesarCompra/{codOperacion}/{page}")
	public String procesarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERPROCESARCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				model.addAttribute("movimiento", movimientoProcesar);
				LOGGER.info(SOLICITUDCONTROLLERPROCESARCOMPRAF);
				return URLFORMSOLICITUD;
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
			}	
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	@PostMapping("/guardarProcesarCompra")
	public String guardarProcesarCompra(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERGUARDARCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		model.addAttribute(PAGINAACTUAL, movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add("Los valores de los montos tasa debe ser numerico");
			}
			
			
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
		}
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError(LISTAERROR, " La fecha liquidacion es invalida"));
			
			listaError.add("La fecha liquidacion es invalida");
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
		setAprobarRechazarRequestGuardar(aprobarRechazarRequest, request.getRemoteAddr(), movimiento, 1);
		
		try {
			String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(SOLICITUDCONTROLLERGUARDARCOMPRAF);
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+getPageAprobarRechazarCompra(movimiento.getPaginaActual());
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
			
		}
	}
	
	
	public void setAprobarRechazarRequestGuardar(AprobarRechazarRequest aprobarRechazarRequest, 
			String remoteAddr, Movimiento movimiento, Integer estatus) {
		aprobarRechazarRequest.setIp(remoteAddr);
		aprobarRechazarRequest.setOrigen(valorOrigen);
		aprobarRechazarRequest.setCodSolicitud(movimiento.getCodOperacion());
		aprobarRechazarRequest.setTipoPacto(movimiento.getTipoPacto());
		aprobarRechazarRequest.setTasa(movimiento.getTasaOperacion());
		aprobarRechazarRequest.setFechaLiquidacion(movimiento.getFecha());
		aprobarRechazarRequest.setEstatus(estatus);
	}
	
	@GetMapping("/procesarVenta/{codOperacion}/{page}")
	public String procesarVenta(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERPROCESARVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setTipoTransaccion("V");
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				model.addAttribute("movimiento", movimientoProcesar);
				LOGGER.info(SOLICITUDCONTROLLERPROCESARVENTAF);
				return URLFORMSOLICITUDVENTA;
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
			}
			
			
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	
	@PostMapping("/guardarProcesarVenta")
	public String guardarProcesarVenta(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERGUARDARVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		model.addAttribute(PAGINAACTUAL, movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info(error.getDefaultMessage());
				listaError.add("Los valores de los montos tasa debe ser numerico");
			}
			
			
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError(LISTAERROR, " La fecha liquidacion es invalida"));
			listaError.add("La fecha liquidacion es invalida");
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
		setAprobarRechazarRequestGuardar(aprobarRechazarRequest, request.getRemoteAddr(), movimiento, 1);
		
		try {
			String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
			LOGGER.info(SOLICITUDCONTROLLERGUARDARVENTAF);
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+getPageAprobarRechazarVenta(movimiento.getPaginaActual());
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
	}
	
	public void setAprobarRechazarRequest(AprobarRechazarRequest aprobarRechazarRequest, 
			String remoteAddr, String codOperacion, Movimiento movimientoProcesar, Integer estatus) {
		aprobarRechazarRequest.setIp(remoteAddr);
		aprobarRechazarRequest.setOrigen(valorOrigen);
		aprobarRechazarRequest.setCodSolicitud(codOperacion);
		aprobarRechazarRequest.setTipoPacto(movimientoProcesar.getTipoPacto());
		aprobarRechazarRequest.setTasa(movimientoProcesar.getTasaCliente());
		aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
		aprobarRechazarRequest.setEstatus(estatus);
	}
	
	
	@GetMapping("/rechazarCompra/{codOperacion}/{page}")
	public String rechazarCompra(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERRECHAZARCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		
		
		try {
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				setAprobarRechazarRequest(aprobarRechazarRequest, request.getRemoteAddr(), codOperacion, movimientoProcesar, 2);
				
				String respuesta = movimientosApiRest.rechazarCompra(aprobarRechazarRequest);
				LOGGER.info(respuesta);
				redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
				LOGGER.info(SOLICITUDCONTROLLERRECHAZARCOMPRAF);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+getPageAprobarRechazarCompra(page);
				
				
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+getPageAprobarRechazarCompra(page);
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	public int getPageAprobarRechazarCompra(int page) throws CustomException{
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		movimientosRequest.setNumeroPagina(page);
		movimientosRequest.setTamanoPagina(numeroRegistroPage);
		Movimiento filtrosCompra = new Movimiento();
		filtrosCompra.setTipoTransaccion("C");
		filtrosCompra.setEstatus(0);
		movimientosRequest.setFiltros(filtrosCompra);
		int valorPage = 0;
		
		if(page==1) {
			valorPage = 1;	
		}else {
			MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
			if(responseCompra != null) {
				if(responseCompra.getMovimientos().isEmpty())
					valorPage = page - 1;
				else
					valorPage = page;
			}
		}
		LOGGER.info(valorPage);	
		return valorPage;		
		
	}
	
	public int getPageAprobarRechazarVenta(int page) throws CustomException{
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		movimientosRequest.setNumeroPagina(page);
		movimientosRequest.setTamanoPagina(numeroRegistroPage);
		Movimiento filtrosCompra = new Movimiento();
		filtrosCompra.setTipoTransaccion("V");
		filtrosCompra.setEstatus(0);
		movimientosRequest.setFiltros(filtrosCompra);
		int valorPage = 0;
		
		if(page==1) {
			valorPage = 1;	
		}else {
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobarVenta(movimientosRequest);
			if(responseVenta != null) {
				if(responseVenta.getMovimientos().isEmpty())
					valorPage = page - 1;
				else
					valorPage = page;
			}
		}
		LOGGER.info(valorPage);	
		return valorPage;		
		
	}
	
	
	@GetMapping("/aprobarCompra/{codOperacion}/{page}")
	public String aprobarCompra(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERAPROBARCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				setAprobarRechazarRequest(aprobarRechazarRequest, request.getRemoteAddr(), codOperacion, movimientoProcesar, 1);
				
				String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
				LOGGER.info(respuesta);
				redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
				LOGGER.info(SOLICITUDCONTROLLERAPROBARCOMPRAF);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+getPageAprobarRechazarCompra(page);
				
				
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+getPageAprobarRechazarCompra(page);
			}
			
			
			
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	@GetMapping("/rechazarVenta/{codOperacion}/{page}")
	public String rechazarVenta(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERRECHAZARVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				setAprobarRechazarRequest(aprobarRechazarRequest, request.getRemoteAddr(), codOperacion, movimientoProcesar, 2);
			
				String respuesta = movimientosApiRest.rechazarVenta(aprobarRechazarRequest);
				LOGGER.info(respuesta);
				redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
				LOGGER.info(SOLICITUDCONTROLLERRECHAZARVENTAF);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+getPageAprobarRechazarVenta(page);
				
				
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+getPageAprobarRechazarVenta(page);
			}
			
			
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	@GetMapping("/aprobarVenta/{codOperacion}/{page}")
	public String aprobarVenta(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERAPROBARVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorOperacionBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				setAprobarRechazarRequest(aprobarRechazarRequest, request.getRemoteAddr(), codOperacion, movimientoProcesar, 1);
				
				String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
				LOGGER.info(respuesta);
				redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
				LOGGER.info(SOLICITUDCONTROLLERAPROBARVENTAF);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+getPageAprobarRechazarVenta(page);
				
				
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+getPageAprobarRechazarVenta(page);
			}
			
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	
	
	
	
	@GetMapping("/precesarConsulta/export/excel")
    public String procesarExportToExcelConsulta(Movimiento movimiento, BindingResult result, Model model, 
    		RedirectAttributes redirectAttributes, HttpServletResponse response, HttpSession httpSession){
		LOGGER.info("exportToExcelConsulta");
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientos = new ArrayList<>();
		
		try {
			
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(2147483647);
			Movimiento filtros = new Movimiento();
			setFiltros(filtros, movimiento.getCodMoneda(), movimiento.getTipoTransaccion(), movimiento.getTipoCliente(), movimiento.getEstatus());
			
			
			if(!movimiento.getFechas().getFechaDesde().equals("") && !movimiento.getFechas().getFechaHasta().equals("")) {
				if(libreriaUtil.isFechaValidaDesdeHasta(movimiento.getFechas().getFechaDesde(), movimiento.getFechas().getFechaHasta())) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(movimiento.getFechas().getFechaDesde());
					fechas.setFechaHasta(movimiento.getFechas().getFechaHasta());
					filtros.setFechas(fechas);
				}else {
					MonedasRequest monedasRequest = getMonedasRequest();
					Moneda moneda = new Moneda();
					moneda.setFlagActivo(true);
					monedasRequest.setMoneda(moneda);
					model.addAttribute(LISTAMONEDAS, monedaServiceApiRest.listaMonedas(monedasRequest));
					result.addError(new ObjectError(LISTAERROR, MENSAJEFECHASINVALIDAS));
					listaError.add(MENSAJEFECHASINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					
				}
			}
			
			
			
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				listaMovimientos = responseVenta.getMovimientos();
				if(!listaMovimientos.isEmpty()) {
					exportToExcelConsulta(listaMovimientos, response);
				}else {
					MonedasRequest monedasRequest = getMonedasRequest();
					Moneda moneda = new Moneda();
					moneda.setFlagActivo(true);
					monedasRequest.setMoneda(moneda);
					model.addAttribute(LISTAERROR, monedaServiceApiRest.listaMonedas(monedasRequest));
					result.addError(new ObjectError(LISTAERROR, MENSAJENORESULTADO));
					model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
				}
			}else {
				result.addError(new ObjectError(LISTAERROR, MENSAJENORESULTADO));
				model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR,e.getMessage()));
			 listaError.add(e.getMessage());
			 model.addAttribute(LISTAERROR, listaError);			
		}
		
		return URLFORMSOLICITUDGENERARREPORTE;
    }
	
	
	public Movimiento setFiltros(Movimiento filtros, String codMoneda, String tipoTransaccion, String tipoCliente, Integer estatus) {
		if(!codMoneda.equals(""))
			filtros.setCodMoneda(codMoneda);
		if(!tipoTransaccion.equals(""))
		    filtros.setTipoTransaccion(tipoTransaccion);
		if(estatus != -1)
			filtros.setEstatus(estatus);
		if(!tipoCliente.equals(""))
			filtros.setTipoCliente(tipoCliente);
		
		return filtros;
	}
	
    public void exportToExcelConsulta(List<Movimiento> listaMovimientos, HttpServletResponse response) {
    	LOGGER.info(SOLICITUDCONTROLLEREXPORTEXCELI);
        	
		
		
				response.setContentType("application/octet-stream");
		        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		        String currentDateTime = dateFormatter.format(new Date());
		         
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=movimientosconsulta_" + currentDateTime + ".xlsx";
		        response.setHeader(headerKey, headerValue);
		        ConsultaExcelExporter excelExporter = new ConsultaExcelExporter(listaMovimientos); 
		        try {
					excelExporter.export(response);
					response.getOutputStream().flush();
					 response.getOutputStream().close();
					 LOGGER.info(SOLICITUDCONTROLLEREXPORTEXCELF);
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
				}
		
    }
	
    
	
	
	
	public Venta acumuladosVenta(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaVenta;
		Venta ventaRes = new Venta("","", new BigDecimal("0.00"), new BigDecimal("0.00"),"");
		
		
		
		AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
		if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
			listaVenta = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getVenta();
			for (Venta venta : listaVenta) {
				if(venta.getCodMoneda().equals(codMoneda)) {
					ventaRes = venta;
				}
			}
			
			return ventaRes;
		}else {
			return ventaRes;
		}
		
		
		
		
		
	}
	
	
	
	public Compra acumuladosCompra(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaCompra;
		Compra compraRes = new Compra("","", new BigDecimal("0.00"), new BigDecimal("0.00"), "");
		AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
		if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
			listaCompra = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getCompra();
			for (Compra compra : listaCompra) {
				if(compra.getCodMoneda().equals(codMoneda)) {
					compraRes = compra;
				}
			}
			
			return compraRes;
		}else {
			return compraRes;
		}
		
	}
	
	
	
	
	
	public Venta acumuladosPorAprobarVenta(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaPorAprobarVenta;
		Venta ventaPorAprobarRes = new Venta("","", new BigDecimal("0.00"), new BigDecimal("0.00"),"");
		AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
		if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
			listaPorAprobarVenta = acumuladoResponse.getAcumuladosPorAprobar().getVenta();
			for (Venta venta : listaPorAprobarVenta) {
				if(venta.getCodMoneda().equals(codMoneda)) {
					ventaPorAprobarRes = venta;
				}
			}
			
			return ventaPorAprobarRes;
		}else {
			return ventaPorAprobarRes;
		}
	}
	
	
	
	public Compra acumuladosPorAprobarCompra(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaPorAprobarCompra;
		Compra compraPorAprobarRes = new Compra("","", new BigDecimal("0.00"), new BigDecimal("0.00"), "");
		AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
		if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
			listaPorAprobarCompra = acumuladoResponse.getAcumuladosPorAprobar().getCompra();
			for (Compra compra : listaPorAprobarCompra) {
				if(compra.getCodMoneda().equals(codMoneda)) {
					compraPorAprobarRes = compra;
				}
			}
			
			return compraPorAprobarRes;
		}else {
			return compraPorAprobarRes;
		}
		
	}
	
	
	
	
	public MovimientosRequest getMovimientosRequest() {
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		movimientosRequest.setIdUsuario(userName);
		movimientosRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		movimientosRequest.setUsuario(userName);
		movimientosRequest.setCanal(canal);
		return movimientosRequest;
	}
	
	public AprobarRechazarRequest getAprobarRechazarRequest() {
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobarRechazarRequest.setIdUsuario(userName);
		aprobarRechazarRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobarRechazarRequest.setCodUsuario(userName);
		aprobarRechazarRequest.setCanal(canal);
		return aprobarRechazarRequest;
	}
	
	public AcumuladoRequest getAcumuladoRequest() {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		acumuladoRequest.setIdUsuario(userName);
		acumuladoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		acumuladoRequest.setCanal(canal);
		return acumuladoRequest;
		
	}
	
	public MonedasRequest getMonedasRequest() {
		MonedasRequest monedasRequest = new MonedasRequest();
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		monedasRequest.setIdUsuario(userName);
		monedasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		monedasRequest.setCodUsuario(userName);
		monedasRequest.setCanal(canal);
		return monedasRequest;
	}
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		Movimiento movimientoSearch = new Movimiento();
		model.addAttribute("movimientoSearch", movimientoSearch);
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "solicitudes";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	@GetMapping("/searchEstatus")
	public String searchEstatus(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		
		LOGGER.info(SOLICITUDCONTROLLERSEARCHESTATUSVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(movimientoSearch.getEstatus());
			
			LOGGER.info(movimientoSearch.getFechas().getFechaDesde());
			LOGGER.info(movimientoSearch.getFechas().getFechaHasta());

			if(libreriaUtil.isFechaValidaDesdeHasta(movimientoSearch.getFechas().getFechaDesde()  , movimientoSearch.getFechas().getFechaHasta())) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(movimientoSearch.getFechas().getFechaDesde());
					fechas.setFechaHasta(movimientoSearch.getFechas().getFechaHasta());
					filtrosVenta.setFechas(fechas);
			}else {
					model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
					model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
					model.addAttribute(MENSAJEERROR, MENSAJEFECHASINVALIDAS);
					//-----LLenando Lista Movimientos Compras-------------//
					movimientosRequest.setNumeroPagina(1);
					movimientosRequest.setTamanoPagina(numeroRegistroPage);
					Movimiento filtrosCompra = new Movimiento();
					filtrosCompra.setTipoTransaccion("C");
					movimientosRequest.setFiltros(filtrosCompra);
					
					modelResponseCompra(model, movimientosRequest);
					model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
					
			}
			
			movimientosRequest.setFiltros(filtrosVenta);
			//-----LLenando Lista Movimientos Ventas-------------//
			
			modelResponseVenta(model, movimientosRequest);
			model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
			model.addAttribute(FECHADESDE, movimientoSearch.getFechas().getFechaDesde());
			model.addAttribute(FECHAHASTA, movimientoSearch.getFechas().getFechaHasta());
			
			//-----LLenando Lista Movimientos Compras-------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			movimientosRequest.setFiltros(filtrosCompra);
			
			modelResponseCompra(model, movimientosRequest);
			LOGGER.info(SOLICITUDCONTROLLERSEARCHESTATUSVENTAF);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			searchError(model);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		}
		
	}
	
	public void searchError(Model model) {
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
	}
	
	
	public Model modelResponseCompra(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosCompra;
		DatosPaginacion datosPaginacionCompra;
		MovimientosResponse responseCompra = responseConsulta(movimientosRequest);
		listaMovimientosCompra = responseCompra.getMovimientos();
		datosPaginacionCompra = responseCompra.getDatosPaginacion();
		if(!listaMovimientosCompra.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosCompra) {
				movimiento.setMontoBsClienteString(libreriaUtil.formatNumber(movimiento.getMontoBsCliente()));
				movimiento.setMontoBsOperacionString(libreriaUtil.formatNumber(movimiento.getMontoBsOperacion()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}else {
			model.addAttribute(MENSAJECOMPRA, MENSAJENORESULTADO);
		}
		
		model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
		model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
		
		return model;
	}
	
	@GetMapping("/listaSolicitudesMovimientosVentasPage")
	public String consultaMovimientoVentaSearchEstatus(@RequestParam("page") int page,@RequestParam("estatus") int estatus, 
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSVENTAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(estatus);
			
			if(libreriaUtil.isFechaValidaDesdeHasta(fechaDesde, fechaHasta)) {
				Fechas fechas = new Fechas();
				fechas.setFechaDesde(fechaDesde);
				fechas.setFechaHasta(fechaHasta);
				filtrosVenta.setFechas(fechas);
			}else {
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				model.addAttribute(MENSAJEERROR, MENSAJEFECHASINVALIDAS);
				//-----LLenando Lista Movimientos Compras-------------//
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				
				modelResponseCompra(model, movimientosRequest);	
				return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
				
			}
			
			movimientosRequest.setFiltros(filtrosVenta);
			//-----LLenando Lista Movimientos Ventas-------------//
			modelResponseVenta(model, movimientosRequest);
			model.addAttribute(ESTATUS, estatus);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			//-----LLenando Lista Movimientos Compras-------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			movimientosRequest.setFiltros(filtrosCompra);
			modelResponseCompra(model, movimientosRequest);
			LOGGER.info(SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSVENTAF);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			searchError(model);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		}
	}
	
	
	
	
	@GetMapping("/searchEstatusCompra")
	public String searchEstatusCompra(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		
		LOGGER.info(SOLICITUDCONTROLLERSEARCHESTATUSCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			filtrosCompra.setEstatus(movimientoSearch.getEstatus());
			LOGGER.info(movimientoSearch.getFechas().getFechaDesde());
			LOGGER.info(movimientoSearch.getFechas().getFechaHasta());
			if(libreriaUtil.isFechaValidaDesdeHasta(movimientoSearch.getFechas().getFechaDesde(), movimientoSearch.getFechas().getFechaHasta())) {
				Fechas fechas = new Fechas();
				fechas.setFechaDesde(movimientoSearch.getFechas().getFechaDesde());
				fechas.setFechaHasta(movimientoSearch.getFechas().getFechaHasta());
				filtrosCompra.setFechas(fechas);
			}else {
				model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
				model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
				model.addAttribute(MENSAJEERRORCOMPRA, MENSAJEFECHASINVALIDAS);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosVenta = new Movimiento();
				filtrosVenta.setTipoTransaccion("V");
				movimientosRequest.setFiltros(filtrosVenta);
				
				//-----LLenando Lista Movimientos Ventas-------------//
				modelResponseVenta(model, movimientosRequest);
				return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
			}
			
			
			//-----LLenando Lista Movimientos Compras-------------//
			movimientosRequest.setFiltros(filtrosCompra);
			modelResponseCompra(model, movimientosRequest);
			model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
			model.addAttribute(FECHADESDE, movimientoSearch.getFechas().getFechaDesde());
			model.addAttribute(FECHAHASTA, movimientoSearch.getFechas().getFechaHasta());
			
			//-----LLenando Lista Movimientos Ventas-------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			modelResponseVenta(model, movimientosRequest);
			LOGGER.info(SOLICITUDCONTROLLERSEARCHESTATUSCOMPRAF);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			searchError(model);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		}
		
	}
	
	public Model modelResponseVenta(Model model, MovimientosRequest movimientosRequest) throws CustomException{
		List<Movimiento> listaMovimientosVenta;
		DatosPaginacion datosPaginacionVenta;
		MovimientosResponse responseVenta = responseConsulta(movimientosRequest);
		listaMovimientosVenta = responseVenta.getMovimientos();
		datosPaginacionVenta = responseVenta.getDatosPaginacion();
		if(!listaMovimientosVenta.isEmpty()) {
			for (Movimiento movimiento : listaMovimientosVenta) {
				movimiento.setMontoBsOperacionString(libreriaUtil.formatNumber(movimiento.getMontoBsOperacion()));
				movimiento.setMontoBsClienteString(libreriaUtil.formatNumber(movimiento.getMontoBsCliente()));
				movimiento.setMontoDivisaString(libreriaUtil.formatNumber(movimiento.getMontoDivisa()));
			}
		}else {
			model.addAttribute(MENSAJE, MENSAJENORESULTADO);	
		}
		model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
		model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
		
		return model;
	}
	
	@GetMapping("/listaSolicitudesMovimientosComprasPage")
	public String consultaMovimientoCompraSearchEstatus(@RequestParam("page") int page,@RequestParam("estatus") int estatus,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta,Model model, HttpSession httpSession) {
		LOGGER.info(SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSCOMPRAI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosCompra = new Movimiento();
			filtrosCompra.setTipoTransaccion("C");
			filtrosCompra.setEstatus(estatus);
			
			if(libreriaUtil.isFechaValidaDesdeHasta(fechaDesde, fechaHasta)) {
				Fechas fechas = new Fechas();
				fechas.setFechaDesde(fechaDesde);
				fechas.setFechaHasta(fechaHasta);
				filtrosCompra.setFechas(fechas);
			}else {
				model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
				model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
				model.addAttribute(MENSAJEERRORCOMPRA, MENSAJEFECHASINVALIDAS);
				
				//-----LLenando Lista Movimientos Ventas-------------//
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosVenta = new Movimiento();
				filtrosVenta.setTipoTransaccion("V");
				movimientosRequest.setFiltros(filtrosVenta);
				modelResponseVenta(model, movimientosRequest);
				return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
			}
			
			movimientosRequest.setFiltros(filtrosCompra);
			//-----LLenando Lista Movimientos Compras-------------//
			modelResponseCompra(model, movimientosRequest);
			model.addAttribute(ESTATUS, estatus);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			
			//-----LLenando Lista Movimientos Ventas-------------//
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			modelResponseVenta(model, movimientosRequest);
			LOGGER.info(SOLICITUDCONTROLLERLISTACONSULTASEARCHESTATUSCOMPRAF);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			searchError(model);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		}
	}
	
	@GetMapping("/formSolicitudGenerarReporte")
	public String formSolicitudGenerarReporte(Movimiento movimiento, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		if(!libreriaUtil.isPermisoMenu(httpSession, valorConsultaBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute(LISTAMONEDAS, listaMonedas);
			return URLFORMSOLICITUDGENERARREPORTE;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosVentas/1";
		}
		
	}
	
	
	
	public String fecha(Date fecha) {
		
		String strDateFormat = STRDATEFORMET;   
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); 
        
        return objSDF.format(fecha);
	}
	
	
	public boolean isFechaValida(String fechaLiquidacion) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	
        	
        	Date fechaDate1 = formato.parse(fechaLiquidacion);
        	Date fechaDate2 = formato.parse(fecha(new Date()));
        	
        	if ( fechaDate1.before(fechaDate2) ){
        		LOGGER.info("La fechaLiquidacion es menor que la actual");
        		return false;
        	}else{
        	     if ( fechaDate2.before(fechaDate1) ){
        	    	 LOGGER.info("La fechaActual es menor que la fechaLiquidacion");
        	    	 return true;
        	     }else{
        	    	 LOGGER.info("La fechaActual es igual que la fechaLiquidacion");
        	    	 return true;
        	     } 
        	}
        } 
        catch (ParseException ex) 
        {
        	LOGGER.error(ex.getMessage());
        }
        
        return false;
	}
	
	
	
	@GetMapping("/listaSolicitudesMovimientosVentas")
	public String consultaMovimientoVenta1(Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		movimientosRequest.setNumeroPagina(1);
		movimientosRequest.setTamanoPagina(numeroRegistroPage);
		Movimiento filtros = new Movimiento();
		filtros.setTipoTransaccion("V");
		movimientosRequest.setFiltros(filtros);
		
		try {
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(response != null) {
				DatosPaginacion datosPaginacion = response.getDatosPaginacion();
				List<Movimiento> listaMovimientos = response.getMovimientos();
				model.addAttribute("listaMovimientos", listaMovimientos);
				model.addAttribute("datosPaginacion", datosPaginacion);
				return URLLISTAMOVIMIENTOSVENTA;
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			return REDIRECTINICIO;
		}
	}
	
	
	public Venta evaluarBigDecimalVenta(Venta ventaAcumulado) {
		if(ventaAcumulado.getMonto().compareTo(BigDecimal.ZERO) == 0) { 			
			ventaAcumulado.setMonto(new BigDecimal("0.00"));
			ventaAcumulado.setMontoString(libreriaUtil.formatNumber(new BigDecimal("0.00")));
		}
		ventaAcumulado.setMontoString(libreriaUtil.formatNumber(ventaAcumulado.getMonto()));
		return ventaAcumulado;
	}
	
	public Compra evaluarBigDecimalCompra(Compra compraAcumulado) {
		if(compraAcumulado.getMonto().compareTo(BigDecimal.ZERO) == 0) { 			
			compraAcumulado.setMonto(new BigDecimal("0.00"));
			compraAcumulado.setMontoString(libreriaUtil.formatNumber(new BigDecimal("0.00")));
		}
		compraAcumulado.setMontoString(libreriaUtil.formatNumber(compraAcumulado.getMonto()));
		return compraAcumulado;
	}
	
	public BigDecimal evaluarBigDecimal(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == 0) { 			
			return new BigDecimal("0.00");
		}else {
			return valor;
		}
	}

}
