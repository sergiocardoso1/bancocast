package br.com.cast.controlers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import br.com.cast.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LoginController {
	
	@Autowired
	private UsuarioRepository repository;
	
	@GetMapping("/login")
	public String index() {
		return "login/index";
	}

	@PostMapping("/logar")
	public String logar(Model model, Usuario admParam, String lembrar, HttpServletResponse response) throws UnsupportedEncodingException {
		Usuario adm = repository.login(admParam.getEmail(), admParam.getSenha());
		if(adm != null) {
			int tempologado = (60*60);
			if(lembrar != null) {
				tempologado = (60*60*24);
			}
			CookieService.setCookie(response, "usuarioId", String.valueOf(adm.getId()) ,tempologado);
			CookieService.setCookie(response, "nomeUsuario", String.valueOf(adm.getNome()) ,tempologado);
			CookieService.setCookie(response, "tipoUsuario", String.valueOf(adm.getTipoUsuario()) ,tempologado);
			CookieService.setCookie(response, "adminId", String.valueOf(adm.getAdminId()) ,tempologado);
			
			return "redirect:/";
		}
		model.addAttribute("erro", "Usuário ou senha inválidos!");
		 return "redirect:/login";
	}
	
	@GetMapping("/sair")
	public String sair(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            cookie.setValue("");
	            cookie.setPath("/"); 
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
	        }
	    }
	    return "redirect:/login";
	}


}
