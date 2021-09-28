package com.bancoexterior.app.convenio.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.AprobarRechazarResponse;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;




@Service
public class MovimientosApiRestImpl implements IMovimientosApiRest{

	private static final Logger LOGGER = LogManager.getLogger(MovimientosApiRestImpl.class);
	
	
	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
    
    @Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientosPorAprobar}")
    private String urlConsultarMovimientosPorAprobar;
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientosPorAprobarVenta}")
    private String urlConsultarMovimientosPorAprobarVenta;
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientos}")
    private String urlConsultarMovimientos;
    
    @Value("${${app.ambiente}"+".movimientos.compra.actualizar}")
    private String urlActualizarMovimientosCompra;
    
    @Value("${${app.ambiente}"+".movimientos.venta.actualizar}")
    private String urlActualizarMovimientosVenta;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Movimientos";
    
    private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARI = "[==== INICIO ConsultarMovimientosPorAprobar Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARF = "[==== FIN ConsultarMovimientosPorAprobar Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAI = "[==== INICIO ConsultarMovimientosPorAprobarVenta Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAF = "[==== FIN ConsultarMovimientosPorAprobarVenta Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSI = "[==== INICIO ConsultarMovimientos Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSF = "[==== FIN ConsultarMovimientos Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARCOMPRAI = "[==== INICIO RechazarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARCOMPRAF = "[==== FIN RechazarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARCOMPRAI = "[==== INICIO AprobarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARCOMPRAF = "[==== FIN AprobarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARVENTAI = "[==== INICIO RechazarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARVENTAF = "[==== FIN RechazarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARVENTAI = "[==== INICIO AprobarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARVENTAF = "[==== FIN AprobarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSI = "[==== INICIO GetListaMvimientos Movimientos Consultas- Service ====]";
	
	private static final String MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSF = "[==== FIN GetListaMovimientos Movimientos Consultas- Service ====]";
    
	public static final char COMA                                 = ',';
	
	public static final char PUNTO                                = '.';
	
	public static final String NUMEROFORMAT                       = "#,##0.00";
	
	
	
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientosPorAprobar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	
	public MovimientosResponse respuesta2xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			return mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
       
	}
	
	public String respuesta4xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobarVenta(MovimientosRequest movimientosRequest)
			throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientosPorAprobarVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}
	
	
	
	@Override
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		LOGGER.info(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientos);
		LOGGER.info(urlConsultarMovimientos);
		retorno = wsService.post(wsrequest);
		LOGGER.info("luego retorno");
		LOGGER.info(retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	@Override
	public String rechazarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICERECHAZARCOMPRAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);								 
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICERECHAZARCOMPRAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	
	
	
	
	public String respuesta2xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			AprobarRechazarResponse aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
            Resultado resultado = aprobarRechazarResponse.getResultado();
            return resultado.getDescripcion();
            
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta4xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta5xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			AprobarRechazarResponse aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
			Resultado resultado = aprobarRechazarResponse.getResultado();
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	@Override
	public String aprobarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICEAPROBARCOMPRAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICEAPROBARCOMPRAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String rechazarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICERECHAZARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICERECHAZARVENTAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String aprobarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICEAPROBARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);										 
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICEAPROBARVENTAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);		 
		     }else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public List<Movimiento> getListaMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		LOGGER.info(MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientos);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSF);
				return respuesta2xxGetListaMovimientos(retorno);
	       	}else {
	       		throw new CustomException(respuesta4xxGetListaMovimientos(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Movimiento> respuesta2xxGetListaMovimientos(WSResponse retorno){
		try {
			MovimientosResponse movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
        	return movimientosResponse.getMovimientos();
        	
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxGetListaMovimientos(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}


	@Override
	public ByteArrayInputStream exportAllData(List<Movimiento> listaMovimientos) throws IOException{
		
		String[] columns = { "Cod. Operacion", "Fecha Operacion", "Codigo Moneda", "Codigo Ibs", "Nro IdCliente", "Cuenta en divisas", "Cuenta en Bolivares", 
				"Monto Divisa", "Monto Bs", "Tasa Cliente", "Tasa Operacion", "Monto Bs Operacion", "Referencia Debito", "Referencia Credito", "Tipo Transaccion", 
				"Estatus", "Fecha Liquidacion", "Tipo Pacto" };

		
	    XSSFSheet sheet;
	    try(XSSFWorkbook workbook = new XSSFWorkbook();
		   ByteArrayOutputStream stream = new ByteArrayOutputStream();) {
	    	

			sheet = workbook.createSheet("Transacciones");
			Row row = sheet.createRow(0);
			
			
			CellStyle style = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
	        font.setBold(true);
	        font.setFontHeight(16);
	        style.setFont(font);
			
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
				Cell cell = row.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(style);
			}

			
			int initRow = 1;
			CellStyle style2 = workbook.createCellStyle();
			XSSFFont font2 = workbook.createFont();
	        font2.setBold(false);
	        font2.setFontHeight(14);
	        style2.setFont(font2);
	       
			for (Movimiento movimiento : listaMovimientos) {
				sheet.autoSizeColumn(initRow);
				row = sheet.createRow(initRow);
				Cell cell = row.createCell(0);
				cell.setCellValue(movimiento.getCodOperacion());
				cell.setCellStyle(style2);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(movimiento.getFechaOperacion());
				cell1.setCellStyle(style2);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(movimiento.getCodMoneda());
				cell2.setCellStyle(style2);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(movimiento.getCodigoIbs());
				cell3.setCellStyle(style2);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(movimiento.getNroIdCliente());
				cell4.setCellStyle(style2);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(movimiento.getCuentaDivisa());
				cell5.setCellStyle(style2);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(movimiento.getCuentaNacional());
				cell6.setCellStyle(style2);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(formatNumber(movimiento.getMontoDivisa()));
				cell7.setCellStyle(style2);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(formatNumber(movimiento.getMontoBsCliente()));
				cell8.setCellStyle(style2);
				Cell cell9 = row.createCell(9);
				cell9.setCellValue(formatNumber(movimiento.getTasaCliente()));
				cell9.setCellStyle(style2);
				Cell cell10 = row.createCell(10);
				cell10.setCellValue(formatNumber(movimiento.getTasaOperacion()));
				cell10.setCellStyle(style2);
				Cell cell11 = row.createCell(11);
				cell11.setCellValue(formatNumber(movimiento.getMontoBsOperacion()));
				cell11.setCellStyle(style2);
				Cell cell12 = row.createCell(12);
				cell12.setCellValue(movimiento.getReferenciaDebito());
				cell12.setCellStyle(style2);
				Cell cell13 = row.createCell(13);
				cell13.setCellValue(movimiento.getReferenciaCredito());
				cell13.setCellStyle(style2);
				Cell cell14 = row.createCell(14);
				cell14.setCellValue(compraVenta(movimiento.getTipoTransaccion()));
				cell14.setCellStyle(style2);
				Cell cell15 = row.createCell(15);
				cell15.setCellValue(getEstatus(movimiento.getEstatus()));
				cell15.setCellStyle(style2);
				Cell cell16 = row.createCell(16);
				cell16.setCellValue(movimiento.getFechaValor());
				cell16.setCellStyle(style2);
				Cell cell17 = row.createCell(17);
				cell17.setCellValue(movimiento.getTipoPacto());
				cell17.setCellStyle(style2);
				initRow++;
			}
			
			workbook.write(stream);
			
			return new ByteArrayInputStream(stream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		} 
		

		
		
		

		
	}
	
	public String compraVenta(String tipoTransaccion) {
    	String valor = ""; 
    	if(tipoTransaccion.equals("C")) {
    		valor = "Compra"; 
         }else {
        	 valor = "Venta";
         }
    	return valor;
    }
	
	public String getEstatus(Integer estatus) {
    	String valor = ""; 
    	if(estatus == 0) {
    		valor = "Por Aprobar";
        }else {
        	if(estatus == 1) {
        		valor = "Aprobada Automática";
            }else {
            	if(estatus == 2) {
            		valor = "Aprobada Funcional";
                }else {
                	if(estatus == 3) {
                		valor = "Rechazada Automática";
                    }else {
                    	valor = "Rechazada Funcional";
                    }
                }
            }
        }
    	return valor;
    }
	
	public  String formatNumber(BigDecimal numero) {
		
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(COMA);
        decimalFormatSymbols.setGroupingSeparator(PUNTO);
        DecimalFormat df = new DecimalFormat(NUMEROFORMAT, decimalFormatSymbols);
        
         return df.format(numero);
        
    }

}
