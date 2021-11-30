package com.bancoexterior.app.cce.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancoexterior.app.cce.model.CceCuentasUnicasBcv;
import com.bancoexterior.app.cce.service.ICceCuentasUnicasBcvService;

@RestController
@RequestMapping("/cuentasUnicasBcvRest")
public class CceCuentasUnicasBcvRestController {

	
	@Autowired
	private ICceCuentasUnicasBcvService cceCuentasUnicasBcvService;
	
	@GetMapping("/{codigoBic}")
	public CceCuentasUnicasBcv findById(@PathVariable("codigoBic") String codigoBic) {
		
		return cceCuentasUnicasBcvService.findById(codigoBic);
		
		
	}
	
	
	@GetMapping("/consulta")
	public List<CceCuentasUnicasBcv> findAll() {
		List<CceCuentasUnicasBcv> lista = new ArrayList<>();
		
		CceCuentasUnicasBcv cceCuentasUnicasBcv1 = new CceCuentasUnicasBcv();
		cceCuentasUnicasBcv1.setCodigoBic("0115");
		cceCuentasUnicasBcv1.setCuenta("00010001360002000115");
		cceCuentasUnicasBcv1.setNombreBanco("Banco Exterior C.A. Banco Universal");
		cceCuentasUnicasBcv1.setStatus("A");
		lista.add(cceCuentasUnicasBcv1);
		
		CceCuentasUnicasBcv cceCuentasUnicasBcv2 = new CceCuentasUnicasBcv();
		cceCuentasUnicasBcv2.setCodigoBic("0102");
		cceCuentasUnicasBcv2.setCuenta("00010001340002000102");
		cceCuentasUnicasBcv2.setNombreBanco("Banco Venezuela");
		cceCuentasUnicasBcv2.setStatus("A");
		lista.add(cceCuentasUnicasBcv2);
		
		return lista;
	}
}
