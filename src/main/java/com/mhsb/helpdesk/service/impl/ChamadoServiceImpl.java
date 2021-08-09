package com.mhsb.helpdesk.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mhsb.helpdesk.entity.Chamado;
import com.mhsb.helpdesk.entity.MudancaStatus;
import com.mhsb.helpdesk.repository.ChamadoRepository;
import com.mhsb.helpdesk.repository.MudancaStatusRepository;
import com.mhsb.helpdesk.service.ChamadoService;

@Service
public class ChamadoServiceImpl implements ChamadoService{

	@Autowired
	private ChamadoRepository chamadoRepository;
	
	@Autowired
	private MudancaStatusRepository mudancaStatusRepository;
	
	@Override
	public Chamado createOrUpdate(Chamado chamado) {
		return this.chamadoRepository.save(chamado);
	}

	@Override
	public Optional<Chamado> findById(String id) {
		return this.chamadoRepository.findById(id);
	}

	@Override
	public void delete(String id) {
		this.chamadoRepository.deleteById(id);
	}

	@Override
	public Page<Chamado> listChamado(int page, int count) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findAll(pages);
		return this.chamadoRepository.findAll(PageRequest.of(page, count));
	}

	@Override
	public MudancaStatus createMudancaStatus(MudancaStatus mudancaStatus) {
		return this.mudancaStatusRepository.save(mudancaStatus);
	}

	@Override
	public Iterable<MudancaStatus> listMudancaStatus(String chamadoId) {
		return this.mudancaStatusRepository.findByChamadoIdOrderByDataAlteraStatusDesc(chamadoId);
	}

	@Override
	public Page<Chamado> findByCurrentUser(int page, int count, String usuarioId) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findByUsuarioIdOrderByDataDesc(pages, usuarioId);
		return this.chamadoRepository.findByUsuarioIdOrderByDataDesc(PageRequest.of(page, count), usuarioId);
	}

	@Override
	public Page<Chamado> findByParameters(int page, int count, String titulo, String status, String prioridade) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeOrderByDataDesc(pages, titulo,  status, prioridade);
		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingOrderByDataDesc(PageRequest.of(page, count), titulo, status, prioridade);
	}

	@Override
	public Page<Chamado> findByParametersAndCurrentUser(int page, int count, String titulo, String status,
			String prioridade, String usuarioId) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDataDesc(pages, titulo, status, prioridade, usuarioId);
		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndUsuarioIdOrderByDataDesc(PageRequest.of(page, count), titulo, status, prioridade, usuarioId);
	}

	@Override
	public Page<Chamado> findByNumero(int page, int count, Integer numero) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDataDesc(pages, numero);
		return this.chamadoRepository.findByNumero(PageRequest.of(page, count), numero);
	}

	@Override
	public Iterable<Chamado> findAll() {
		return this.chamadoRepository.findAll();
	}

	@Override
	public Page<Chamado> findByParameterAndAssignedUser(int page, int count, String titulo, String status,
			String prioridade, String usuarioDesignadoId) {
//		Pageable pages = new PageRequest(page, count);
//		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDataDesc(pages, titulo, status, prioridade, usuarioDesignadoId);
		return this.chamadoRepository.findByTituloIgnoreCaseContainingAndStatusContainingAndPrioridadeContainingAndUsuarioDesignadoIdOrderByDataDesc(PageRequest.of(page, count), titulo, status, prioridade, usuarioDesignadoId);
	}

}
