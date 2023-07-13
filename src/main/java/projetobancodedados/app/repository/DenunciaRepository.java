package projetobancodedados.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Denuncia;

/**
 * Spring Data JPA repository for the Denuncia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {}
