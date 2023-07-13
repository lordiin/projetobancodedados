package projetobancodedados.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.Avaliacao;

/**
 * Spring Data JPA repository for the Avaliacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    @Query(value = "select * from avaliacao where id = :id", nativeQuery = true)
    Avaliacao findOne(@Param("id") Long id);

    //    @Query(value = "select a.id, a.descricao, a.nota, a.turma_id, a.user_id, " +
    //        "t.id, t.turma, t.periodo, t.professor, t.horario, t.vagas_ocupadas, t.total_vagas, t.local, t." +
    //        "from avaliacao a join turma t on t.id = a.turma_id join jhi_user ju on ju.id = a.user_id", nativeQuery = true)
    //    Page<Avaliacao> findAll(Pageable pageable);

    @Query(value = "select * from avaliacao", nativeQuery = true)
    Page<Avaliacao> findAll(Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO avaliacao (descricao, nota, turma_id) VALUES (:descricao, :nota, :turmaId)", nativeQuery = true)
    void save(@Param("descricao") String descricao, @Param("nota") Integer nota, @Param("turmaId") Long turmaId);

    @Modifying
    @Query(value = "UPDATE avaliacao SET descricao = :descricao, nota = :nota, turma_id = :turmaId WHERE id = :id", nativeQuery = true)
    void update(@Param("id") Long id, @Param("descricao") String descricao, @Param("nota") Integer nota, @Param("turmaId") Long turmaId);

    @Modifying
    @Query(value = "DELETE from avaliacao where id = :id", nativeQuery = true)
    void delete(@Param("id") Long id);
}
