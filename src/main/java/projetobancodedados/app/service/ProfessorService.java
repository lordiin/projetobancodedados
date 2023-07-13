package projetobancodedados.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Professor;
import projetobancodedados.app.repository.ProfessorRepository;

/**
 * Service Implementation for managing {@link Professor}.
 */
@Service
@Transactional
public class ProfessorService {

    private final Logger log = LoggerFactory.getLogger(ProfessorService.class);

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /**
     * Save a professor.
     *
     * @param professor the entity to save.
     * @return the persisted entity.
     */
    public Professor save(Professor professor) {
        log.debug("Request to save Professor : {}", professor);
        return professorRepository.save(professor);
    }

    /**
     * Update a professor.
     *
     * @param professor the entity to save.
     * @return the persisted entity.
     */
    public Professor update(Professor professor) {
        log.debug("Request to update Professor : {}", professor);
        return professorRepository.save(professor);
    }

    /**
     * Partially update a professor.
     *
     * @param professor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Professor> partialUpdate(Professor professor) {
        log.debug("Request to partially update Professor : {}", professor);

        return professorRepository
            .findById(professor.getId())
            .map(existingProfessor -> {
                if (professor.getNome() != null) {
                    existingProfessor.setNome(professor.getNome());
                }
                if (professor.getEmail() != null) {
                    existingProfessor.setEmail(professor.getEmail());
                }

                return existingProfessor;
            })
            .map(professorRepository::save);
    }

    /**
     * Get all the professors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Professor> findAll() {
        log.debug("Request to get all Professors");
        return professorRepository.findAll();
    }

    /**
     * Get one professor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Professor> findOne(Long id) {
        log.debug("Request to get Professor : {}", id);
        return professorRepository.findById(id);
    }

    /**
     * Delete the professor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Professor : {}", id);
        professorRepository.deleteById(id);
    }
}
