package com.example.demo.repositorio;

import com.example.demo.entidad.Comentario;
import com.example.demo.entidad.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
     boolean existsByUsername(String username);
    
 	@Query("SELECT username FROM Usuario")
 	List<String> obtenerNombresUsuario();

}
