package ifrn.pi.projetos.controllers;

import java.util.ArrayList;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/editar")
	public String formEdit(Usuario usuario) {
		return "usuarios/editar-dados";
	}
	
	@GetMapping("/editarSenha")
	public String formEditSenha(Usuario usuario) {
		return "usuarios/editar-senha";
	}
	

	@PostMapping("/cadastro")
	public String salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes, Model model) {
		
		if(result.hasErrors()) {
			return form(usuario);
		}
		
		Usuario usr = ur.findByMatricula(usuario.getMatricula());
		
		if(usr!=null) {
			model.addAttribute("mensagemErro", "Essa matrícula já está cadastrada!");
			return form(usuario);
		}
		
		ArrayList<Role> roles = new ArrayList<Role>();
		Role role = rr.findByNome("ROLE_USUARIO"); 
		roles.add(role);

		usuario.setRoles(roles);

		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		ur.save(usuario);
		
		attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
		
		return "redirect:/cadastro";
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
	public String editar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			return formEdit(usuario);
		}
		
		ur.save(usuario);
		
		attributes.addFlashAttribute("mensagem", "Usuário editado com sucesso!");

		return "redirect:/editar";
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
	public String editarSenha(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			return formEditSenha(usuario);
		}
		
		usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
		
		ur.save(usuario);
		
		attributes.addFlashAttribute("mensagem", "Senha editada com sucesso!");

		return "redirect:/editarSenha";
	}
	
}
