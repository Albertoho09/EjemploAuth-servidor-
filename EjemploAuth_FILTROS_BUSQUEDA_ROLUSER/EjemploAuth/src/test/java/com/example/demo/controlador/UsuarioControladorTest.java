package com.example.demo.controlador;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entidad.Usuario;
import com.example.demo.entidad.enumerado.RolUsuario;
import com.example.demo.servicio.usuario.UsuarioServicio;
import com.example.demo.servicio.comentario.ComentarioServicio;

@WebMvcTest(UserController.class)
public class UsuarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioServicio usuarioServicio;

    @MockBean
    private ComentarioServicio comentarioServicio;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Usuario usuario = new Usuario();
        usuario.setUsername("user");
        usuario.setPassword(passwordEncoder.encode("user123")); // Codificar y asignar la contraseña

     
        UsuarioDTO usuarioDTOMock = new UsuarioDTO();
        usuarioDTOMock.setId(1L);
        usuarioDTOMock.setUsername(usuario.getUsername());
        Mockito.when(usuarioServicio.obtenerUsuarioDTO(any(String.class))).thenReturn(usuarioDTOMock);

        UserDetails usuarioPrueba = User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword()) // Asegúrate de que la contraseña no sea null
            .authorities(Collections.singletonList(new SimpleGrantedAuthority(RolUsuario.ROLE_USER.toString())))
            .build();

        UserDetailsService userDetailsServiceMock = Mockito.mock(UserDetailsService.class);
        Mockito.when(userDetailsServiceMock.loadUserByUsername(usuario.getUsername())).thenReturn(usuarioPrueba);
    }

    @Test
    void testInicioSesion() throws Exception {
        mockMvc.perform(post("/login")
                .with(csrf())
                .param("username", "user")
                .param("password", "user123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}