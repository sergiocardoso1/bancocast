package br.com.cast.model;

import java.math.BigDecimal;
import java.util.Objects;

import br.com.cast.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "nome", length = 100, nullable = false)
	private String nome;

	@Column(name = "email", length = 100, nullable = false)
	private String email;

	@Column(name = "senha", length = 255, nullable = false)
	private String senha;
	
	@Column(name = "admin_id")
	private Integer adminId;
	
	@Column(name = "tipo_usuario", nullable = false)
	private Integer tipoUsuario;
	
	@Column(name = "saldo", nullable = false)
	private BigDecimal saldo = BigDecimal.ZERO;
	
	public Usuario() {
	
	}
	
	public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.tipoUsuario = tipoUsuario.getCod();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}


	public Integer getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario.getCod();
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
