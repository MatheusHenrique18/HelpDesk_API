package com.mhsb.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mhsb.helpdesk.entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{

	Usuario findByEmail (String email);
}
