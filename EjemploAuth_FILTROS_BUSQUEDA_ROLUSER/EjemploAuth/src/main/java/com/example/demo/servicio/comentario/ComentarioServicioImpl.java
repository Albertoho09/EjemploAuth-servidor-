package com.example.demo.servicio.comentario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entidad.Comentario;
import com.example.demo.repositorio.ComentarioRepository;

@Service
public class ComentarioServicioImpl implements ComentarioServicio {
	
	@Autowired
    private ComentarioRepository comentarioRepository;
	
    @Override
    public Comentario guardar(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public void eliminar(Long id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public Comentario obtenerPorId(Long id) {
        Optional<Comentario> resultado = comentarioRepository.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            // Manejar el caso en que el comentario no se encuentra.
            // Podrías lanzar una excepción o devolver null.
            return null;
        }
    }
    
    public List<Comentario> obtenerTodos(){
    	return comentarioRepository.findAll();
    }
    
    @Override
    public Slice<Comentario> listarTodosComoSlice(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "fechaCreacion"));
        return comentarioRepository.findAll(pageable);
    }

    @Override
    public Page<Comentario> listarTodos(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "fechaCreacion"));
        return comentarioRepository.findAll(pageable);
    }

    @Override
    public Page<Comentario> listarFiltroPorUsuario(Pageable pageable, String username) {
    	return comentarioRepository.findByUsername(username, pageable);
    }

	public List<String> obtenerUsuariosConComentarios() {
		return comentarioRepository.findAll().stream()
		        .map(comentario -> comentario.getUsuario().getUsername())
		        .distinct()
		        .collect(Collectors.toList());
	}

}