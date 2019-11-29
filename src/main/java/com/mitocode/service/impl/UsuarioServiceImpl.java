package com.mitocode.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.model.Usuario;
import com.mitocode.repo.IUsuarioRepo;
import com.mitocode.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements UserDetailsService ,IUsuarioService {
	
	@Autowired
	private IUsuarioRepo userRepo;
	
	@Value("${mitocine.default-rol}")
	private Integer DEFAULT_ROL;
		
	@Transactional
	@Override
	public Usuario registrarTransaccional(Usuario usuario) {	
		Usuario u;
		try {
			u = userRepo.save(usuario);	
			userRepo.registrarRolPorDefecto(u.getIdUsuario(), DEFAULT_ROL);	
		}catch(Exception e) {
			throw e;
		}
		
		return u;
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userRepo.findOneByNombre(username); //from usuario where nombre := username
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
		}
		
		List<GrantedAuthority> roles = new ArrayList<>();
		
		user.getRoles().forEach( role -> {
			roles.add(new SimpleGrantedAuthority(role.getNombre()));
		});
		
		UserDetails userDetails = new User(user.getNombre(), user.getClave(), roles);
		
		return userDetails;
	}
	
	@Override
	public Usuario buscarPorUsuario(String username) {
		return userRepo.findOneByNombre(username);
	}

	@Override
	public Usuario listarPorId(Integer id) {
		Optional<Usuario> op = userRepo.findById(id);
		return op.isPresent() ? op.get() : new Usuario();
	}

	@Transactional
	@Override
	public void cambiarClave(Usuario usu) {
		try {
		userRepo.modificarClave(usu.getIdUsuario(), usu.getClave());
		}catch (Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	

}
