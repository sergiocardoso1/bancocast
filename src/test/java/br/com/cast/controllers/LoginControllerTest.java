package br.com.cast.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import br.com.cast.controlers.LoginController;
import br.com.cast.enums.TipoUsuario;
import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginControllerTest {
	 @Mock
	    private UsuarioRepository usuarioRepository;

	    @Mock
	    private Model model;

	    @Mock
	    private HttpServletRequest request;

	    @Mock
	    private HttpServletResponse response;

	    @InjectMocks
	    private LoginController loginController;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void deveLogarComSucessoSemLembrar() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario usuario = new Usuario();
	        usuario.setId(1);
	        usuario.setNome("Admin");
	        usuario.setEmail("admin@email.com");
	        usuario.setSenha("1234");
	        usuario.setTipoUsuario(TipoUsuario.ADMIN);

	        when(usuarioRepository.login(usuario.getEmail(), usuario.getSenha())).thenReturn(usuario);

	        // Act
	        String view = loginController.logar(model, usuario, null, response);

	        // Assert
	        assertEquals("redirect:/", view);
	        verify(usuarioRepository, times(1)).login(usuario.getEmail(), usuario.getSenha());
	        // Aqui poderíamos mockar o CookieService também se ele fosse mais complexo
	    }

	    @Test
	    void deveLogarComSucessoComLembrar() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario usuario = new Usuario();
	        usuario.setId(1);
	        usuario.setNome("Admin");
	        usuario.setEmail("admin@email.com");
	        usuario.setSenha("1234");
	        usuario.setTipoUsuario(TipoUsuario.ADMIN);

	        when(usuarioRepository.login(usuario.getEmail(), usuario.getSenha())).thenReturn(usuario);

	        // Act
	        String view = loginController.logar(model, usuario, "on", response);

	        // Assert
	        assertEquals("redirect:/", view);
	        verify(usuarioRepository, times(1)).login(usuario.getEmail(), usuario.getSenha());
	    }

	    @Test
	    void deveFalharLoginComUsuarioInvalido() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario usuario = new Usuario();
	        usuario.setEmail("invalido@email.com");
	        usuario.setSenha("errada");

	        when(usuarioRepository.login(usuario.getEmail(), usuario.getSenha())).thenReturn(null);

	        // Act
	        String view = loginController.logar(model, usuario, null, response);

	        // Assert
	        assertEquals("redirect:/login", view);
	        verify(model).addAttribute(eq("erro"), eq("Usuário ou senha inválidos!"));
	    }

	    @Test
	    void deveRealizarLogout() {
	        // Arrange
	        Cookie cookie1 = new Cookie("usuarioId", "1");
	        Cookie cookie2 = new Cookie("nomeUsuario", "Admin");
	        Cookie[] cookies = new Cookie[]{cookie1, cookie2};

	        when(request.getCookies()).thenReturn(cookies);

	        // Act
	        String view = loginController.sair(request, response);

	        // Assert
	        assertEquals("redirect:/login", view);
	        verify(response, times(2)).addCookie(any(Cookie.class));
	        assertEquals(0, cookie1.getMaxAge());
	        assertEquals(0, cookie2.getMaxAge());
	    }
	}