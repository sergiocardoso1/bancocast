package br.com.cast.controlers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.cast.enums.TipoUsuario;
import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {
	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping("/registrar")
	public String index() {
		return "create/register";
	}
	
	@PostMapping("/registrar_conta")
	public String registrar(Model model, Usuario admParam, String repeticaoSenha, HttpServletResponse response) throws UnsupportedEncodingException {
		Usuario adm = repository.getEmail(admParam.getEmail());
		if(adm != null) {
			model.addAttribute("erro", "Email inserido já está em uso!");
			return "create/register";
		}
		if (!admParam.getSenha().equals(repeticaoSenha)) {
			model.addAttribute("erro", "Senha não confere!");
			return "create/register";
		}
		repository.save(new Usuario(admParam.getNome(), admParam.getEmail(), admParam.getSenha(), TipoUsuario.ADMIN));
		return "login/index";
	}
}
