package com.mhsb.helpdesk.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mhsb.helpdesk.entity.Chamado;
import com.mhsb.helpdesk.entity.MudancaStatus;

@Component
public interface ChamadoService {

	Chamado createOrUpdate(Chamado chamado);
	
	Optional<Chamado> findById(String id);
	
	void delete(String id);
	
	Page<Chamado> listChamado(int page, int count);
	
	MudancaStatus createMudancaStatus(MudancaStatus mudancaStatus);
	
	Iterable<MudancaStatus> listMudancaStatus(String chamadoId);
	
	Page<Chamado> findByCurrentUser(int page, int count, String usuarioId);
	
	Page<Chamado> findByParameters(int page, int count, String titulo, String status, String prioridade);
	
	Page<Chamado> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String prioridade, String usuarioId);
	
	Page<Chamado> findByNumero(int page, int count, Integer numero);
	
	Iterable<Chamado> findAll();
	
	Page<Chamado> findByParameterAndAssignedUser(int page, int count, String titulo, String status, String prioridade, String usuarioDesignadoId);
	
}
