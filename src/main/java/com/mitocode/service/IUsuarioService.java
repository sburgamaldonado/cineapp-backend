package com.mitocode.service;

import com.mitocode.model.Usuario;

public interface IUsuarioService{

	Usuario registrarTransaccional(Usuario us);
	
	public Usuario buscarPorUsuario(String username);

	public Usuario listarPorId(Integer id);

	public void cambiarClave(Usuario usu);
	
}
