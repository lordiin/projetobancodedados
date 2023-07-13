package projetobancodedados.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Disciplina;

/**
 * Spring Data JPA repository for the Disciplina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {}
