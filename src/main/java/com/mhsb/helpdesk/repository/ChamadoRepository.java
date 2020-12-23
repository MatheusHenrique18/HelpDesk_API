package com.mhsb.helpdesk.repository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mhsb.helpdesk.entity.Chamado;

public interface ChamadoRepository extends MongoRepository<Chamado, String>{

	Page<Chamado> findByUsuarioIdOrderByDataDesc(Pageable pages, String usuarioId);
	
	Page<Chamado> findByTituloIgnoreCaseContainingAndStatusAndPrioridadeOrderByDataDesc(Pageable pages, String titulo, String status, String prioridade);

	Page<Chamado> findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDataDesc(Pageable pages, String titulo, String status, String prioridade, String usuarioId);
	
	Page<Chamado> findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioDesignadoIdOrderByDataDesc(Pageable pages, String titulo, String status, String prioridade, String usuarioDesignadoId);

	Page<Chamado> findByNumero(Pageable pages, Integer numero);
	
}
