package ifrn.pi.projetos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ifrn.pi.projetos.models.Usuario;
import ifrn.pi.projetos.repositories.UsuarioRepository;

@Component
public class CustomDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByMatricula(username);
		
		if(usuario == null) {
			throw new UsernameNotFoundException("Usuario não encontrado.");
		}
		
		return usuario;
	}

}
