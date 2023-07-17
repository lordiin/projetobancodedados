package projetobancodedados.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Denuncia;

/**
 * Spring Data JPA repository for the Denuncia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    @Modifying
    @Query(value = "INSERT INTO denuncia (motivo, avaliacao_id) VALUES (:motivo, :avaliacaoId)", nativeQuery = true)
    void saveDenuncia(@Param("motivo") String motivo, @Param("avaliacaoId") Long avaliacaoId);
}
