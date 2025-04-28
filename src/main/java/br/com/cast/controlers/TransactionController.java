package br.com.cast.controlers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Controller
public class TransactionController {
    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/transacao/processar") 
    @Transactional(rollbackOn = Exception.class)  
    public String processarTransacao(
    		@RequestParam(value = "usuarioId", required = false) Integer usuarioOrigemId,
            @RequestParam("usuarioRecebedorId") Integer usuarioRecebedorId,
            @RequestParam("tipoTransacao") String tipoTransacao,
            @RequestParam(value = "valor") BigDecimal valor,
            @RequestParam("nomeUsuario") String nomeUsuario, 
            @RequestParam("tipoUsuario") String tipoUsuario, 
            @RequestParam("adminId") Integer adminId, 
            Model model
    ) {

        try {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) 
                throw new RuntimeException("Valor inválido para a transação");

            if(tipoTransacao.equals("transferir")) {
            	if(usuarioOrigemId != null && usuarioOrigemId > 0) {
            		if (usuarioOrigemId.equals(usuarioRecebedorId)) 
                        throw new RuntimeException("O usuário origem não pode ser o mesmo que o usuário recebedor.");
            	}
            	
            }

            
            Usuario usuarioOrigem = null;
            Usuario usuarioRecebedor = repository.findByIdWithLock(usuarioRecebedorId)
                    .orElseThrow(() -> new RuntimeException("Usuário de destino do processo não encontrado"));

            switch (tipoTransacao) {
                case "creditar":
       
                	usuarioRecebedor.setSaldo(usuarioRecebedor.getSaldo().add(valor));
                	repository.save(usuarioRecebedor);
                    break;
                case "debitar":
   
                    usuarioRecebedor.setSaldo(usuarioRecebedor.getSaldo().subtract(valor));
                    repository.save(usuarioRecebedor);
                    break;
                case "transferir":
                	if(tipoUsuario.equals("2")) {
                     	usuarioOrigem = repository.findByIdWithLock(usuarioOrigemId)
                         		.orElseThrow(() -> new RuntimeException("Usuário origem não encontrado"));
                            if (usuarioOrigem.getSaldo().compareTo(valor) < 0) 
                                throw new RuntimeException("Saldo insuficiente para transferir");
                	}
                		
                	if(usuarioOrigem != null) {
                		usuarioOrigem.setSaldo(usuarioOrigem.getSaldo().subtract(valor));
                		repository.save(usuarioOrigem);
                	}
                		
                    usuarioRecebedor.setSaldo(usuarioRecebedor.getSaldo().add(valor));
                    repository.save(usuarioRecebedor);
                    break;
                default:
                    throw new RuntimeException("Tipo de transação inválido");
            }

            model.addAttribute("sucesso", "Transação realizada com sucesso!");

        } 
        catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
        }
        model.addAttribute("usuarios", repository.getUsersOfAdminLogin(adminId == null || adminId == 0 ? usuarioOrigemId : adminId, usuarioOrigemId));
        model.addAttribute("nome", nomeUsuario);
        model.addAttribute("tipoUsuario", tipoUsuario);
        model.addAttribute("adminId", adminId);
        if(usuarioOrigemId != null && Integer.valueOf(usuarioOrigemId) > 0)
        	model.addAttribute("saldo", repository.getSaldoUser(Integer.valueOf(usuarioOrigemId)));
        model.addAttribute("usuarioId", usuarioOrigemId);
        return "home/index"; 
    }
}
