package com.example.demo.controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entidad.Comentario;
import com.example.demo.entidad.Usuario;
import com.example.demo.servicio.comentario.ComentarioServicio;
import com.example.demo.servicio.usuario.UsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	ComentarioServicio comentariosServicio;
	@Autowired
	UsuarioServicio usuarioServicio;

	private final String HOME = "/auth/user/home";

	@GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public String user(Model model, Authentication authentication, 
    		@RequestParam(value = "page", defaultValue = "0") int page, /* PAGINACIÓN */
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "contenido", required = false, defaultValue = "") String contenido,/* PAGINACIÓN */
            @RequestParam(value = "opcion", required = false) String username, /*FILTRAR POR USUARIO*/
            HttpServletRequest request ) /* Para obtener URI -> request.getRequestURI()*/
    {
    	
   	 String usernameAuth = authentication.getName(); // Obtener el nombre de usuario del objeto de autenticación
	 model.addAttribute("username", usernameAuth); // Agregarlo al modelo
	 
	 UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuarioDTO(usernameAuth);
	 model.addAttribute("usuarioDTO", usuarioDTO);
    
	 //Para el <Select>Usuario</Select>
	 model.addAttribute("usuarios", comentariosServicio.obtenerUsuariosConComentarios());

    //Para crear <form>Comentarios </form>
    model.addAttribute("comentario", new Comentario());
    
    /**
     * #######################
     * ##    PÁGINACIÓN     ##
     * #######################	 
     */
    //Obtener { Comentarios }
    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Comentario> comentarios = null;

    if (username != null && !username.isEmpty() && usuarioServicio.existe(username) ) {
    	comentarios = comentariosServicio.listarFiltroPorUsuario(pageRequest, username);
        System.out.println("#COMENTARIOS DE USUARIO '" + username + "': " + comentarios);
        model.addAttribute("opcionPorDefecto", username);

    }else {
        comentarios = comentariosServicio.listarTodos(pageRequest);
        System.out.println("#COMENTARIOS TOTALES: " + comentarios);
    }

        int currentPage = page;
        int startPage = Math.max(0, currentPage - 2);
        int endPage = currentPage + 2; // Asume un rango fijo de páginas alrededor de la página actual

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("comentarios", comentarios.getContent());
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("hasNext", comentarios.hasNext());
        model.addAttribute("hasPrevious", comentarios.hasPrevious());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("contenido", contenido);
        model.addAttribute("nombreselector", usuarioServicio.obtenerNombre());

        System.out.println(request.getRequestURI());

        return HOME; // Muestra la página específica del usuario (user.html)
    }
    /**
     * #############################
     * ##<FORM> COMENTARIO</FORM> ##
     * #############################	 
     */
@GetMapping("/agregarComentario")
   public String mostrarFormularioComentario(Model model) {
	   
	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	 // Obtener UsuarioDTO en lugar de la entidad Usuario completa
        UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuarioDTO(username);
	    
	   System.out.println("############# id:" +usuarioDTO.getId());
	 
	// Crear una nueva instancia de Comentario y añadirla al modelo
       Comentario comentario = new Comentario();
       comentario.setFechaCreacion(LocalDateTime.now());
       model.addAttribute("comentario", comentario);
       model.addAttribute("usuarioDTO", usuarioDTO); // Añadir el UsuarioDTO al modelo si es necesario

	   return "/auth/user/formCrearComentario";
   }
   @PostMapping("/agregarComentario")
   public String agregarComentario(@Valid @ModelAttribute("comentario") Comentario comentario, 
                                   BindingResult result, 
                                   @RequestParam("usuarioId") Long usuarioId, 
                                   
                                   Model model) {
       if (result.hasErrors()) {
           return "/auth/user/formCrearComentario";
       }
       // Busca el usuario por el ID capturado
       Usuario usuario = usuarioServicio.obtenerPorId(usuarioId);
       // Asocia el usuario al comentario
       comentario.setUsuario(usuario);
       
       System.out.println("## contenido ## " + comentario.toString());
       
       comentariosServicio.guardar(comentario);
       return "redirect:/user/home";
   }
    
  
	
}