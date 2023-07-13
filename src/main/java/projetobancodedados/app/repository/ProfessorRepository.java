package projetobancodedados.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Professor;

/**
 * Spring Data JPA repository for the Professor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {}
