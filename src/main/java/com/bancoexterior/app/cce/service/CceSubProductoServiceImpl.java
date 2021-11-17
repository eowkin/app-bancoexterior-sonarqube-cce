package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceSubProducto;
import com.bancoexterior.app.cce.repository.ICceSubProductoRepository;

@Service
public class CceSubProductoServiceImpl implements ICceSubProductoService{

	@Autowired
	private ICceSubProductoRepository repo;
	
	@Override
	public List<CceSubProducto> listaSubproductos() {
		return repo.listaSubproductos();
	}

	@Override
	public CceSubProducto buscarSubProductoPorCodProductoAndCodSubProducto(String codProducto, String codSubProducto) {
		return repo.buscarSubProductoPorCodProductoAndCodSubProducto(codProducto, codSubProducto);
	}

}
