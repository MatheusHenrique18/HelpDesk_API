package com.mhsb.helpdesk.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.security.jwt.JwtAuthenticationRequest;
import com.mhsb.helpdesk.security.jwt.JwtTokenUtil;
import com.mhsb.helpdesk.security.model.CurrentUser;
import com.mhsb.helpdesk.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping(value = "/api/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException{
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
						authenticationRequest.getSenha()
					)
			);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final Usuario usuario = usuarioService.findByEmail(authenticationRequest.getEmail());
		usuario.setSenha(null);
		return ResponseEntity.ok(new CurrentUser(token, usuario));
				
	}
	
	@PostMapping(value = "/api/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken( HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		final Usuario usuario = usuarioService.findByEmail(username);
		
		if(jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUser(refreshedToken, usuario));
		}else {
			return ResponseEntity.badRequest().body(null);
		}
				
	}
	
}
