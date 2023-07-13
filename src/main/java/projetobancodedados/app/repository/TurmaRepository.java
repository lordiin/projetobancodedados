package projetobancodedados.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Turma;

/**
 * Spring Data JPA repository for the Turma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {}
