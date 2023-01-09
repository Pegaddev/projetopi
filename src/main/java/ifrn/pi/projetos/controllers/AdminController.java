package ifrn.pi.projetos.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ifrn.pi.projetos.models.Role;
import ifrn.pi.projetos.models.Usuario;
import ifrn.pi.projetos.repositories.RoleRepository;
import ifrn.pi.projetos.repositories.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UsuarioRepository ur;
	@Autowired
	RoleRepository rr;

	@GetMapping("/usuarios")
	public ModelAndView listaUsuarios(String filter) {

		if (filter == null)
			filter = "";

		Iterable<Usuario> usuarios = ur.findAllByNomeContaining(filter);

		ModelAndView mv = new ModelAndView("admin/usuarios/lista");
		mv.addObject("usuarios", usuarios);

		return mv;
	}

	@GetMapping("/usuarios/{id}")
	public ModelAndView selecionaUsuario(@PathVariable("id") Long id) {
		Usuario usuario = ur.findById(id).get();
		Iterable<Role> papeis = rr.findAllByOrderByNomeAsc();
		
		ModelAndView mv = new ModelAndView("admin/usuarios/form");
		mv.addObject("usuario", usuario);
		mv.addObject("papeis", papeis);

		return mv;
	}
	
	@PostMapping("/usuarios/{id}")
	public String salvarPapeisUsuario(Usuario usuarioForm) {		
		
		Usuario usuario = ur.findById(usuarioForm.getId()).get();
		if(usuario == null) {
			return "redirect:/admin/usuarios/";
		}
		usuario.setRoles(usuarioForm.getRoles());
		
		ur.save(usuario);
		
		return "redirect:/admin/usuarios/" + usuarioForm.getId();
	}
	
	@GetMapping("/usuarios/{id}/remover")
	public String apagarUsuario(@PathVariable Long id, RedirectAttributes attributes) {
		
		Optional<Usuario> opt = ur.findById(id);
		
		if(!opt.isEmpty()) {
			Usuario usuario = opt.get();
			
			ur.delete(usuario);
			
			attributes.addFlashAttribute("mensagem", "Usuário removido com sucesso!");
		}
		
		return "redirect:/admin/usuarios/";
	}

}
