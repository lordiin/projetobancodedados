package projetobancodedados.app.repository;

import jakarta.persistence.NamedQuery;
import java.lang.annotation.Native;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Turma;

/**
 * Spring Data JPA repository for the Turma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    //    @Query(value = "select * from turma", nativeQuery = true)
    //    Page<Turma> findAll(Pageable pageable);

}
