package projetobancodedados.app.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Avaliacao;
import projetobancodedados.app.domain.NotaMediaTurmas;

/**
 * Spring Data JPA repository for the Avaliacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    @Query(value = "select * from avaliacao where id = :id", nativeQuery = true)
    Avaliacao findOne(@Param("id") Long id);

    @Query(value = "select * from avaliacao", nativeQuery = true)
    Page<Avaliacao> findAll(Pageable pageable);

    @Modifying
    @Query(
        value = "INSERT INTO avaliacao (descricao, nota, turma_id, user_id) VALUES (:descricao, :nota, :turmaId, :userId)",
        nativeQuery = true
    )
    void save(
        @Param("descricao") String descricao,
        @Param("nota") Integer nota,
        @Param("turmaId") Long turmaId,
        @Param("userId") Long userId
    );

    @Modifying
    @Query(value = "UPDATE avaliacao SET descricao = :descricao, nota = :nota, turma_id = :turmaId WHERE id = :id", nativeQuery = true)
    void update(@Param("id") Long id, @Param("descricao") String descricao, @Param("nota") Integer nota, @Param("turmaId") Long turmaId);

    @Modifying
    @Query(value = "DELETE from avaliacao where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);

    List<Avaliacao> findAllByTurmaId(Long turmaId);

    @Query(value = "SELECT * FROM obter_nota_media_turma()", nativeQuery = true)
    List<NotaMediaTurmas> getNotaMediaTurmas();
}
