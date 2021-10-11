package com.bancoexterior.app.cce.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.CceTransaccion;


public interface ICceTransaccionService {
 
    public CceTransaccionDto findByEndtoendId(String endtoendId);
    
    
    public Page<CceTransaccion> consultaMovimientosConFechasPageFinal(int tipoTransaccion, String bancoDestino, String numeroIdentificacion, 
			String fechaDesde, String fechaHasta, int page);
    
    public Page<CceTransaccion> consultaMovimientosConFechasPageExcel(int tipoTransaccion, String bancoDestino, String numeroIdentificacion, 
			String fechaDesde, String fechaHasta, int page);
    
    public ByteArrayInputStream exportAllData(List<CceTransaccion> listaTransaccionesDto) throws IOException;
    
    public int countTransaccionByCodTransaccion(String codTransaccion);
}
