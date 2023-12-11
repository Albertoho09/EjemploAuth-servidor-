package com.example.demo.servicio.usuario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.UsuarioDTO;

import org.springframework.stereotype.Service;

import com.example.demo.entidad.Usuario;
import com.example.demo.repositorio.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServicio {

	@Autowired
	UsuarioRepository usuarioRepositorio;

	public Usuario guardar(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}
	
	public List<Usuario> obtenerTodos(){
		return usuarioRepositorio.findAll();
	}

	public Usuario obtenerPorId(Long id) {
		Optional<Usuario> resultado = usuarioRepositorio.findById(id);
		if (resultado.isPresent()) {
			return resultado.get();
		} else {
			// Manejar el caso en que el comentario no se encuentra.
			// Podrías lanzar una excepción o devolver null.
			return null;
		}
	}
	@Transactional
	private Usuario obtenerPorUsername(String username) {
		return usuarioRepositorio.findByUsername(username).get();

	}

	@Transactional
	public UsuarioDTO obtenerUsuarioDTO(String username) {
		Usuario usuario = obtenerPorUsername(username);
		return convertirAUsuarioDTO(usuario);
	}
	
	public List<String> obtenerNombre(){
		return usuarioRepositorio.obtenerNombresUsuario();
	}

	private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
		Set<String> roles = usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
		return new UsuarioDTO(usuario.getId(), usuario.getUsername(), roles);
	}

	public boolean existe(String username) {
		return usuarioRepositorio.existsByUsername(username);
	}
	
	

}