package com.bancoexterior.app.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.inicio.model.Menu;




@Component
public class LibreriaUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(LibreriaUtil.class);
	                                              //                "#,##0.00"
    public static final String NUMEROFORMAT                       = "#,##0.00";
	
    public static final char COMA                                 = ',';
    
    public static final char PUNTO                                = '.';
    
    private static final String STRDATEFORMET = "yyyy-MM-dd";
 
    public static final String CHANNEL = "0005"; 
    
    public static final String IDCDTRACCTGENERICO = "00010001300003000174";
    
    @Value("${trasaccionesAprobarMicroservicio.rmtinf}")
    private String valorRmtinf;
    
	public String obtenerIdSesion() {
		LocalDateTime ahora = LocalDateTime.now();
		String valorAno = "";
		valorAno = ahora.getYear()+"";
		
		
		String valorMes = getValorMes(ahora);
		
		String valorDia = getValorDia(ahora);
		
		
		String valorHora = "";
		if(ahora.getHour() < 10) {
			valorHora = "0"+ahora.getHour();
		}else {
			valorHora = ""+ahora.getHour();
		}
		
		
		String valorMin = "";
		if(ahora.getMinute() < 10) {
			valorMin = "0"+ahora.getMinute();
		}else {
			valorMin = ""+ahora.getMinute();
		}
		
		
		String valorSeg = "";
		if(ahora.getSecond() < 10) {
			valorSeg = "0"+ahora.getSecond();
		}else {
			valorSeg = ""+ahora.getSecond();
		}
		
		
	
		return valorAno+valorMes+valorDia+valorHora+valorMin+valorSeg;
	}
	
	
	public String obtenerFechaYYYYMMDD() {
		LocalDateTime ahora = LocalDateTime.now();
		String valorAno = "";
		valorAno = ahora.getYear()+"";
		
		String valorMes = getValorMes(ahora);
		
		String valorDia = getValorDia(ahora);
	
		return valorAno+"-"+valorMes+"-"+valorDia;
	}
	public String obtenerIdSesionCce() {
		LocalDateTime ahora = LocalDateTime.now();
		String valorAno = "";
		valorAno = ahora.getYear()+"";
	
		String valorMes = getValorMes(ahora);
		
		String valorDia = getValorDia(ahora);
		
		return valorDia+valorMes+valorAno;
	}
	
	public String obtenerFechaHoy() {
		LocalDateTime ahora = LocalDateTime.now();
		String valorAno = "";
		valorAno = ahora.getYear()+"";
		
		String valorMes = getValorMes(ahora);
	
		String valorDia = getValorDia(ahora);
	
		return valorDia+"/"+valorMes+"/"+valorAno;
	}

	
	public String getValorMes(LocalDateTime ahora) {
		String valorMes = "";
		if(ahora.getMonthValue() < 10) {
			valorMes = "0"+ahora.getMonthValue();
		}else {
			valorMes = ""+ahora.getMonthValue();
		}
		
		return valorMes;
	}
	
	
	public String getValorDia(LocalDateTime ahora) {
		String valorDia = "";
		if(ahora.getDayOfMonth() < 10) {
			valorDia = "0"+ahora.getDayOfMonth();
		}else {
			valorDia = ""+ahora.getDayOfMonth();
		}
		
		return valorDia;
	}
	
	/**
	* Convierte un tipo de dato String a BigDecimal.
	* Ideal para obtener el dato de un JTextField u otro componente y realizar las operaciones
	* matemáticas sobre ese dato.
	* @param num
	* @return BigDecimal
	*/
	public  BigDecimal stringToBigDecimal(String num){
		//se inicializa en 0
		BigDecimal money = BigDecimal.ZERO;
		//sino esta vacio entonces
		if(!num.isEmpty()){
			/**
			* primero elimina los puntos y luego remplaza las comas en puntos.
			*/
			String formatoValido = num.replace(".", "").replace(",", ".");
			money = new BigDecimal(formatoValido);
		}//if
		return money;
	}//metodo
	/**
	* Convierte un tipo de dato BigDecimal a String.
	* Ideal para mostrar el dato BigDecimal en un JTextField u otro componente de texto.
	* @param big
	* @return String
	*/
	public String bigDecimalToString(BigDecimal big){
		double datoDoubleD = 0;
		//se verifica que sean correctos los argumentos recibidos
		if(big != null)
			datoDoubleD = big.doubleValue();
		/**
		* Los # indican valores no obligatorios
		* Los 0 indican que si no hay valor se pondrá un cero
		*/
		NumberFormat formatter = new DecimalFormat(NUMEROFORMAT);
		return formatter.format(datoDoubleD);
	}//metodo
	
	public  String formatNumber(BigDecimal numero) {
		
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(COMA);
        decimalFormatSymbols.setGroupingSeparator(PUNTO);
        DecimalFormat df = new DecimalFormat(NUMEROFORMAT, decimalFormatSymbols);
        
         return df.format(numero);
        
    }
	
	   
    public String fechayhora() {
        DateFormat fecha = new SimpleDateFormat(STRDATEFORMET);
        DateFormat hora = new SimpleDateFormat("hh:mm:ss");
        String fechacompleta;   
        fechacompleta= fecha.format(new Date()) + "T" + hora.format(new Date()) ;
        return fechacompleta;
       
    } 
    
    public String getChannel(String idCanal) {
        return idCanal.substring(idCanal.length() - 1);
    }
	
    public String getIdCdtrAcct() {
        return IDCDTRACCTGENERICO;

    }
    
    public String getIdCdtrAgtAcct() {
        return IDCDTRACCTGENERICO;

    }
    
    public String getRmtInf() {
        return valorRmtinf;

    }
    
    
    public String getSchmeNm(String nroIdEmisor) {
    	char valor = nroIdEmisor.charAt(0);
    
    	if(valor == 'J' || valor == 'j' || valor == 'G' || valor == 'g' || valor == 'C' || valor == 'c' || valor == 'R' || valor == 'r') {
    		return "SRIF";
    	}else {
    		if(valor == 'V' || valor == 'v' || valor == 'E' || valor == 'e') {
    			return "SCID";
    		}else {
    			if(valor == 'P' || valor == 'p') {
    				return "SPAS";
    			}else {
    				return "NO-ASIGNADO";
    			}
    		}
    	}
    }
    
    public String getProducto(String codProducto) {
    	return codProducto.substring(1,codProducto.length());
    }
    
    public String getEndToEndId(String bancoEmisor, String referencia) {
    	
    	return bancoEmisor+obtenerIdSesion()+getReferencia(referencia);
    }
    
    public String getReferencia(String referencia) {
    	StringBuilder bld = new StringBuilder();
    	if(referencia.length() == 8) {
    		return referencia;
    	}else {
    		if(referencia.length() < 8) {
    			for (int i = 0; i < 8 - referencia.length(); i++) {
    				bld.append("0");
				}
    			return bld.toString() + referencia;
    		}else {
    			return referencia;
    		}
    			
    	}
    		
    	
    }
    
    public String getReferenciaUltimosOchoDigitos(String referencia) {
    	
    	if(referencia.length() == 8) {
    		return referencia;
    	}else {
    		if(referencia.length() > 8) {
    			return referencia.substring(referencia.length() - 8, referencia.length());
    		}else {
    			return referencia;
    		}
    			
    	}
    		
    	
    }
    
    
    
public boolean isFechaDesdeHastaIgual(String fechaDesde, String fechaHasta) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	
        	
        	Date fechaDate1Igual = formato.parse(fechaDesde);
        	Date fechaDate2Igual = formato.parse(fechaHasta);
        	
        	if ( fechaDate2Igual.before(fechaDate1Igual) ){
        		LOGGER.info("La fechaHasta es menor que la fechaDesde");
        		return false;
        	}else{
        	     if ( fechaDate1Igual.before(fechaDate2Igual) ){
        	    	 LOGGER.info("La fechaDesde es menor que la fechaHasta");
        	    	 return false;
        	     }else{
        	    	 LOGGER.info("La fechaDesde es igual que la fechaHasta");
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
    
    
    
    
    public boolean isFechaValidaDesdeHasta(String fechaDesde, String fechaHasta) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	
        	
        	Date fechaDate1 = formato.parse(fechaDesde);
        	Date fechaDate2 = formato.parse(fechaHasta);
        	
        	if ( fechaDate2.before(fechaDate1) ){
        		LOGGER.info("La fechaHasta es menor que la fechaDesde");
        		return false;
        	}else{
        	     if ( fechaDate1.before(fechaDate2) ){
        	    	 LOGGER.info("La fechaDesde es menor que la fechaHasta");
        	    	 return true;
        	     }else{
        	    	 LOGGER.info("La fechaDesde es igual que la fechaHasta");
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
    
    public BigDecimal montoSerch(BigDecimal numero) {
		if(numero != null) {
			return stringToBigDecimal(formatNumber(numero));
		}
		return new BigDecimal("0.00");
	}
	
	
	public boolean isPermisoMenu(HttpSession httpSession, int valor) {
		List<Menu> listaMenu = (List<Menu>)httpSession.getAttribute("listaMenu");
		List<Integer> listaMenuInt = new ArrayList<>();
		boolean permiso = false;
		if(listaMenu != null) {
			for (Menu menu : listaMenu) {
				listaMenuInt.add(menu.getIdMenu());
			}
			
			for (Integer intMenu : listaMenuInt) {
				if(intMenu == valor)
					permiso = true;
			}
			return permiso;
		}else {
			return permiso;
		}
		
	}
	
	
	
	public BigDecimal montoAprobacionesLotes(List<BCVLBT> listaBCVLBTPorAprobarLotes) {
		
		double d = 0.0;

		BigDecimal montoAprobacionesLotes = BigDecimal.valueOf(d);
		
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobarLotes) {
			montoAprobacionesLotes = montoAprobacionesLotes.add(bcvlbt.getMonto());
		}
		
		return montoAprobacionesLotes;
	}
	
	public boolean isMontoDesdeMontoHastaDistintoNull(BigDecimal montoDesde, BigDecimal montoHasta) {
		
		return montoDesde != null && montoHasta != null;
		
	}
	
	public boolean isFechaHoraValidaDesdeHasta(String fechaHoraDesde, String fechaHoraHasta) {
		
		String[] arrOfFechaD = fechaHoraDesde.split("T");
        
        String fechaDesde = arrOfFechaD[0];
        String horaDesde = arrOfFechaD[1];
        
        String[] arrOfFechaH = fechaHoraHasta.split("T");
        
        String fechaHasta = arrOfFechaH[0];
        String horaHasta = arrOfFechaH[1];
        
        if(isFechaValidaDesdeHasta(fechaDesde, fechaHasta)){
        	return isHoraValidaDesdeHasta(fechaDesde, fechaHasta, horaDesde, horaHasta);
        }else {
        	return false;
        }
		
	}
	
	public boolean isHoraValidaDesdeHasta(String fechaDesde, String fechaHasta,String horaDesde, String horaHasta) {
    	LOGGER.info("isHoraValidaDesdeHasta");
		String[] arrOfHoraD = horaDesde.split(":");
        
        String hDesde = arrOfHoraD[0];
        
        int hDesdeInt = Integer.parseInt(hDesde);
        String minutoDesde = arrOfHoraD[1];
        int minutoDesdeInt = Integer.parseInt(minutoDesde);
        
        String[] arrOfHoraH = horaHasta.split(":");
        
        String hHasta = arrOfHoraH[0];
        int hHastaInt = Integer.parseInt(hHasta);
        String minutoHasta = arrOfHoraH[1];
        int minutoHastaInt = Integer.parseInt(minutoHasta);
        
		if(isFechaDesdeHastaIgual(fechaDesde, fechaHasta)) {
			LOGGER.info("isFechaDesdeHastaIgual");
			if(hDesdeInt == hHastaInt) {
				LOGGER.info("hora iguales");
				if(minutoDesdeInt == minutoHastaInt) {
					LOGGER.info("minutos iguales");
					return true;
				}else {
					LOGGER.info("minutos distintos");
					return minutoDesdeInt < minutoHastaInt;
				}
			}else {
				LOGGER.info("horas distintos");
				return hDesdeInt < hHastaInt;					
			}
		}
		
		return true;
	}
	
	public boolean isMinutoDesdeMayorMinutoHasta(int minutoDesdeInt, int minutoHastaInt) {
		return minutoDesdeInt > minutoHastaInt;
	}
	
	
	
	public BigDecimal montoAprobarOperacionesSeleccionadas(List<BCVLBT> listaBCVLBTPorAprobarLotes) {
		
		double d = 0.0;
		
		BigDecimal montoAprobarOperacionesSeleccionadas = BigDecimal.valueOf(d);
		
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobarLotes) {
			if(bcvlbt.isSeleccionado()) {
				montoAprobarOperacionesSeleccionadas = montoAprobarOperacionesSeleccionadas.add(bcvlbt.getMonto());
			}
				
		}
		
		return montoAprobarOperacionesSeleccionadas;
	}
	
	
	public Date getFechaDate(String fecha) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	return formato.parse(fecha);  	
        }catch (ParseException ex){
        	LOGGER.error(ex.getMessage());
        	return null;
        }
   }
	
	public boolean isFechaValidaDesdeHastaDate(Date fechaHoy, Date fechaValor) {
	
		
		if(fechaValor.before(fechaHoy) ){
        	LOGGER.info("La fechaHasta es menor que la fechaDesde");
        	return false;
        }else{
        	if(fechaHoy.before(fechaValor) ){
        	    LOGGER.info("La fechaDesde es menor que la fechaHasta");
        	    return true;
        	}else{
        	    LOGGER.info("La fechaDesde es igual que la fechaHasta");
        	    return true;
        	} 
        }
       
	}	
	
	public boolean isFechaIgualDate(Date fechaHoy, Date fechaValor) {
	
		
		if(fechaValor.before(fechaHoy) ){
        	LOGGER.info("La fechaHasta es menor que la fechaDesde");
        	return false;
        }else{
        	if(fechaHoy.before(fechaValor) ){
        	    LOGGER.info("La fechaDesde es menor que la fechaHasta");
        	    return false;
        	}else{
        	    LOGGER.info("La fechaDesde es igual que la fechaHasta");
        	    return true;
        	} 
        }
       
	}	
	
	
	public boolean isFechaFinDeSemana(Date fecha) {
	
		Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        	 
           
	}
	
	
 }
