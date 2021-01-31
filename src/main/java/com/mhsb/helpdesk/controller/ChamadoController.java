package com.mhsb.helpdesk.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mhsb.helpdesk.dto.ContadoresChamado;
import com.mhsb.helpdesk.entity.Chamado;
import com.mhsb.helpdesk.entity.MudancaStatus;
import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.enums.PerfilEnum;
import com.mhsb.helpdesk.enums.StatusEnum;
import com.mhsb.helpdesk.response.Response;
import com.mhsb.helpdesk.security.jwt.JwtTokenUtil;
import com.mhsb.helpdesk.service.ChamadoService;
import com.mhsb.helpdesk.service.UsuarioService;

public class ChamadoController {

	@Autowired
	private ChamadoService chamadoService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Chamado>> create (HttpServletRequest request, @RequestBody Chamado chamado, BindingResult result){
		Response<Chamado> response = new Response<Chamado>();
		try {
			validateCreateChamado(chamado, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			chamado.setStatus(StatusEnum.getStatus("NOVO"));
			chamado.setUsuario(usuarioFromRequest(request));
			chamado.setData(new Date());
			chamado.setNumero(gerarNumeroChamado());
			Chamado chamadoPersistido = (Chamado) chamadoService.createOrUpdate(chamado);
			response.setData(chamadoPersistido);
			
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateCreateChamado(Chamado chamado, BindingResult result) {
		if(chamado.getTitulo() == null) {
			result.addError(new ObjectError("Chamado", "Título não informado."));
		}
	}
	
	public Usuario usuarioFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return usuarioService.findByEmail(email);
	}
	
	private Integer gerarNumeroChamado() {
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Chamado>> update (HttpServletRequest request, @RequestBody Chamado chamado, BindingResult result){
		Response<Chamado> response = new Response<Chamado>();
		try {
			validateUpdateChamado(chamado, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			//FAZER TESTE SEM PESQUISAR POR ID, DA MESMA FORMA COMO ESTÁ NO USUARIO
			Optional<Chamado> optionalChamadoAtual = chamadoService.findById(chamado.getId());
			Chamado chamadoAtual = optionalChamadoAtual.get();
			chamado.setStatus(chamadoAtual.getStatus());
			chamado.setUsuario(chamadoAtual.getUsuario());
			chamado.setData(chamadoAtual.getData());
			chamado.setNumero(chamadoAtual.getNumero());
			if(chamadoAtual.getUsuarioDesignado() == null) {
				chamado.setUsuarioDesignado(chamadoAtual.getUsuarioDesignado());
			}
			Chamado chamadoPersistido = (Chamado) chamadoService.createOrUpdate(chamado);
			response.setData(chamadoPersistido);
			
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdateChamado(Chamado chamado, BindingResult result) {
		if(chamado.getId() == null) {
			result.addError(new ObjectError("Chamado", "Id não informado."));
		}
		if(chamado.getTitulo() == null) {
			result.addError(new ObjectError("Chamado", "Título não informado."));
		}
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CLIENTE','TECNICO')")
	public ResponseEntity<Response<Chamado>> findById (@PathVariable("id") String id){
		
		Response<Chamado> response = new Response<Chamado>();
		Optional<Chamado> optionalChamado = chamadoService.findById(id);
		Chamado chamado = optionalChamado.get();
		
		if(chamado == null) {
			response.getErros().add("Registro não encontrado, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		List<MudancaStatus> mudancas = new ArrayList<>();
		Iterable<MudancaStatus> mudancasAtuais = chamadoService.listMudancaStatus(chamado.getId());
		
		for (Iterator<MudancaStatus> iterator = mudancasAtuais.iterator(); iterator.hasNext();) {
			MudancaStatus mudancaStatus = (MudancaStatus) iterator.next();
			mudancaStatus.setChamado(null);
			mudancas.add(mudancaStatus);
		}
		
		chamado.setHistoricoStatus(mudancas);
		response.setData(chamado);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<String>> delete (@PathVariable("id") String id){
		Response<String> response = new Response<String>();
		Optional<Chamado> optionalChamado = chamadoService.findById(id);
		Chamado chamado = optionalChamado.get();
		
		if(chamado == null) {
			response.getErros().add("Registro não encontrado, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		chamadoService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CLIENTE','TECNICO')")
	public ResponseEntity<Response<Page<Chamado>>> findAll (HttpServletRequest request,
			@PathVariable("page") int page, @PathVariable("count") int count){
		
		Response<Page<Chamado>> response = new Response<Page<Chamado>>();
		Page<Chamado> chamados = null;
		Usuario usuarioRequest = usuarioFromRequest(request);
		
		if(usuarioRequest.getPerfil().equals(PerfilEnum.ROLE_TECNICO)) {
			chamados = chamadoService.listChamado(page, count);
		}else if(usuarioRequest.getPerfil().equals(PerfilEnum.ROLE_CLIENTE)) {
			chamados = chamadoService.findByCurrentUser(page, count, usuarioRequest.getId());
		}
		
		response.setData(chamados);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "{page}/{count}/{numero}/{titulo}/{status}/{prioridade}/{assinado}")
	@PreAuthorize("hasAnyRole('CLIENTE','TECNICO')")
	public ResponseEntity<Response<Page<Chamado>>> findByParams (HttpServletRequest request,
			@PathVariable("page") int page, @PathVariable("count") int count,
			@PathVariable("numero") Integer numero, @PathVariable("titulo") String titulo,
			@PathVariable("status") String status, @PathVariable("prioridade") String prioridade,
			@PathVariable("assinado") boolean assinado){
		
		titulo = titulo.equals("uninformed") ? "" : titulo;
		status = status.equals("uninformed") ? "" : status;
		prioridade = prioridade.equals("uninformed") ? "" : prioridade;
		
		Response<Page<Chamado>> response = new Response<Page<Chamado>>();
		Page<Chamado> chamados = null;
		
		if(numero > 0) {
			chamados = chamadoService.findByNumero(page, count, numero);
		}else {
			Usuario usuarioRequest = usuarioFromRequest(request);
			if(usuarioRequest.getPerfil().equals(PerfilEnum.ROLE_TECNICO)) {
				if(assinado) {
					chamados = chamadoService.findByParameterAndAssignedUser(page, count, titulo, status, prioridade, usuarioRequest.getId());
				}else {
					chamados = chamadoService.findByParameters(page, count, titulo, status, prioridade);
				}
			} else if(usuarioRequest.getPerfil().equals(PerfilEnum.ROLE_CLIENTE)) {
				chamados = chamadoService.findByParametersAndCurrentUser(page, count, titulo, status, prioridade, usuarioRequest.getId());
			}
		}
		
		response.setData(chamados);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping(value = "{id}/{status}")
	@PreAuthorize("hasAnyRole('CLIENTE','TECNICO')")
	public ResponseEntity<Response<Chamado>> changeStatus(@PathVariable("id") String id,
			@PathVariable("status") String status,
			HttpServletRequest request,
			@RequestBody Chamado chamado,
			BindingResult result){
		
		Response<Chamado> response = new Response<Chamado>();
		
		try {
			validateChangeStatus(id, status, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Chamado> chamadoOptional = chamadoService.findById(id);
			Chamado chamadoAtual = chamadoOptional.get();
			chamadoAtual.setStatus(StatusEnum.getStatus(status));
			
			if(status.equals("DESIGNADO")) {
				chamadoAtual.setUsuarioDesignado(usuarioFromRequest(request));
			}
			
			Chamado chamadoPersistido = (Chamado) chamadoService.createOrUpdate(chamadoAtual);
			MudancaStatus mudancaStatus = new MudancaStatus();
			mudancaStatus.setUsuarioAlteraStatus(usuarioFromRequest(request));
			mudancaStatus.setDataAlteraStatus(new Date());
			mudancaStatus.setStatus(StatusEnum.getStatus(status));
			mudancaStatus.setChamado(chamadoPersistido);
			chamadoService.createMudancaStatus(mudancaStatus);
			response.setData(chamadoPersistido);
			
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateChangeStatus(String id, String status, BindingResult result) {
		if(id == null || id.equals("")) {
			result.addError(new ObjectError("Chamado", "Id não informado."));
		}
		if(status == null || status.equals("")) {
			result.addError(new ObjectError("Chamado", "Título não informado."));
		}
	}
	
	@GetMapping(value = "/contadores")
	public ResponseEntity<Response<ContadoresChamado>> listarContadores(){
		Response<ContadoresChamado> response = new Response<ContadoresChamado>();
		ContadoresChamado contadores = new ContadoresChamado();
		
		Integer chamadosNovos = 0;
		Integer chamadosDesignados = 0;
		Integer chamadosResolvidos = 0;
		Integer chamadosAprovados = 0;
		Integer chamadosReprovados = 0;
		Integer chamadosFechados = 0;
		
		Iterable<Chamado> chamados = chamadoService.findAll();
		if(chamados != null) {
			for(Iterator<Chamado> iterator = chamados.iterator(); iterator.hasNext();) {
				Chamado chamado = (Chamado) iterator.next();
				if(chamado.getStatus().equals(StatusEnum.NOVO)) {
					chamadosNovos++;
				}
				if(chamado.getStatus().equals(StatusEnum.DESIGNADO)) {
					chamadosDesignados++;
				}
				if(chamado.getStatus().equals(StatusEnum.RESOLVIDO)) {
					chamadosResolvidos++;
				}
				if(chamado.getStatus().equals(StatusEnum.APROVADO)) {
					chamadosAprovados++;
				}
				if(chamado.getStatus().equals(StatusEnum.REPROVADO)) {
					chamadosReprovados++;
				}
				if(chamado.getStatus().equals(StatusEnum.FECHADO)) {
					chamadosFechados++;
				}
			}
		}
		
		contadores.setChamadosNovos(chamadosNovos);
		contadores.setChamadosDesignados(chamadosDesignados);
		contadores.setChamadosResolvidos(chamadosResolvidos);
		contadores.setChamadosAprovados(chamadosAprovados);
		contadores.setChamadosReprovados(chamadosReprovados);
		contadores.setChamadosFechados(chamadosFechados);
		
		response.setData(contadores);
		
		return ResponseEntity.ok(response);
	}
	
	
	
}
