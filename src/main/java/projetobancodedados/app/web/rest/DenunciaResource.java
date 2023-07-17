package projetobancodedados.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetobancodedados.app.domain.Denuncia;
import projetobancodedados.app.repository.DenunciaRepository;
import projetobancodedados.app.service.DenunciaService;
import projetobancodedados.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link projetobancodedados.app.domain.Denuncia}.
 */
@RestController
@RequestMapping("/api")
public class DenunciaResource {

    private final Logger log = LoggerFactory.getLogger(DenunciaResource.class);

    private static final String ENTITY_NAME = "denuncia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DenunciaService denunciaService;

    private final DenunciaRepository denunciaRepository;

    public DenunciaResource(DenunciaService denunciaService, DenunciaRepository denunciaRepository) {
        this.denunciaService = denunciaService;
        this.denunciaRepository = denunciaRepository;
    }

    /**
     * {@code POST  /denuncias} : Create a new denuncia.
     *
     * @param denuncia the denuncia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new denuncia, or with status {@code 400 (Bad Request)} if the denuncia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/denuncias")
    public ResponseEntity<Denuncia> createDenuncia(@RequestBody Denuncia denuncia) throws URISyntaxException {
        log.debug("REST request to save Denuncia : {}", denuncia);
        if (denuncia.getId() != null) {
            throw new BadRequestAlertException("A new denuncia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Denuncia result = denunciaService.save(denuncia);
        return ResponseEntity
            .created(new URI("/api/denuncias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /denuncias/:id} : Updates an existing denuncia.
     *
     * @param id the id of the denuncia to save.
     * @param denuncia the denuncia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated denuncia,
     * or with status {@code 400 (Bad Request)} if the denuncia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the denuncia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/denuncias/{id}")
    public ResponseEntity<Denuncia> updateDenuncia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Denuncia denuncia
    ) throws URISyntaxException {
        log.debug("REST request to update Denuncia : {}, {}", id, denuncia);
        if (denuncia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, denuncia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!denunciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Denuncia result = denunciaService.update(denuncia);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, denuncia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /denuncias/:id} : Partial updates given fields of an existing denuncia, field will ignore if it is null
     *
     * @param id the id of the denuncia to save.
     * @param denuncia the denuncia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated denuncia,
     * or with status {@code 400 (Bad Request)} if the denuncia is not valid,
     * or with status {@code 404 (Not Found)} if the denuncia is not found,
     * or with status {@code 500 (Internal Server Error)} if the denuncia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/denuncias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Denuncia> partialUpdateDenuncia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Denuncia denuncia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Denuncia partially : {}, {}", id, denuncia);
        if (denuncia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, denuncia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!denunciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Denuncia> result = denunciaService.partialUpdate(denuncia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, denuncia.getId().toString())
        );
    }

    /**
     * {@code GET  /denuncias} : get all the denuncias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of denuncias in body.
     */
    @GetMapping("/denuncias")
    public List<Denuncia> getAllDenuncias() {
        log.debug("REST request to get all Denuncias");
        return denunciaService.findAll();
    }

    /**
     * {@code GET  /denuncias/:id} : get the "id" denuncia.
     *
     * @param id the id of the denuncia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the denuncia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/denuncias/{id}")
    public ResponseEntity<Denuncia> getDenuncia(@PathVariable Long id) {
        log.debug("REST request to get Denuncia : {}", id);
        Optional<Denuncia> denuncia = denunciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(denuncia);
    }

    /**
     * {@code DELETE  /denuncias/:id} : delete the "id" denuncia.
     *
     * @param id the id of the denuncia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/denuncias/{id}")
    public ResponseEntity<Void> deleteDenuncia(@PathVariable Long id) {
        log.debug("REST request to delete Denuncia : {}", id);
        denunciaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/denuncias/avaliacao/{avaliacaoId}")
    public ResponseEntity<Denuncia> createDenuncia(@RequestBody Denuncia denuncia, @PathVariable Long avaliacaoId)
        throws URISyntaxException {
        log.debug("REST request to save Denuncia : {}", denuncia);
        if (denuncia.getId() != null) {
            throw new BadRequestAlertException("A new denuncia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Denuncia result = denunciaService.saveDenuncia(denuncia, avaliacaoId);
        return ResponseEntity.ok().body(result);
    }
}
