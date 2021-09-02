package com.bancoexterior.app.inicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.inicio.dto.GrupoDto;
import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.repository.IGrupoRepository;
import com.bancoexterior.app.util.Mapper;


@Service
public class GrupoServiceImpl implements IGrupoService{

	@Autowired
	private IGrupoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<Grupo> findAll() {
		return repo.findAll();
	}

	@Override
	public GrupoDto findById(int id) {
		
		Grupo grupo = repo.findById(id).orElse(null);
		if(grupo != null) {
			return mapper.map(grupo, GrupoDto.class);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Grupo findByNombre(String nombre) {
		return repo.findByNombreGrupo(nombre);
	}

	@Override
	public GrupoDto save(GrupoDto grupoDto) {
		Grupo grupo = mapper.map(grupoDto, Grupo.class);
		Grupo grupoSave = repo.save(grupo);
		return mapper.map(grupoSave, GrupoDto.class);
	}

	@Override
	public void updateNombreGrupo(String nombreGrupo, String codUsuario, int id) {
		repo.updateNombreGrupo(nombreGrupo, codUsuario,id);
		
	}

	@Override
	public void updateActivarDesactivarGrupo(boolean flagActivo, String codUsuario, int id) {
		repo.updateEditarFlagGrupo(flagActivo, codUsuario,id);
		
	}

	@Override
	public Grupo findByNombreAndFlagActivo(String nombre, boolean flagActivo) {
		return repo.findByNombreGrupoAndFlagActivo(nombre, flagActivo);
	}

}
