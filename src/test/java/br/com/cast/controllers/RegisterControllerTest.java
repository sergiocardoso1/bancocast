package br.com.cast.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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

import br.com.cast.controlers.RegisterController;
import br.com.cast.enums.TipoUsuario;
import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterControllerTest {
	 @Mock
	    private UsuarioRepository usuarioRepository;

	    @Mock
	    private Model model;

	    @Mock
	    private HttpServletResponse response;

	    @InjectMocks
	    private RegisterController registerController;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void deveCadastrarUsuarioComSucesso() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario novoUsuario = new Usuario("Fulano", "fulano@email.com", "123456", TipoUsuario.ADMIN);
	        when(usuarioRepository.getEmail(novoUsuario.getEmail())).thenReturn(null);

	        // Act
	        String view = registerController.registrar(model, novoUsuario, "123456", response);

	        // Assert
	        verify(usuarioRepository, times(1)).save(any(Usuario.class));
	        assertEquals("login/index", view);
	    }

	    @Test
	    void deveRetornarErroQuandoEmailJaExiste() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario existente = new Usuario("Existente", "email@existente.com", "senha", TipoUsuario.ADMIN);
	        when(usuarioRepository.getEmail(existente.getEmail())).thenReturn(existente);

	        // Act
	        String view = registerController.registrar(model, existente, "senha", response);

	        // Assert
	        verify(usuarioRepository, never()).save(any(Usuario.class));
	        verify(model).addAttribute(eq("erro"), eq("Email inserido já está em uso!"));
	        assertEquals("create/register", view);
	    }

	    @Test
	    void deveRetornarErroQuandoSenhasNaoConferem() throws UnsupportedEncodingException {
	        // Arrange
	        Usuario novoUsuario = new Usuario("Novo", "novo@email.com", "123456", TipoUsuario.ADMIN);
	        when(usuarioRepository.getEmail(novoUsuario.getEmail())).thenReturn(null);

	        // Act
	        String view = registerController.registrar(model, novoUsuario, "senha_diferente", response);

	        // Assert
	        verify(usuarioRepository, never()).save(any(Usuario.class));
	        verify(model).addAttribute(eq("erro"), eq("Senha não confere!"));
	        assertEquals("create/register", view);
	    }
	}