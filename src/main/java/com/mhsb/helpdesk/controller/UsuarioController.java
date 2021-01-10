package com.mhsb.helpdesk.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.response.Response;
import com.mhsb.helpdesk.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> create(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result){
		
		Response<Usuario> response = new Response<Usuario>();
		try {
			validateCreateUser(usuario, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioPersistido = (Usuario) usuarioService.createOrUpdate(usuario);
			response.setData(usuarioPersistido);
			
		} catch (DuplicateKeyException dE) {
			response.getErros().add("Email já cadastrado.");
			return ResponseEntity.badRequest().body(response);
		} catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateCreateUser(Usuario usuario, BindingResult result) {
		if(usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email não informado."));
		}
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> update(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result){
		
		Response<Usuario> response = new Response<Usuario>();
		try {
			validateUpdateUser(usuario, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioPersistido = (Usuario) usuarioService.createOrUpdate(usuario);
			response.setData(usuarioPersistido);
			
		}  catch (DuplicateKeyException dE) {
			response.getErros().add("Email já cadastrado.");
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdateUser(Usuario usuario, BindingResult result) {
		if(usuario.getId() == null) {
			result.addError(new ObjectError("Usuario", "Id inexistente."));
		}
		if(usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email não informado."));
		}
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> findById(@PathVariable("id") String id){
		
		Response<Usuario> response = new Response<Usuario>();
		Optional<Usuario> optionalUsuario = usuarioService.findById(id);
		Usuario usuario = optionalUsuario.get();
		
		if(usuario == null) {
			response.getErros().add("Usuário não encontrado, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(usuario);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id){
	
		Response<String> response = new Response<String>();
		Optional<Usuario> optionalUsuario = usuarioService.findById(id);
		Usuario usuario = optionalUsuario.get();
		
		if(usuario == null) {
			response.getErros().add("Usuário não encontrado, id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		usuarioService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<Usuario>>> findAll(@PathVariable int page, @PathVariable int count){
		
		Response<Page<Usuario>> response = new Response<Page<Usuario>>();
		Page<Usuario> usuarios = usuarioService.findAll(page, count);
		response.setData(usuarios);
		
		return ResponseEntity.ok(response);
	}
	
}
