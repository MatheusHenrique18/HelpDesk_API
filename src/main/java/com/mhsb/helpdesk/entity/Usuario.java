package com.mhsb.helpdesk.entity;

import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mhsb.helpdesk.enums.PerfilEnum;

@Document
public class Usuario {

	@Id
	private String id;
	
	@Indexed(unique = true)
	@NotBlank(message = "Email é obrigatório.")
	@Email(message = "Email inválido.")
	private String email;
	
	@NotBlank(message = "Senha é obrigatória.")
	@Size(min = 6)
	
	private String senha;
	
	private PerfilEnum perfil;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public PerfilEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}
	
}
