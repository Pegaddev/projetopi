package ifrn.pi.projetos.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ifrn.pi.projetos.models.Inscrito;
import ifrn.pi.projetos.models.Projeto;
import ifrn.pi.projetos.repositories.InscritoRepository;
import ifrn.pi.projetos.repositories.ProjetoRepository;

@Controller
@RequestMapping("/projetos")
public class ProjetosController {
	
	@Autowired
	private ProjetoRepository pr;
	@Autowired
	private InscritoRepository ir;
	
	@GetMapping("/form")
	public String form(Projeto projeto) {
		return "Projetos/formProjeto";
	}
	
	@PostMapping
	public String adicionar(@Valid Projeto projeto, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			return form(projeto);
		}
		
		System.out.println(projeto);
		pr.save(projeto);
		attributes.addFlashAttribute("mensagem", "Projeto salvo com sucesso!");
		
		return "redirect:/projetos";
	}
	
	@GetMapping
	public ModelAndView listar() {
		List<Projeto> projetos = pr.findAll();
		ModelAndView mv = new ModelAndView("projetos/lista");
		mv.addObject("projetos", projetos);
		return mv;
	}
	
	@GetMapping("/{id}")
	public ModelAndView detalhar (@PathVariable Long id, Inscrito inscrito) {
		ModelAndView md = new ModelAndView();
		
		Optional <Projeto> opt = pr.findById(id);
		
		if(opt.isEmpty()) {
			md.setViewName("redirect:/projetos");
			return md;
		}
		
		md.setViewName("projetos/detalhes");
		Projeto projeto = opt.get();
		md.addObject("projeto", projeto);
		
		List<Inscrito> inscritos = ir.findByProjeto(projeto);
		md.addObject("inscritos", inscritos);
		
		return md;
	}
	
	@PostMapping("/{idProjeto}")
	public ModelAndView salvarInscrito(@PathVariable Long idProjeto, @Valid Inscrito inscrito, BindingResult result, RedirectAttributes attributes) {
		ModelAndView md = new ModelAndView();
		
		if(result.hasErrors()) {
			return detalhar(idProjeto, inscrito);
		}
		
		System.out.println("Id do projeto:" + idProjeto);
		System.out.println(inscrito);
		
		Optional<Projeto> opt = pr.findById(idProjeto);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/projetos");
			return md;
		}
		
		Projeto projeto = opt.get();
		inscrito.setProjeto(projeto);
		
		ir.save(inscrito);
		
		attributes.addFlashAttribute("mensagem", "Inscrito salvo com sucesso!");
		
		md.setViewName("redirect:/projetos/{idProjeto}");
		return md;
	}
	
	@GetMapping("{id}/selecionar")
	public ModelAndView selecionarProjeto(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Projeto> opt = pr.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/projetos");
			return md;
		}
		
		Projeto projeto = opt.get();
		md.setViewName("projetos/formProjeto");
		md.addObject("projeto", projeto);
		
		return md;
	}
	
	@GetMapping("{idProjeto}/inscritos/{idInscrito}/selecionar")
	public ModelAndView selecionarInscrito(@PathVariable Long idProjeto, @PathVariable Long idInscrito) {
		ModelAndView md = new ModelAndView();
		
		Optional<Projeto> optProjeto = pr.findById(idProjeto);
		Optional<Inscrito> optInscrito = ir.findById(idInscrito);
		
		if(optProjeto.isEmpty() || optInscrito.isEmpty()) {
			md.setViewName("redirect:/projetos");
			return md;
		}
		
		Projeto projeto = optProjeto.get();
		Inscrito inscrito = optInscrito.get();
		
		if(projeto.getId() != inscrito.getProjeto().getId()) {
			md.setViewName("redirect:/projetos");
			return md;
		}
		
		md.setViewName("projetos/detalhes");
		md.addObject("inscrito", inscrito);
		md.addObject("projeto", projeto);
		md.addObject("inscritos", ir.findByProjeto(projeto));
		
		return md;
	}
	
	@GetMapping("/{id}/remover")
	public String apagarProjeto(@PathVariable Long id, RedirectAttributes attributes) {
		
		Optional<Projeto> opt = pr.findById(id);
		
		if(!opt.isEmpty()) {
			Projeto projeto = opt.get();
			
			List<Inscrito> inscritos = ir.findByProjeto(projeto);
			
			ir.deleteAll(inscritos);
			
			pr.delete(projeto);
			
			attributes.addFlashAttribute("mensagem", "Projeto removido com sucesso!");
		}
		
		return "redirect:/projetos";
	}
	
	@GetMapping("{idProjeto}/inscritos/{idInscrito}/remover")
	public String apagarInscrito(@PathVariable Long idInscrito, @PathVariable Long idProjeto, RedirectAttributes attributes) {
		
		Optional<Inscrito> opt = ir.findById(idInscrito);
		
		if(!opt.isEmpty()) {
			Inscrito inscrito = opt.get();
			ir.delete(inscrito);
			attributes.addFlashAttribute("mensagem", "Inscrito removido com sucesso!");
		}
		
		return "redirect:/projetos/{idProjeto}";
	}
}