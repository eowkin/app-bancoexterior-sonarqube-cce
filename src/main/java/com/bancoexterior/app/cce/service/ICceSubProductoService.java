package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.model.CceSubProducto;

public interface ICceSubProductoService {

	public List<CceSubProducto> listaSubproductos();
	
	public CceSubProducto buscarSubProductoPorCodProductoAndCodSubProducto(String codProducto, String codSubProducto);
}
