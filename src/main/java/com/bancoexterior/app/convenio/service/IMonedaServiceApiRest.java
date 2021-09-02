package com.bancoexterior.app.convenio.service;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;



public interface IMonedaServiceApiRest {
	
	
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException;
	
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException;
	
	public boolean existe(MonedasRequest monedasRequest) throws CustomException;
	
	public String actualizar(MonedasRequest monedasRequest) throws CustomException;
	
	public String actualizar(MonedasRequest monedasRequest, String accion, HttpServletRequest request) throws CustomException;
	
	public String crear(MonedasRequest monedasRequest) throws CustomException;
	
}
