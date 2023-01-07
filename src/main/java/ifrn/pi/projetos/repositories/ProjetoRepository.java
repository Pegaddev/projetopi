package ifrn.pi.projetos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ifrn.pi.projetos.models.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

}
