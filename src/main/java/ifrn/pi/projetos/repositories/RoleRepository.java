package ifrn.pi.projetos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ifrn.pi.projetos.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByNome(String nome);
	
	Iterable<Role> findAllByOrderByNomeAsc();

}
