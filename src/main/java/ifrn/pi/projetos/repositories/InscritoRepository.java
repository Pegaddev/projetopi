package ifrn.pi.projetos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.projetos.models.Inscrito;
import ifrn.pi.projetos.models.Projeto;

public interface InscritoRepository extends JpaRepository<Inscrito, Long>{

	List<Inscrito> findByProjeto(Projeto projeto);
	
}
