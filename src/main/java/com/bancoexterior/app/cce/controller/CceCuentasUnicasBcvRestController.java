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
		
		/*
		CceCuentasUnicasBcv cceCuentasUnicasBcv = new CceCuentasUnicasBcv();
		
		if(codigoBic.equals("0115")) {
			cceCuentasUnicasBcv.setCodigoBic(codigoBic);
			cceCuentasUnicasBcv.setCuenta("00010001360002000115");
			cceCuentasUnicasBcv.setNombreBanco("Banco Exterior C.A. Banco Universal");
			cceCuentasUnicasBcv.setStatus("A");
		}else {
			if(codigoBic.equals("0102")) {
				cceCuentasUnicasBcv.setCodigoBic(codigoBic);
				cceCuentasUnicasBcv.setCuenta("00010001340002000102");
				cceCuentasUnicasBcv.setNombreBanco("Banco Venezuela");
				cceCuentasUnicasBcv.setStatus("A");
			}else {
				if(codigoBic.equals("0105")) {
					cceCuentasUnicasBcv.setCodigoBic(codigoBic);
					cceCuentasUnicasBcv.setCuenta("00010001390002000105");
					cceCuentasUnicasBcv.setNombreBanco("Banco Mercantil");
					cceCuentasUnicasBcv.setStatus("A");
				}else {
					if(codigoBic.equals("0108")) {
						cceCuentasUnicasBcv.setCodigoBic(codigoBic);
						cceCuentasUnicasBcv.setCuenta("00010001330002000108");
						cceCuentasUnicasBcv.setNombreBanco("Banco Provincial");
						cceCuentasUnicasBcv.setStatus("A");
					}else {
						cceCuentasUnicasBcv.setCodigoBic(codigoBic);
						cceCuentasUnicasBcv.setCuenta("00010001380002000114");
						cceCuentasUnicasBcv.setNombreBanco("Banco del Caribe");
						cceCuentasUnicasBcv.setStatus("A");
					}
				}
			}
		}
		
		return cceCuentasUnicasBcv;*/
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
