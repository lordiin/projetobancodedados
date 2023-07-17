package projetobancodedados.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Denuncia;
import projetobancodedados.app.repository.DenunciaRepository;

/**
 * Service Implementation for managing {@link Denuncia}.
 */
@Service
@Transactional
public class DenunciaService {

    private final Logger log = LoggerFactory.getLogger(DenunciaService.class);

    private final DenunciaRepository denunciaRepository;

    public DenunciaService(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    /**
     * Save a denuncia.
     *
     * @param denuncia the entity to save.
     * @return the persisted entity.
     */
    public Denuncia save(Denuncia denuncia) {
        log.debug("Request to save Denuncia : {}", denuncia);
        return denunciaRepository.save(denuncia);
    }

    /**
     * Update a denuncia.
     *
     * @param denuncia the entity to save.
     * @return the persisted entity.
     */
    public Denuncia update(Denuncia denuncia) {
        log.debug("Request to update Denuncia : {}", denuncia);
        return denunciaRepository.save(denuncia);
    }

    /**
     * Partially update a denuncia.
     *
     * @param denuncia the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Denuncia> partialUpdate(Denuncia denuncia) {
        log.debug("Request to partially update Denuncia : {}", denuncia);

        return denunciaRepository
            .findById(denuncia.getId())
            .map(existingDenuncia -> {
                if (denuncia.getMotivo() != null) {
                    existingDenuncia.setMotivo(denuncia.getMotivo());
                }

                return existingDenuncia;
            })
            .map(denunciaRepository::save);
    }

    /**
     * Get all the denuncias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Denuncia> findAll() {
        log.debug("Request to get all Denuncias");
        return denunciaRepository.findAll();
    }

    /**
     * Get one denuncia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Denuncia> findOne(Long id) {
        log.debug("Request to get Denuncia : {}", id);
        return denunciaRepository.findById(id);
    }

    /**
     * Delete the denuncia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Denuncia : {}", id);
        denunciaRepository.deleteById(id);
    }

    public Denuncia saveDenuncia(Denuncia denuncia, Long avaliacaoId) {
        log.debug("Request to save Denuncia : {}", denuncia);
        denunciaRepository.saveDenuncia(denuncia.getMotivo(), avaliacaoId);
        return denuncia;
    }
}
