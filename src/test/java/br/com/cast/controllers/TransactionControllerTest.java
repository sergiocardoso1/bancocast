package br.com.cast.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import br.com.cast.controlers.TransactionController;
import br.com.cast.model.Usuario;
import br.com.cast.repository.UsuarioRepository;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testProcessarTransacaoCredito() throws Exception {
        Integer usuarioRecebedorId = 1;
        BigDecimal valor = new BigDecimal("100.00");
        String tipoTransacao = "creditar";
        String nomeUsuario = "Usuario Teste";
        String tipoUsuario = "1";
        Integer adminId = 1;
        Integer usuarioOrigemId = null;

        Usuario usuarioRecebedor = new Usuario();
        usuarioRecebedor.setId(usuarioRecebedorId);
        usuarioRecebedor.setSaldo(new BigDecimal("50.00"));

        when(usuarioRepository.findByIdWithLock(usuarioRecebedorId)).thenReturn(Optional.of(usuarioRecebedor));

        String viewName = transactionController.processarTransacao(
            usuarioOrigemId, usuarioRecebedorId, tipoTransacao, valor, nomeUsuario, tipoUsuario, adminId, model);

        verify(usuarioRepository, times(1)).save(usuarioRecebedor);

        assert viewName.equals("home/index");
        verify(model).addAttribute("sucesso", "Transação realizada com sucesso!");
    }

    @Test
    public void testProcessarTransacaoDebito() throws Exception {
        Integer usuarioRecebedorId = 2;
        BigDecimal valor = new BigDecimal("50.00");
        String tipoTransacao = "debitar";
        String nomeUsuario = "Usuario Teste";
        String tipoUsuario = "2";
        Integer adminId = 1;
        Integer usuarioOrigemId = 1;

        // Mock do repositório
        Usuario usuarioRecebedor = new Usuario();
        usuarioRecebedor.setId(usuarioRecebedorId);
        usuarioRecebedor.setSaldo(new BigDecimal("100.00"));

        when(usuarioRepository.findByIdWithLock(usuarioRecebedorId)).thenReturn(Optional.of(usuarioRecebedor));

        String viewName = transactionController.processarTransacao(
            usuarioOrigemId, usuarioRecebedorId, tipoTransacao, valor, nomeUsuario, tipoUsuario, adminId, model);

        verify(usuarioRepository, times(1)).save(usuarioRecebedor);

        assert viewName.equals("home/index");
        verify(model).addAttribute("sucesso", "Transação realizada com sucesso!");
    }

    @Test
    public void testProcessarTransacaoTransferir() throws Exception {
        // Dados de entrada
        Integer usuarioRecebedorId = 2;
        BigDecimal valor = new BigDecimal("50.00");
        String tipoTransacao = "transferir";
        String nomeUsuario = "Usuario Teste";
        String tipoUsuario = "2";
        Integer adminId = 1;
        Integer usuarioOrigemId = 1;

        Usuario usuarioOrigem = new Usuario();
        usuarioOrigem.setId(usuarioOrigemId);
        usuarioOrigem.setSaldo(new BigDecimal("100.00"));

        Usuario usuarioRecebedor = new Usuario();
        usuarioRecebedor.setId(usuarioRecebedorId);
        usuarioRecebedor.setSaldo(new BigDecimal("50.00"));

        when(usuarioRepository.findByIdWithLock(usuarioOrigemId)).thenReturn(Optional.of(usuarioOrigem));
        when(usuarioRepository.findByIdWithLock(usuarioRecebedorId)).thenReturn(Optional.of(usuarioRecebedor));

        String viewName = transactionController.processarTransacao(
            usuarioOrigemId, usuarioRecebedorId, tipoTransacao, valor, nomeUsuario, tipoUsuario, adminId, model);

        verify(usuarioRepository, times(1)).save(usuarioOrigem);
        verify(usuarioRepository, times(1)).save(usuarioRecebedor);

        assert viewName.equals("home/index");
        verify(model).addAttribute("sucesso", "Transação realizada com sucesso!");
    }

    @Test
    public void testProcessarTransacaoValorInvalido() throws Exception {
        Integer usuarioRecebedorId = 2;
        BigDecimal valor = new BigDecimal("0.00");
        String tipoTransacao = "creditar";
        String nomeUsuario = "Usuario Teste";
        String tipoUsuario = "1";
        Integer adminId = 1;
        Integer usuarioOrigemId = 1;

        String viewName = transactionController.processarTransacao(
            usuarioOrigemId, usuarioRecebedorId, tipoTransacao, valor, nomeUsuario, tipoUsuario, adminId, model);

        assert viewName.equals("home/index");

        verify(model).addAttribute("erro", "Valor inválido para a transação");
    }
}
