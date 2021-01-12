package com.mhsb.helpdesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mhsb.helpdesk.entity.Usuario;
import com.mhsb.helpdesk.enums.PerfilEnum;
import com.mhsb.helpdesk.repository.UsuarioRepository;

@SpringBootApplication
public class HelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(usuarioRepository, passwordEncoder);
		};
	}
	
	private void initUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		Usuario admin = new Usuario();
		
		admin.setEmail("admin@helpdesk.com");
		admin.setSenha(passwordEncoder.encode("123456"));
		admin.setPerfil(PerfilEnum.ROLE_ADMIN);
		
		Usuario usuarioExistente = usuarioRepository.findByEmail("admin@helpdesk.com");
		if(usuarioExistente == null) {
			usuarioRepository.save(admin);
		}
	}

}
