package com.example.demo.repositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entidad.Comentario;
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
	Page<Comentario> findAll(Pageable pageable);
	@Query("SELECT c FROM Comentario c WHERE c.usuario.username = :username")
	Page<Comentario> findByUsername(@Param("username") String username, Pageable pageable);

}
