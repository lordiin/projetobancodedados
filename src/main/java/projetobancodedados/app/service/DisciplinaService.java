package projetobancodedados.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Disciplina;
import projetobancodedados.app.repository.DisciplinaRepository;

/**
 * Service Implementation for managing {@link Disciplina}.
 */
@Service
@Transactional
public class DisciplinaService {

    private final Logger log = LoggerFactory.getLogger(DisciplinaService.class);

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Save a disciplina.
     *
     * @param disciplina the entity to save.
     * @return the persisted entity.
     */
    public Disciplina save(Disciplina disciplina) {
        log.debug("Request to save Disciplina : {}", disciplina);
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Update a disciplina.
     *
     * @param disciplina the entity to save.
     * @return the persisted entity.
     */
    public Disciplina update(Disciplina disciplina) {
        log.debug("Request to update Disciplina : {}", disciplina);
        return disciplinaRepository.save(disciplina);
    }

    /**
     * Partially update a disciplina.
     *
     * @param disciplina the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Disciplina> partialUpdate(Disciplina disciplina) {
        log.debug("Request to partially update Disciplina : {}", disciplina);

        return disciplinaRepository
            .findById(disciplina.getId())
            .map(existingDisciplina -> {
                if (disciplina.getCodigo() != null) {
                    existingDisciplina.setCodigo(disciplina.getCodigo());
                }
                if (disciplina.getNome() != null) {
                    existingDisciplina.setNome(disciplina.getNome());
                }

                return existingDisciplina;
            })
            .map(disciplinaRepository::save);
    }

    /**
     * Get all the disciplinas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Disciplina> findAll() {
        log.debug("Request to get all Disciplinas");
        return disciplinaRepository.findAll();
    }

    /**
     * Get one disciplina by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Disciplina> findOne(Long id) {
        log.debug("Request to get Disciplina : {}", id);
        return disciplinaRepository.findById(id);
    }

    /**
     * Delete the disciplina by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Disciplina : {}", id);
        disciplinaRepository.deleteById(id);
    }
}
