package com.mhsb.helpdesk.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.repository.UsuarioRepository;
import com.mhsb.helpdesk.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Usuario findByEmail(String email) {
		return this.usuarioRepository.findByEmail(email);
	}

	@Override
	public Usuario createOrUpdate(Usuario usuario) {
		return this.usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findById(String id) {
		return this.usuarioRepository.findById(id);
	}

	@Override
	public void delete(String id) {
		this.usuarioRepository.deleteById(id);
	}

	@Override
	public Page<Usuario> findAll(int page, int count) {
//		Pageable pages = new PageRequest(page, count);
//		return this.usuarioRepository.findAll(pages);
		return this.usuarioRepository.findAll(PageRequest.of(page, count));

	}

}
