package br.com.cast.controlers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cast.enums.TipoUsuario;
import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import br.com.cast.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {
	@Autowired
	private UsuarioRepository repository;

	@GetMapping("/usuarios")
	public String index(Model model, HttpServletRequest request) {
		try {
			String usuarioId = CookieService.getCookie(request, "usuarioId");
			String tipoUsuario = CookieService.getCookie(request, "tipoUsuario");

			model.addAttribute("tipoUsuario", tipoUsuario);
			if (usuarioId != null && !usuarioId.isBlank())
				model.addAttribute("usuarios", repository.getUsersOfAdmin(Integer.valueOf(usuarioId)));
		} 
		catch (UnsupportedEncodingException e) {

		}

		return "users/index";
	}

	@GetMapping("/usuarios/novo")
	public String novo(Model model, HttpServletRequest request) {
	    try {
	        String usuarioId = CookieService.getCookie(request, "usuarioId");
	        if (usuarioId != null && !usuarioId.isBlank()) 
	            model.addAttribute("adminId", Integer.valueOf(usuarioId));
	        
	    } catch (UnsupportedEncodingException e) {
	        
	    }
	    return "users/new";
	}

	@PostMapping("/usuarios/criar")
	public String criar(Usuario usuario) {
	    usuario.setTipoUsuario(TipoUsuario.CLIENTE);
	    repository.save(usuario);
	    return "redirect:/usuarios";
	}

	@GetMapping("/usuarios/{id}/excluir")
	public String excluir(@PathVariable Integer id, RedirectAttributes attr) {
		repository.deleteById(id);
		attr.addFlashAttribute("mensagem", "Usuáro excluído com sucesso.");
		return "redirect:/usuarios";
	}

	@GetMapping("/usuarios/{id}")
	public String buscar(@PathVariable Integer id, Model model) {
		Optional<Usuario> usuarioOptional = repository.findById(id);

		if (usuarioOptional.isPresent()) {
			Usuario usuario = usuarioOptional.get();
			model.addAttribute("usuario", usuario);
			model.addAttribute("id", usuario.getId());
			model.addAttribute("adminId", usuario.getAdminId());
			return "users/edit";
		} else
			return "redirect:/usuarios";

	}

	@PostMapping("/usuario/{id}/editar")
	public String editar(@PathVariable Integer id, Usuario usuario, RedirectAttributes attr) {
		if (repository.findById(id) != null) {
			usuario.setTipoUsuario(TipoUsuario.CLIENTE);
			repository.save(usuario);
			attr.addFlashAttribute("mensagem", "Usuário editado com sucesso.");
		}

		return "redirect:/usuarios";
	}

}
