package com.example.demo.configuracion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entidad.enumerado.RolUsuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_ADMIN.toString()));
        boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString()));

        if (isAdmin) {
            return "/admin/home"; // URL para administradores
        } else if (isUser) {
            return "/user/home"; // URL para usuarios
        } else {
            throw new IllegalStateException();
        }
    }
}