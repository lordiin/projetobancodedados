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
import projetobancodedados.app.domain.Professor;
import projetobancodedados.app.repository.ProfessorRepository;
import projetobancodedados.app.service.ProfessorService;
import projetobancodedados.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link projetobancodedados.app.domain.Professor}.
 */
@RestController
@RequestMapping("/api")
public class ProfessorResource {

    private final Logger log = LoggerFactory.getLogger(ProfessorResource.class);

    private static final String ENTITY_NAME = "professor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessorService professorService;

    private final ProfessorRepository professorRepository;

    public ProfessorResource(ProfessorService professorService, ProfessorRepository professorRepository) {
        this.professorService = professorService;
        this.professorRepository = professorRepository;
    }

    /**
     * {@code POST  /professors} : Create a new professor.
     *
     * @param professor the professor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professor, or with status {@code 400 (Bad Request)} if the professor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/professors")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) throws URISyntaxException {
        log.debug("REST request to save Professor : {}", professor);
        if (professor.getId() != null) {
            throw new BadRequestAlertException("A new professor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Professor result = professorService.save(professor);
        return ResponseEntity
            .created(new URI("/api/professors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /professors/:id} : Updates an existing professor.
     *
     * @param id the id of the professor to save.
     * @param professor the professor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professor,
     * or with status {@code 400 (Bad Request)} if the professor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/professors/{id}")
    public ResponseEntity<Professor> updateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Professor professor
    ) throws URISyntaxException {
        log.debug("REST request to update Professor : {}, {}", id, professor);
        if (professor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Professor result = professorService.update(professor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /professors/:id} : Partial updates given fields of an existing professor, field will ignore if it is null
     *
     * @param id the id of the professor to save.
     * @param professor the professor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professor,
     * or with status {@code 400 (Bad Request)} if the professor is not valid,
     * or with status {@code 404 (Not Found)} if the professor is not found,
     * or with status {@code 500 (Internal Server Error)} if the professor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/professors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Professor> partialUpdateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Professor professor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Professor partially : {}, {}", id, professor);
        if (professor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Professor> result = professorService.partialUpdate(professor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, professor.getId().toString())
        );
    }

    /**
     * {@code GET  /professors} : get all the professors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professors in body.
     */
    @GetMapping("/professors")
    public List<Professor> getAllProfessors() {
        log.debug("REST request to get all Professors");
        return professorService.findAll();
    }

    /**
     * {@code GET  /professors/:id} : get the "id" professor.
     *
     * @param id the id of the professor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/professors/{id}")
    public ResponseEntity<Professor> getProfessor(@PathVariable Long id) {
        log.debug("REST request to get Professor : {}", id);
        Optional<Professor> professor = professorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professor);
    }

    /**
     * {@code DELETE  /professors/:id} : delete the "id" professor.
     *
     * @param id the id of the professor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/professors/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        log.debug("REST request to delete Professor : {}", id);
        professorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
