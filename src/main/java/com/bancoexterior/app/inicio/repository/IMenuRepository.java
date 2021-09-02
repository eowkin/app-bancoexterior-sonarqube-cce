package com.bancoexterior.app.inicio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bancoexterior.app.inicio.model.Menu;

public interface IMenuRepository extends JpaRepository<Menu, Integer>{
	public static final String SELECTCCEMENUORDENADO ="SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo "
			+ "FROM monitor_financiero.menu "
			+ "order by nivel asc, orden asc ;";
	
	@Query(value = SELECTCCEMENUORDENADO, nativeQuery = true)
	public List<Menu> menuOrdenado();
	
	
	public static final String SELECTCMENUROLE ="with recursive menu_usuario as "
			+ "(SELECT c.id_menu, c.nombre, c.nivel, c.orden, c.id_menu_padre, c.direccion, c.flag_activo "
			+ "FROM monitor_financiero.menu c "
			+ "where c.id_menu in (?1) "
			+ "union all "
			+ "SELECT s.id_menu, s.nombre, s.nivel, s.orden, s.id_menu_padre, s.direccion, s.flag_activo "
			+ "FROM menu_usuario t inner join public.menu s on s.id_menu = t.id_menu_padre) "
			+ "SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo FROM menu_usuario where flag_activo = true "
			+ "group by id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo order by nivel asc, orden asc";
	
	
	public static final String SELECTCMENUROLEIN ="with recursive menu_usuario as "
			+ "(SELECT c.id_menu, c.nombre, c.nivel, c.orden, c.id_menu_padre, c.direccion, c.flag_activo "
			+ "FROM monitor_financiero.menu c "
			+ "where c.id_menu in (?1) "
			+ "union all "
			+ "SELECT s.id_menu, s.nombre, s.nivel, s.orden, s.id_menu_padre, s.direccion, s.flag_activo "
			+ "FROM menu_usuario t inner join monitor_financiero.menu s on s.id_menu = t.id_menu_padre) "
			+ "SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo FROM menu_usuario "
			+ "where flag_activo = true "
			+ "group by id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo "
			+ "order by nivel asc, orden asc";
	
	
	public static final String SELECTCMENUGRUPOINNOMBRES ="WITH recursive grupo_menu as(SELECT T02.id_menu "
			+ "FROM monitor_financiero.grupos T01 "
			+ "inner join monitor_financiero.grupos_menu T02 on (T01.id_grupo=T02.id_grupo and t01.flag_activo= true and T01.nombre_grupo in(?1)) "
			+ "inner join monitor_financiero.menu T03 on (t02.id_menu = t03.id_menu and t03.flag_activo = true)), menu_usuario_hijo_padre AS "
			+ "(SELECT T01.id_menu,  T01.nombre,  T01.nivel,  T01.orden,  T01.id_menu_padre,  T01.direccion,  T01.flag_activo FROM monitor_financiero.menu T01 "
			+ "WHERE T01.id_menu in(SELECT id_menu from grupo_menu) and T01.flag_activo = true\r\n "
			+ "UNION ALL "
			+ "SELECT T02.id_menu,  T02.nombre,  T02.nivel,  T02.orden,  T02.id_menu_padre,  T02.direccion,  T01.flag_activo "
			+ "FROM menu_usuario_hijo_padre T01 "
			+ "INNER JOIN monitor_financiero.menu T02 ON (T02.id_menu=T01.id_menu_padre )and t02.flag_activo= true), menu_usuario_padre_hijo AS "
			+ "(SELECT T01.id_menu,  T01.nombre,  T01.nivel,  T01.orden,  T01.id_menu_padre,  T01.direccion,  T01.flag_activo "
			+ "FROM monitor_financiero.menu T01 "
			+ "WHERE T01.id_menu in(SELECT id_menu from grupo_menu) and T01.flag_activo = true "
			+ "UNION all "
			+ "SELECT T02.id_menu,  T02.nombre,  T02.nivel,  T02.orden,  T02.id_menu_padre,  T02.direccion,  T01.flag_activo "
			+ "FROM monitor_financiero.menu T02 "
			+ "INNER JOIN menu_usuario_padre_hijo T01 ON (T01.id_menu=T02.id_menu_padre) and t02.flag_activo= true), menu_union as "
			+ "(SELECT id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo FROM menu_usuario_hijo_padre "
			+ "union "
			+ "SELECT id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo FROM menu_usuario_padre_hijo "
			+ "group by id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo) "
			+ "select id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo from menu_union "
			+ "where flag_activo = true "
			+ "order by nivel asc, orden asc";
	
	
	
	
	@Query(value = SELECTCMENUROLE, nativeQuery = true)
	public List<Menu> menuRole(int valores); 
	
	@Query(value = SELECTCMENUROLEIN, nativeQuery = true)
	public List<Menu> menuRoleIn(List<Integer>  valores);
	
	@Query(value = SELECTCMENUGRUPOINNOMBRES, nativeQuery = true)
	public List<Menu> menuNombreGrupoIn(List<String>  valores);
	
	
	
}
