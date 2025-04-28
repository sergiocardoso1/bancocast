package br.com.cast.controlers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import br.com.cast.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) throws UnsupportedEncodingException {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) 
            return "redirect:/login";

        String usuarioIdStr = CookieService.getCookie(request, "usuarioId");
        String nomeUsuario = CookieService.getCookie(request, "nomeUsuario");
        String tipoUsuario = CookieService.getCookie(request, "tipoUsuario");
        String adminIdStr = CookieService.getCookie(request, "adminId");

        if (isBlank(usuarioIdStr) || isBlank(nomeUsuario) || isBlank(tipoUsuario)) 
            return "redirect:/login";

        Integer usuarioId = Integer.valueOf(usuarioIdStr);
        Integer adminId = parseIntegerOrDefault(adminIdStr, 0);
        Integer admIdRetorno = (adminId > 0) ? adminId : usuarioId;

        ArrayList<Usuario> usuariosTransacao = repository.getUsersOfAdminLogin(admIdRetorno, usuarioId);

        model.addAttribute("usuarioId", usuarioIdStr);
        model.addAttribute("usuarios", usuariosTransacao);
        model.addAttribute("nome", nomeUsuario);
        model.addAttribute("tipoUsuario", tipoUsuario);
        model.addAttribute("adminId", adminId);
        model.addAttribute("saldo", repository.getSaldoUser(Integer.valueOf(usuarioIdStr)));

        return "home/index"; 
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Integer parseIntegerOrDefault(String value, Integer defaultValue) {
        try {
            if (isBlank(value) || "null".equalsIgnoreCase(value.trim())) 
                return defaultValue;
            
            return Integer.valueOf(value.trim());
        } 
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
