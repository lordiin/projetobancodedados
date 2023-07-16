package projetobancodedados.app.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.domain.Avaliacao;
import projetobancodedados.app.repository.AvaliacaoRepository;

/**
 * Service Implementation for managing {@link Avaliacao}.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AvaliacaoService {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoService.class);

    private final AvaliacaoRepository avaliacaoRepository;

    /**
     * Save a avaliacao.
     *
     * @param avaliacao the entity to save.
     * @return the persisted entity.
     */
    public void save(Avaliacao avaliacao) {
        log.debug("Request to save Avaliacao : {}", avaliacao);
        avaliacaoRepository.save(avaliacao.getDescricao(), avaliacao.getNota(), avaliacao.getTurma().getId());
    }

    /**
     * Update a avaliacao.
     *
     * @param avaliacao the entity to save.
     * @return the persisted entity.
     */
    public void update(Avaliacao avaliacao) {
        log.debug("Request to update Avaliacao : {}", avaliacao);
        avaliacaoRepository.update(avaliacao.getId(), avaliacao.getDescricao(), avaliacao.getNota(), avaliacao.getTurma().getId());
    }

    /**
     * Get all the avaliacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Avaliacao> findAll(Pageable pageable) {
        log.debug("Request to get all Avaliacaos");
        Page avaliacoes = avaliacaoRepository.findAll(pageable);
        avaliacoes.getContent().forEach(avaliacao -> log.info(avaliacao.toString()));
        return avaliacoes;
    }

    /**
     * Get one avaliacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Avaliacao findOne(Long id) {
        log.debug("Request to get Avaliacao : {}", id);
        Avaliacao avaliacao = avaliacaoRepository.findOne(id);
        log.info(avaliacao.toString());
        return avaliacao;
    }

    /**
     * Delete the avaliacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Avaliacao : {}", id);
        avaliacaoRepository.delete(id);
    }
}
