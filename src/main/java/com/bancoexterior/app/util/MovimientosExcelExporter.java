package com.bancoexterior.app.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.bancoexterior.app.cce.dto.CceTransaccionDto;



public class MovimientosExcelExporter {
	
	public static final char COMA                                 = ',';
	
	public static final char PUNTO                                = '.';
	
	public static final String NUMEROFORMAT                       = "#,##0.00";
	
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
	List<CceTransaccionDto> listaTransaccionesDto;

	public MovimientosExcelExporter(List<CceTransaccionDto> listaTransaccionesDto) {
		
	    this.listaTransaccionesDto = listaTransaccionesDto;
	    workbook = new XSSFWorkbook();
	}

	
	
	
	private void createCellMovimiento(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cellMovimiento = row.createCell(columnCount);
        if (value instanceof Integer) {
            cellMovimiento.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cellMovimiento.setCellValue((Boolean) value);
        }else {
            cellMovimiento.setCellValue((String) value);
        }
        cellMovimiento.setCellStyle(style);
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
     
	public String cuentaOrdenante(CceTransaccionDto cceTransaccionDto) {
		String cuentaOrdenante = "";
		if(cceTransaccionDto.getCodTransaccion().equals("5724") || cceTransaccionDto.getCodTransaccion().equals("9734") || cceTransaccionDto.getCodTransaccion().equals("9742") || cceTransaccionDto.getCodTransaccion().equals("9743") 
				|| cceTransaccionDto.getCodTransaccion().equals("5728") || cceTransaccionDto.getCodTransaccion().equals("9738")) {
			cuentaOrdenante = cceTransaccionDto.getCuentaDestino();
		}else {
			cuentaOrdenante = cceTransaccionDto.getCuentaOrigen();
		}
		
		
		return cuentaOrdenante;
	}
	
	public String cuentaBeneficiario(CceTransaccionDto cceTransaccionDto) {
		String cuentaBeneficiario = "";
		if(cceTransaccionDto.getCodTransaccion().equals("5724") || cceTransaccionDto.getCodTransaccion().equals("9734") || cceTransaccionDto.getCodTransaccion().equals("9742") || cceTransaccionDto.getCodTransaccion().equals("9743") 
				|| cceTransaccionDto.getCodTransaccion().equals("5728") || cceTransaccionDto.getCodTransaccion().equals("9738")) {
			cuentaBeneficiario = cceTransaccionDto.getCuentaOrigen();
		}else {
			cuentaBeneficiario = cceTransaccionDto.getCuentaDestino();
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
	
	
	
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
	
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (CceTransaccionDto cceTransaccionDto : listaTransaccionesDto) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCellMovimiento(row, columnCount++, cceTransaccionDto.getEndtoendId(), style);
            createCellMovimiento(row, columnCount++, cceTransaccionDto.getReferencia(), style);
            createCellMovimiento(row, columnCount++, tipoTransaccion(cceTransaccionDto.getTipoTransaccion()), style);
            createCellMovimiento(row, columnCount++, cuentaOrdenante(cceTransaccionDto), style);
            createCellMovimiento(row, columnCount++, cuentaBeneficiario(cceTransaccionDto), style);
            createCellMovimiento(row, columnCount++, formatNumber(cceTransaccionDto.getMonto()), style);
            createCellMovimiento(row, columnCount++, estado(cceTransaccionDto.getEstadobcv()), style);
            createCellMovimiento(row, columnCount++, getCorteLiquidacion(cceTransaccionDto.getCorteLiquidacion()), style);
            createCellMovimiento(row, columnCount, getFechaLiquidaBcv(cceTransaccionDto.getFechaLiquidaBcv()), style);
        }
    }
    
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCellMovimiento(row, 0, "Referencia BCV", style);      
        createCellMovimiento(row, 1, "Referencia IBS", style);       
        createCellMovimiento(row, 2, "Tipo Transaccion", style);    
        createCellMovimiento(row, 3, "Cta. Ordenante", style);
        createCellMovimiento(row, 4, "Cta. Beneficiario", style);
        createCellMovimiento(row, 5, "Monto", style);      
        createCellMovimiento(row, 6, "Estado", style);
        createCellMovimiento(row, 7, "Corte Liquidacion", style);      
        createCellMovimiento(row, 8, "Fecha Liquidacion BCV", style);
        
         
    }
    
    public  String formatNumber(BigDecimal numero) {
		
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(COMA);
        decimalFormatSymbols.setGroupingSeparator(PUNTO);
        DecimalFormat df = new DecimalFormat(NUMEROFORMAT, decimalFormatSymbols);
        
         return df.format(numero);
        
    }
}
