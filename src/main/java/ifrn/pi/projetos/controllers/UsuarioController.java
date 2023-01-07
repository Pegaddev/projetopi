package ifrn.pi.projetos.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ifrn.pi.projetos.models.Role;
import ifrn.pi.projetos.models.Usuario;
import ifrn.pi.projetos.repositories.RoleRepository;
import ifrn.pi.projetos.repositories.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository ur;
	@Autowired
	private RoleRepository rr;

	@GetMapping("/cadastro")
	public String form() {
		return "usuarios/form";
	}

	@PostMapping("/cadastro")
	public String salvar(Usuario usuario) {
		
		ArrayList<Role> roles = new ArrayList<Role>();
		Role role = rr.findByNome("ROLE_USUARIO"); 
		roles.add(role);

		usuario.setRoles(roles);

		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

		ur.save(usuario);

		return "redirect:/login";
	}

}
