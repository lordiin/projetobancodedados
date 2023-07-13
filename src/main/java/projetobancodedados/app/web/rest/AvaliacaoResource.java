package projetobancodedados.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import projetobancodedados.app.domain.Avaliacao;
import projetobancodedados.app.repository.AvaliacaoRepository;
import projetobancodedados.app.service.AvaliacaoService;
import projetobancodedados.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link projetobancodedados.app.domain.Avaliacao}.
 */
@RestController
@RequestMapping("/api")
public class AvaliacaoResource {

    private final Logger log = LoggerFactory.getLogger(AvaliacaoResource.class);

    private static final String ENTITY_NAME = "avaliacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvaliacaoService avaliacaoService;

    private final AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoResource(AvaliacaoService avaliacaoService, AvaliacaoRepository avaliacaoRepository) {
        this.avaliacaoService = avaliacaoService;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    /**
     * {@code POST  /avaliacaos} : Create a new avaliacao.
     *
     * @param avaliacao the avaliacao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avaliacao, or with status {@code 400 (Bad Request)} if the avaliacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/avaliacaos")
    public ResponseEntity<Avaliacao> createAvaliacao(@RequestBody Avaliacao avaliacao) throws URISyntaxException {
        log.debug("REST request to save Avaliacao : {}", avaliacao);
        if (avaliacao.getId() != null) {
            throw new BadRequestAlertException("A new avaliacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        avaliacaoService.save(avaliacao);
        return ResponseEntity.ok().body(avaliacao);
    }

    /**
     * {@code PUT  /avaliacaos/:id} : Updates an existing avaliacao.
     *
     * @param id the id of the avaliacao to save.
     * @param avaliacao the avaliacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacao,
     * or with status {@code 400 (Bad Request)} if the avaliacao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avaliacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/avaliacaos/{id}")
    public ResponseEntity<Avaliacao> updateAvaliacao(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Avaliacao avaliacao
    ) throws URISyntaxException {
        log.debug("REST request to update Avaliacao : {}, {}", id, avaliacao);
        if (avaliacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avaliacao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avaliacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        avaliacaoService.update(avaliacao);
        return ResponseEntity.ok().body(avaliacao);
    }

    /**
     * {@code PATCH  /avaliacaos/:id} : Partial updates given fields of an existing avaliacao, field will ignore if it is null
     *
     * @param id the id of the avaliacao to save.
     * @param avaliacao the avaliacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avaliacao,
     * or with status {@code 400 (Bad Request)} if the avaliacao is not valid,
     * or with status {@code 404 (Not Found)} if the avaliacao is not found,
     * or with status {@code 500 (Internal Server Error)} if the avaliacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    //    @PatchMapping(value = "/avaliacaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    //    public ResponseEntity<Avaliacao> partialUpdateAvaliacao(
    //        @PathVariable(value = "id", required = false) final Long id,
    //        @RequestBody Avaliacao avaliacao
    //    ) throws URISyntaxException {
    //        log.debug("REST request to partial update Avaliacao partially : {}, {}", id, avaliacao);
    //        if (avaliacao.getId() == null) {
    //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    //        }
    //        if (!Objects.equals(id, avaliacao.getId())) {
    //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    //        }
    //
    //        if (!avaliacaoRepository.existsById(id)) {
    //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    //        }
    //
    //        Optional<Avaliacao> result = avaliacaoService.partialUpdate(avaliacao);
    //
    //        return ResponseUtil.wrapOrNotFound(
    //            result,
    //            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, avaliacao.getId().toString())
    //        );
    //    }

    /**
     * {@code GET  /avaliacaos} : get all the avaliacaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avaliacaos in body.
     */
    @GetMapping("/avaliacaos")
    public ResponseEntity<List<Avaliacao>> getAllAvaliacaos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Avaliacaos");
        Page<Avaliacao> page = avaliacaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /avaliacaos/:id} : get the "id" avaliacao.
     *
     * @param id the id of the avaliacao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avaliacao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/avaliacaos/{id}")
    public ResponseEntity<Avaliacao> getAvaliacao(@PathVariable Long id) {
        log.debug("REST request to get Avaliacao : {}", id);
        Avaliacao avaliacao = avaliacaoService.findOne(id);
        return ResponseEntity.ok().body(avaliacao);
    }

    /**
     * {@code DELETE  /avaliacaos/:id} : delete the "id" avaliacao.
     *
     * @param id the id of the avaliacao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/avaliacaos/{id}")
    public ResponseEntity<Void> deleteAvaliacao(@PathVariable Long id) {
        log.debug("REST request to delete Avaliacao : {}", id);
        avaliacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
