package com.mhsb.helpdesk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mhsb.helpdesk.entity.MudancaStatus;

public interface MudancaStatusRepository extends MongoRepository<MudancaStatus, String>{

	Iterable<MudancaStatus> findByChamadoIdOrderByDataAlteraStatusDesc(String chamadoId);
}
