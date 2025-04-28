package br.com.cast.repository;

import java.math.BigDecimal;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.cast.enums.TipoUsuario;
import br.com.cast.model.Usuario;

@Service
@Component
public class DBServiceImpl {

	@Autowired
	private UsuarioRepository usuarioRepository;


	public void instantiateTestDatabase() throws ParseException {

		Usuario adm = new Usuario("Sérgio", "sergiocardosodeveloper@gmail.com", "123", TipoUsuario.ADMIN);
		Usuario cliente = new Usuario("Anete", "anetevieira1@gmail.com", "123", TipoUsuario.CLIENTE);

		Usuario cliente2 = new Usuario("Rita", "anetevieira2@gmail.com", "123", TipoUsuario.CLIENTE);
		Usuario cliente3 = new Usuario("Carlinhos", "anetevieira3@gmail.com", "123", TipoUsuario.CLIENTE);
		cliente.setSaldo(BigDecimal.valueOf(100));
		cliente.setAdminId(1);
		cliente2.setAdminId(1);
		cliente3.setAdminId(1);
		usuarioRepository.save(adm);
		usuarioRepository.save(cliente);
		usuarioRepository.save(cliente2);
		usuarioRepository.save(cliente3);
	}

}
