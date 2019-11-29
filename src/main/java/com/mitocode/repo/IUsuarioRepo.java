package com.mitocode.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Usuario;

public interface IUsuarioRepo extends JpaRepository<Usuario, Integer> {
				
	Usuario findOneByNombre(String username);
		
	@Modifying
	@Query(value = "INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (:idUsuario, :idRol)", nativeQuery = true)
	void registrarRolPorDefecto(@Param("idUsuario") Integer idUsuario, @Param("idRol") Integer idRol);
	
	@Modifying
	@Query(value = "UPDATE usuario SET clave = :clave , estado = true where id_usuario = :idUsuario", nativeQuery = true)
	void modificarClave(@Param("idUsuario") Integer idUsuario, @Param("clave") String clave);
	
}