package com.bancoexterior.app.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
 
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import com.bancoexterior.app.convenio.model.Movimiento;


public class ConsultaExcelExporter {
	
	public static final char COMA                                 = ',';
	
	public static final char PUNTO                                = '.';
	
	public static final String NUMEROFORMAT                       = "#,##0.00";
	
	private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Movimiento> listaMovimientos;
    
    
     
    public ConsultaExcelExporter(List<Movimiento> listaMovimientos) {
       this.listaMovimientos = listaMovimientos;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
         
        createCell(row, 0, "Cod. Operacion", style);      
        createCell(row, 1, "Fecha Operacion", style);
        createCell(row, 2, "Codigo Moneda", style);
        createCell(row, 3, "Codigo Ibs", style);
        createCell(row, 4, "Nro IdCliente", style);
        createCell(row, 5, "Cuenta en divisas", style);    
        createCell(row, 6, "Cuenta en Bolivares", style);
        createCell(row, 7, "Monto Divisa", style);
        createCell(row, 8, "Monto Bs", style); 
        createCell(row, 9, "Tasa Cliente", style);
        createCell(row, 10, "Tasa Operacion", style);
        createCell(row, 11, "Monto Bs Operacion", style);
        createCell(row, 12, "Referencia Debito", style);
        createCell(row, 13, "Referencia Credito", style);
        createCell(row, 14, "Tipo Transaccion", style);
        createCell(row, 15, "Estatus", style);       
        createCell(row, 16, "Fecha Liquidacion", style);
        createCell(row, 17, "Tipo Pacto", style); 
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (Movimiento movimiento : listaMovimientos) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, movimiento.getCodOperacion(), style);
            createCell(row, columnCount++, movimiento.getFechaOperacion(), style);
            createCell(row, columnCount++, movimiento.getCodMoneda(), style);
            createCell(row, columnCount++, movimiento.getCodigoIbs(), style);
            createCell(row, columnCount++, movimiento.getNroIdCliente(), style);
            createCell(row, columnCount++, movimiento.getCuentaDivisa(), style);
            createCell(row, columnCount++, movimiento.getCuentaNacional(), style);
            createCell(row, columnCount++, formatNumber(movimiento.getMontoDivisa()), style);
            createCell(row, columnCount++, formatNumber(movimiento.getMontoBsCliente()), style);
            createCell(row, columnCount++, formatNumber(movimiento.getTasaCliente()), style); 
            createCell(row, columnCount++, formatNumber(movimiento.getTasaOperacion()), style);
            createCell(row, columnCount++, formatNumber(movimiento.getMontoBsOperacion()), style);
            createCell(row, columnCount++, movimiento.getReferenciaDebito(), style);
            createCell(row, columnCount++, movimiento.getReferenciaCredito(), style);
            createCell(row, columnCount++, compraVenta(movimiento.getTipoTransaccion()), style);
            createCell(row, columnCount++, getEstatus(movimiento.getEstatus()), style);
            createCell(row, columnCount++, movimiento.getFechaValor(), style);	
            createCell(row, columnCount, movimiento.getTipoPacto(), style);
             
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
    
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
}
