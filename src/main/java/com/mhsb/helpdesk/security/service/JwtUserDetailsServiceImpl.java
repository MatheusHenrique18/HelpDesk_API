package com.mhsb.helpdesk.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.security.jwt.JwtUserFactory;
import com.mhsb.helpdesk.service.UsuarioService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Usuario usuario = usuarioService.findByEmail(email);
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
		}else {
			return JwtUserFactory.create(usuario);
		}
	}

}
