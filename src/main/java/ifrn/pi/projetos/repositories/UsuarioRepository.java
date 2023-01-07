package ifrn.pi.projetos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ifrn.pi.projetos.models.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
	Usuario findByMatricula(String matricula);
	
	Iterable<Usuario> findAllByNomeContaining(String nome);

}