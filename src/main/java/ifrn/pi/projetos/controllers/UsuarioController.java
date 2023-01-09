package ifrn.pi.projetos.controllers;

import java.util.ArrayList;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
	public String form(Usuario usuario) {
		return "usuarios/form";
	}
	
	public String formEdit(Usuario usuario) {
		return "usuarios/editar-dados";
	}
	
	public String formEditSenha(Usuario usuario) {
		return "usuarios/editar-senha";
	}
	

	@PostMapping("/cadastro")
	public String salvar(@Valid Usuario usuario, BindingResult result) {
		
		if(result.hasErrors()) {
			return form(usuario);
		}
		
		ArrayList<Role> roles = new ArrayList<Role>();
		Role role = rr.findByNome("ROLE_USUARIO"); 
		roles.add(role);

		usuario.setRoles(roles);

		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		ur.save(usuario);

		return "redirect:/login";
	}
	
	
	@GetMapping("/usuarioDados/{id}")
	public ModelAndView selecionarUsuario(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Usuario> opt = ur.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/");
			return md;
		}
		
		Usuario usuario = opt.get();
		md.setViewName("usuarios/editar-dados");
		md.addObject("usuario", usuario);
		
		return md;
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Usuario usuario, BindingResult result) {
		
		if(result.hasErrors()) {
			return formEdit(usuario);
		}
		
		ur.save(usuario);

		return "redirect:/";
	}
	
	@GetMapping("/usuarioSenha/{id}")
	public ModelAndView selecionarSenhaUsuario(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Usuario> opt = ur.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/");
			return md;
		}
		
		Usuario usuario = opt.get();
		md.setViewName("usuarios/editar-senha");
		md.addObject("usuario", usuario);
		
		return md;
	}
	
	@PostMapping("/editarSenha")
	public String editarSenha(@Valid Usuario usuario, BindingResult result) {
		
		if(result.hasErrors()) {
			return formEditSenha(usuario);
		}
		
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		ur.save(usuario);

		return "redirect:/";
	}
	
}
