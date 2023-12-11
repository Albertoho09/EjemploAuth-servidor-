package com.example.demo.servicio.comentario;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import com.example.demo.entidad.Comentario;

public interface ComentarioServicio {
	
	  /**
     * Recupera una página de comentarios.
     * 
     * @param pageable Parámetro que contiene la información de paginación y ordenación.
     * @return Una página de comentarios según los parámetros de paginación.
     */
    Page<Comentario> listarTodos(Pageable pageable);

    /**
     * Guarda un comentario en la base de datos.
     * 
     * @param comentario El comentario a guardar.
     * @return El comentario guardado con su ID generado.
     */
    Comentario guardar(Comentario comentario);

    /**
     * Elimina un comentario de la base de datos.
     * 
     * @param id El ID del comentario a eliminar.
     */
    void eliminar(Long id);

    /**
     * Busca un comentario por su ID.
     * 
     * @param id El ID del comentario a buscar.
     * @return El comentario encontrado o null si no se encuentra.
     */
    Comentario obtenerPorId(Long id);
//cuando solo necesites saber si hay más elementos después del conjunto actual y quieras evitar la consulta de conteo adicional. 
    
    /**
     * Recupera una porción (slice) de comentarios.
     * 
     * Este método es útil cuando se necesita paginación pero no se requiere
     * información sobre el número total de páginas o elementos. Ideal para
     * situaciones de carga más datos o scroll infinito.
     * 
     * @param pageable Parámetro que contiene la información de paginación.
     * @return Una porción de comentarios según los parámetros de paginación.
     */
    Slice<Comentario> listarTodosComoSlice(Pageable pageable);

	Page<Comentario> listarFiltroPorUsuario(Pageable pageable, String username);

	List<String> obtenerUsuariosConComentarios();
	


}
