package projetobancodedados.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Instanceof;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Turma;
import projetobancodedados.app.repository.TurmaRepository;

/**
 * Service Implementation for managing {@link Turma}.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class TurmaService {

    private final Logger log = LoggerFactory.getLogger(TurmaService.class);
    private final TurmaRepository turmaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save a turma.
     *
     * @param turma the entity to save.
     * @return the persisted entity.
     */
    public Turma save(Turma turma) {
        log.debug("Request to save Turma : {}", turma);
        return turmaRepository.save(turma);
    }

    /**
     * Update a turma.
     *
     * @param turma the entity to save.
     * @return the persisted entity.
     */
    public Turma update(Turma turma) {
        log.debug("Request to update Turma : {}", turma);
        return turmaRepository.save(turma);
    }

    /**
     * Partially update a turma.
     *
     * @param turma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Turma> partialUpdate(Turma turma) {
        log.debug("Request to partially update Turma : {}", turma);

        return turmaRepository
            .findById(turma.getId())
            .map(existingTurma -> {
                if (turma.getTurma() != null) {
                    existingTurma.setTurma(turma.getTurma());
                }
                if (turma.getPeriodo() != null) {
                    existingTurma.setPeriodo(turma.getPeriodo());
                }
                if (turma.getProfessor() != null) {
                    existingTurma.setProfessor(turma.getProfessor());
                }
                if (turma.getHorario() != null) {
                    existingTurma.setHorario(turma.getHorario());
                }
                if (turma.getVagasOcupadas() != null) {
                    existingTurma.setVagasOcupadas(turma.getVagasOcupadas());
                }
                if (turma.getTotalVagas() != null) {
                    existingTurma.setTotalVagas(turma.getTotalVagas());
                }
                if (turma.getLocal() != null) {
                    existingTurma.setLocal(turma.getLocal());
                }

                return existingTurma;
            })
            .map(turmaRepository::save);
    }

    /**
     * Get all the turmas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Turma> findAll(Pageable pageable, String filtroProfessor, String filtroDisciplina) {
        String nativeQuery;
        if (filtroProfessor != null && filtroDisciplina != null) {
            nativeQuery =
                "SELECT t.id AS id, t.turma AS turma, t.periodo AS periodo, " +
                "t.horario AS horario, t.vagas_ocupadas AS vagas_ocupadas, t.total_vagas AS total_vagas, " +
                "t.local AS local, p.id AS professor_id, p.nome AS p_nome, d.id AS disciplina_id, d.nome AS d_nome, " +
                "dep.id AS departamento_id, dep.nome AS dep_nome " +
                "FROM turma t " +
                "JOIN professor p ON t.professor_id = p.id " +
                "JOIN disciplina d ON t.disciplina_id = d.id " +
                "JOIN departamento dep ON t.departamento_id = dep.id " +
                "WHERE (p.nome = :filtroProfessor) AND " +
                "(d.nome = :filtroDisciplina) " +
                "LIMIT :pageSize OFFSET :offset";
        } else if (filtroProfessor != null && filtroDisciplina == null) {
            nativeQuery =
                "SELECT t.id AS id, t.turma AS turma, t.periodo AS periodo, " +
                "t.horario AS horario, t.vagas_ocupadas AS vagas_ocupadas, t.total_vagas AS total_vagas, " +
                "t.local AS local, p.id AS professor_id, p.nome AS p_nome, d.id AS disciplina_id, d.nome AS d_nome, " +
                "dep.id AS departamento_id, dep.nome AS dep_nome " +
                "FROM turma t " +
                "JOIN professor p ON t.professor_id = p.id " +
                "JOIN disciplina d ON t.disciplina_id = d.id " +
                "JOIN departamento dep ON t.departamento_id = dep.id " +
                "WHERE (p.nome = :filtroProfessor) " +
                "LIMIT :pageSize OFFSET :offset";
        } else if (filtroProfessor == null && filtroDisciplina != null) {
            nativeQuery =
                "SELECT t.id AS id, t.turma AS turma, t.periodo AS periodo, " +
                "t.horario AS horario, t.vagas_ocupadas AS vagas_ocupadas, t.total_vagas AS total_vagas, " +
                "t.local AS local, p.id AS professor_id, p.nome AS p_nome, d.id AS disciplina_id, d.nome AS d_nome, " +
                "dep.id AS departamento_id, dep.nome AS dep_nome " +
                "FROM turma t " +
                "JOIN professor p ON t.professor_id = p.id " +
                "JOIN disciplina d ON t.disciplina_id = d.id " +
                "JOIN departamento dep ON t.departamento_id = dep.id " +
                "WHERE (d.nome = :filtroDisciplina) " +
                "LIMIT :pageSize OFFSET :offset";
        } else {
            nativeQuery =
                "SELECT t.id AS id, t.turma AS turma, t.periodo AS periodo, " +
                "t.horario AS horario, t.vagas_ocupadas AS vagas_ocupadas, t.total_vagas AS total_vagas, " +
                "t.local AS local, p.id AS professor_id, p.nome AS p_nome, d.id AS disciplina_id, d.nome AS d_nome, " +
                "dep.id AS departamento_id, dep.nome AS dep_nome " +
                "FROM turma t " +
                "JOIN professor p ON t.professor_id = p.id " +
                "JOIN disciplina d ON t.disciplina_id = d.id " +
                "JOIN departamento dep ON t.departamento_id = dep.id " +
                "LIMIT :pageSize OFFSET :offset";
        }
        Query query = entityManager.createNativeQuery(nativeQuery, Turma.class);
        if (filtroProfessor != null) {
            query.setParameter("filtroProfessor", filtroProfessor);
        }
        if (filtroDisciplina != null) {
            query.setParameter("filtroDisciplina", filtroDisciplina);
        }
        query.setParameter("pageSize", pageable.getPageSize());
        query.setParameter("offset", pageable.getOffset());

        List<Turma> resultList = query.getResultList();

        String countQuery =
            "SELECT COUNT(*) " +
            "FROM turma t " +
            "JOIN professor p ON t.professor_id = p.id " +
            "JOIN disciplina d ON t.disciplina_id = d.id " +
            "JOIN departamento dep ON t.departamento_id = dep.id";

        Query outraQuery = entityManager.createNativeQuery(countQuery);
        long totalCount = (Long) outraQuery.getSingleResult();
        Page turmas = new PageImpl<>(resultList, pageable, totalCount);
        turmas.getContent().forEach(turma -> log.info(turma.toString()));
        return turmas;
    }

    /**
     * Get one turma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Turma> findOne(Long id) {
        String nativeQuery =
            "SELECT t.id AS id, t.turma AS turma, t.periodo AS periodo, " +
            "t.horario AS horario, t.vagas_ocupadas AS vagas_ocupadas, t.total_vagas AS total_vagas, " +
            "t.local AS local, p.id AS professor_id, p.nome AS p_nome, d.id AS disciplina_id, d.nome AS d_nome, " +
            "dep.id AS departamento_id, dep.nome AS dep_nome " +
            "FROM turma t " +
            "JOIN professor p ON t.professor_id = p.id " +
            "JOIN disciplina d ON t.disciplina_id = d.id " +
            "JOIN departamento dep ON t.departamento_id = dep.id " +
            "WHERE t.id = ?1";
        Query query = entityManager.createNativeQuery(nativeQuery, Turma.class);
        query.setParameter(1, id);
        List<Turma> resultList = query.getResultList();
        log.info(resultList.stream().findFirst().toString());

        return resultList.stream().findFirst();
    }

    /**
     * Delete the turma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Turma : {}", id);
        turmaRepository.deleteById(id);
    }
}
