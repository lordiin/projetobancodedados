package projetobancodedados.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Departamento;
import projetobancodedados.app.repository.DepartamentoRepository;

/**
 * Service Implementation for managing {@link Departamento}.
 */
@Service
@Transactional
public class DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoService.class);

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * Save a departamento.
     *
     * @param departamento the entity to save.
     * @return the persisted entity.
     */
    public Departamento save(Departamento departamento) {
        log.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    /**
     * Update a departamento.
     *
     * @param departamento the entity to save.
     * @return the persisted entity.
     */
    public Departamento update(Departamento departamento) {
        log.debug("Request to update Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    /**
     * Partially update a departamento.
     *
     * @param departamento the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Departamento> partialUpdate(Departamento departamento) {
        log.debug("Request to partially update Departamento : {}", departamento);

        return departamentoRepository
            .findById(departamento.getId())
            .map(existingDepartamento -> {
                if (departamento.getCodigo() != null) {
                    existingDepartamento.setCodigo(departamento.getCodigo());
                }
                if (departamento.getNome() != null) {
                    existingDepartamento.setNome(departamento.getNome());
                }

                return existingDepartamento;
            })
            .map(departamentoRepository::save);
    }

    /**
     * Get all the departamentos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Departamento> findAll() {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll();
    }

    /**
     * Get one departamento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    /**
     * Delete the departamento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}
