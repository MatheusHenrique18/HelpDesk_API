package com.mhsb.helpdesk.dto;

import java.io.Serializable;

public class ContadoresChamado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer chamadosNovos;
	private Integer chamadosDesignados;
	private Integer chamadosResolvidos;
	private Integer chamadosAprovados;
	private Integer chamadosReprovados;
	private Integer chamadosFechados;
	
	public Integer getChamadosNovos() {
		return chamadosNovos;
	}
	public void setChamadosNovos(Integer chamadosNovos) {
		this.chamadosNovos = chamadosNovos;
	}
	public Integer getChamadosDesignados() {
		return chamadosDesignados;
	}
	public void setChamadosDesignados(Integer chamadosDesignados) {
		this.chamadosDesignados = chamadosDesignados;
	}
	public Integer getChamadosResolvidos() {
		return chamadosResolvidos;
	}
	public void setChamadosResolvidos(Integer chamadosResolvidos) {
		this.chamadosResolvidos = chamadosResolvidos;
	}
	public Integer getChamadosAprovados() {
		return chamadosAprovados;
	}
	public void setChamadosAprovados(Integer chamadosAprovados) {
		this.chamadosAprovados = chamadosAprovados;
	}
	public Integer getChamadosReprovados() {
		return chamadosReprovados;
	}
	public void setChamadosReprovados(Integer chamadosReprovados) {
		this.chamadosReprovados = chamadosReprovados;
	}
	public Integer getChamadosFechados() {
		return chamadosFechados;
	}
	public void setChamadosFechados(Integer chamadosFechados) {
		this.chamadosFechados = chamadosFechados;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
