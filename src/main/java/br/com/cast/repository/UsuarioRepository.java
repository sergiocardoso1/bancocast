package br.com.cast.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import br.com.cast.model.Usuario;
import jakarta.persistence.LockModeType;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	@Query(value = "SELECT * FROM usuarios WHERE email = :email AND senha = :senha", nativeQuery = true)
	public Usuario login(String email, String senha);
	
	@Query(value = "SELECT * FROM usuarios WHERE email = :email", nativeQuery = true)
	public Usuario getEmail(String email);
	
	@Query(value = "SELECT * FROM usuarios WHERE admin_id = :adminId", nativeQuery = true)
	public ArrayList<Usuario> getUsersOfAdmin(Integer adminId);
	
	@Query(value = "SELECT * FROM usuarios WHERE admin_id = :adminId and ID <> :id", nativeQuery = true)
	public ArrayList<Usuario> getUsersOfAdminLogin(Integer adminId, Integer id);
	
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    public Optional<Usuario> findByIdWithLock(Integer id);
    
    @Query("SELECT saldo FROM Usuario u WHERE u.id = :id")
    public BigDecimal getSaldoUser(Integer id);
}
