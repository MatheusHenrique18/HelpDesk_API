package com.mhsb.helpdesk.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mhsb.helpdesk.enums.StatusEnum;

@Document
public class MudancaStatus {

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private Chamado chamado;
	
	@DBRef(lazy = true)
	private Usuario usuarioAlteraStatus;
	
	private Date dataAlteraStatus;
	
	private StatusEnum status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Chamado getChamado() {
		return chamado;
	}

	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}

	public Usuario getUsuarioAlteraStatus() {
		return usuarioAlteraStatus;
	}

	public void setUsuarioAlteraStatus(Usuario usuarioAlteraStatus) {
		this.usuarioAlteraStatus = usuarioAlteraStatus;
	}

	public Date getDataAlteraStatus() {
		return dataAlteraStatus;
	}

	public void setDataAlteraStatus(Date dataAlteraStatus) {
		this.dataAlteraStatus = dataAlteraStatus;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
}
