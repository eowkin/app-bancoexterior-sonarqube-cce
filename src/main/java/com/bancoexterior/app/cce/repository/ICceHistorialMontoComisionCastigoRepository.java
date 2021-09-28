package com.bancoexterior.app.cce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceHistorialMontoComisionCastigo;


@Repository
public interface ICceHistorialMontoComisionCastigoRepository extends JpaRepository<CceHistorialMontoComisionCastigo, Integer>{

}
