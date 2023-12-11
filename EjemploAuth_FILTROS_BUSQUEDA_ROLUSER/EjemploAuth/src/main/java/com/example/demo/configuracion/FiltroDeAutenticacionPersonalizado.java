package com.example.demo.configuracion;

import java.io.IOException;

import org.hibernate.annotations.Filter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.demo.entidad.enumerado.RolUsuario;
import org.springframework.security.core.Authentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Filter(name = "FiltroDeAutenticacionPersonalizado")
public class FiltroDeAutenticacionPersonalizado extends BasicAuthenticationFilter {
	
    public FiltroDeAutenticacionPersonalizado(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        if (authentication != null) {
            boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString()));
            boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_ADMIN.toString()));
            String requestURI = request.getRequestURI();
            try {
                // Redirigir si es un usuario y está intentando acceder a /home
                if (isUser && (("/home".equals(requestURI)|| ("/login".equals(requestURI) )))) {
                    response.sendRedirect("/user/home"); // URL para usuarios
                    return;
                }else if(isAdmin && (("/home".equals(requestURI) || ("/login".equals(requestURI) )))) {
                	 response.sendRedirect("/admin/home"); // URL para usuarios
                     return;
                }
            } catch (IOException e) {
                // Loggear o manejar la excepción de redirección
                throw new ServletException("Error al redirigir", e);
            }
        }
        // Continue the filter chain
        chain.doFilter(request, response);
    }
}


