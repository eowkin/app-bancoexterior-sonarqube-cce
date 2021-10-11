package com.bancoexterior.app.cce.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.repository.ICceTransaccionRepository;
import com.bancoexterior.app.util.Mapper;




@Service
public class CceTransaccionServiceImpl implements ICceTransaccionService{

	private static final Logger LOGGER = LogManager.getLogger(CceTransaccionServiceImpl.class);
	
	
	@Autowired 
	private ICceTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEI = "[==== INICIO ConsultaMovimientosConFechasPage CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEF = "[==== FIN ConsultaMovimientosConFechasPage CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICEFINDBYENDTOENDIDI = "[==== INICIO FindByEndtoendId CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICEFINDBYENDTOENDIDF = "[==== FIN FindByEndtoendId CceTransaccion Consultas - Service ====]";
	
	private static final String HORADESDE = " 00:00:00";
	
	private static final String HORAHASTA = " 23:59:59";
	
	public static final char COMA                                 = ',';
	
	public static final char PUNTO                                = '.';
	
	public static final String NUMEROFORMAT                       = "#,##0.00";
	
	
		
	@Override
	public CceTransaccionDto findByEndtoendId(String endtoendId) {
		LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDI);
		CceTransaccion cceTransaccion = repo.getTransaccionByEndToEndId(endtoendId);
		if(cceTransaccion != null) {
			LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDF);
			return mapper.map(cceTransaccion, CceTransaccionDto.class);
		}
		LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDF);
		return null;
	}
	
	
	
	@Override
	public Page<CceTransaccion> consultaMovimientosConFechasPageFinal(int tipoTransaccion, String bancoDestino,
			String numeroIdentificacion, String fechaDesde, String fechaHasta, int page) {
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEI);
		fechaDesde = fechaDesde +HORADESDE;
		fechaHasta = fechaHasta +HORAHASTA;
		int pageNumber = page;
		int pageSize = 10;

		Sort sort = Sort.by("fecha_modificacion").ascending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		LOGGER.info(tipoTransaccion);
		
		
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEF);	
		return repo.consultaMovimientosFinal(tipoTransaccion, bancoDestino, numeroIdentificacion, fechaDesde, fechaHasta, pageable);
		
		
		
	}
	

	@Override
	public Page<CceTransaccion> consultaMovimientosConFechasPageExcel(int tipoTransaccionExcel, String bancoDestinoExcel,
			String numeroIdentificacionExcel, String fechaDesdeExcel, String fechaHastaExcel, int page) {
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEI);
		fechaDesdeExcel = fechaDesdeExcel +HORADESDE;
		fechaHastaExcel = fechaHastaExcel +HORAHASTA;
		int pageNumber = page;
		int pageSize = 10000;

		Sort sort = Sort.by("fecha_modificacion").ascending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEF);	
		return repo.consultaMovimientosFinal(tipoTransaccionExcel, bancoDestinoExcel, numeroIdentificacionExcel, fechaDesdeExcel, fechaHastaExcel, pageable);
	}

	@Override
	public ByteArrayInputStream exportAllData(List<CceTransaccion> listaTransacciones) throws IOException {
		String[] columns = { "Referencia BCV", "Referencia IBS", "Tipo Transaccion", "Cta. Ordenante", "Cta. Beneficiario", "Monto", "Estado", "Corte Liquidacion", "Fecha Liquidacion BCV" };

		XSSFSheet sheet;
		try(XSSFWorkbook workbookCce = new XSSFWorkbook();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();){

			sheet = workbookCce.createSheet("Transacciones");
			Row row = sheet.createRow(0);
		
		
			CellStyle style = workbookCce.createCellStyle();
			XSSFFont font = workbookCce.createFont();
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
			CellStyle style2 = workbookCce.createCellStyle();
			XSSFFont font2 = workbookCce.createFont();
	        font2.setBold(false);
	        font2.setFontHeight(14);
	        style2.setFont(font2);
	       
			for (CceTransaccion cceTransaccion : listaTransacciones) {
				
				sheet.autoSizeColumn(initRow);
				row = sheet.createRow(initRow);
				Cell cell = row.createCell(0);
				cell.setCellValue(cceTransaccion.getEndtoendId());
				cell.setCellStyle(style2);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(cceTransaccion.getReferencia());
				cell1.setCellStyle(style2);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(tipoTransaccion(cceTransaccion.getTipoTransaccion()));
				cell2.setCellStyle(style2);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(cuentaOrdenante(cceTransaccion));
				cell3.setCellStyle(style2);
				Cell cell4 = row.createCell(4);
				cell4.setCellValue(cuentaBeneficiario(cceTransaccion));
				cell4.setCellStyle(style2);
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(formatNumber(cceTransaccion.getMonto()));
				cell5.setCellStyle(style2);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(estado(cceTransaccion.getEstadobcv()));
				cell6.setCellStyle(style2);
				Cell cell7 = row.createCell(7);
				cell7.setCellValue(getCorteLiquidacion(cceTransaccion.getCorteLiquidacion()));
				cell7.setCellStyle(style2);
				Cell cell8 = row.createCell(8);
				cell8.setCellValue(getFechaLiquidaBcv(cceTransaccion.getFechaLiquidaBcv()));
				cell8.setCellStyle(style2);
				initRow++;
			}

			workbookCce.write(stream);
			
			return new ByteArrayInputStream(stream.toByteArray());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		} 
	}

	public String tipoTransaccion(String tipo) {
		
		String tipoTransaccion = "";
		if(tipo.equals("801")) {
			tipoTransaccion = "Interbancaria";
		}else {
			if(tipo.equals("802")) {
				tipoTransaccion = "Intrabancaria";
			}else {
				if(tipo.equals("803")) {
					tipoTransaccion = "Interbancaria ONT";
				}else {
					if(tipo.equals("804")) {
						tipoTransaccion = "Intrabancaria ONT";
					}else {
						tipoTransaccion = "Crédito Inmediato";
					}
				}
			}
		}
		
		return tipoTransaccion;
	}
     
	public String cuentaOrdenante(CceTransaccion cceTransaccion) {
		String cuentaOrdenante = "";
		if(cceTransaccion.isEnvio()) {
			cuentaOrdenante = cceTransaccion.getCuentaOrigen();
		}else {
			cuentaOrdenante = cceTransaccion.getCuentaDestino();
		}
		
		
		return cuentaOrdenante;
	}
	
	public String cuentaBeneficiario(CceTransaccion cceTransaccion) {
		String cuentaBeneficiario = "";
		if(cceTransaccion.isEnvio()) {
			cuentaBeneficiario = cceTransaccion.getCuentaDestino();	
		}else {
			cuentaBeneficiario = cceTransaccion.getCuentaOrigen();
		}
		
		
		return cuentaBeneficiario;
	}
	
	public String estado(String estadobcv) {
		String estado = "";
		
		if(estadobcv == null) {
			estado = "Incompleta";
		}else {
			if(estadobcv.equals("ACCP")) {
				estado = "Aprobada";
			}else {
				estado = "Rechazada";
			}
		}	
		return estado;
	}
	
	public String getCorteLiquidacion(Integer corteLiquidacion) {
		String corteLiquidacionRes = "";
		
		if(corteLiquidacion == null) {
			corteLiquidacionRes = "No Asigando";
		}else {
			corteLiquidacionRes = corteLiquidacion.toString();
		}	
		return corteLiquidacionRes;
	}
	
	
	public String getFechaLiquidaBcv(Date fechaLiquidaBcv) {
		String fechaLiquidaBcvRes = "";
		
		if(fechaLiquidaBcv == null) {
			fechaLiquidaBcvRes = "No Asigando";
		}else {
			fechaLiquidaBcvRes = fechaLiquidaBcv.toString();
		}	
		return fechaLiquidaBcvRes;
	}
	
	public  String formatNumber(BigDecimal numero) {
		
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(COMA);
        decimalFormatSymbols.setGroupingSeparator(PUNTO);
        DecimalFormat df = new DecimalFormat(NUMEROFORMAT, decimalFormatSymbols);
        
         return df.format(numero);
        
    }



	@Override
	public int countTransaccionByCodTransaccion(String codTransaccion) {
		return repo.countTransaccionByCodTransaccion(codTransaccion);
	}

	

	

}
