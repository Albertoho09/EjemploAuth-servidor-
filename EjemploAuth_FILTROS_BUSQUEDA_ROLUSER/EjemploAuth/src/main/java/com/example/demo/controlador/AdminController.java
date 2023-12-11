package com.example.demo.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.servicio.comentario.ComentarioServicioImpl;
import com.example.demo.servicio.usuario.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	ComentarioServicioImpl comentariosServicio;
	@Autowired
	UsuarioServicio usuarioServicio;
	
    @GetMapping("/home")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Model model, Authentication authentication) {
      	 String usernameAuth = authentication.getName(); // Obtener el nombre de usuario del objeto de autenticación
    	 model.addAttribute("username", usernameAuth); // Agregarlo al modelo
    	 
    	 model.addAttribute("usuarios", usuarioServicio.obtenerTodos()); // Agregarlo al modelo
    	 model.addAttribute("comentarios", comentariosServicio.obtenerTodos()); // Agregarlo al modelo

    	
        return "auth/admin/home"; // Muestra la página específica del administrador (admin.html)
    }
    
    @GetMapping("/borrarComentario")
    public String borrarComentario(Model model, @RequestParam Long id) {
    	comentariosServicio.eliminar(id);
		return "redirect:/home";
    	
    }
    
}
