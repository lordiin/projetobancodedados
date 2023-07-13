package projetobancodedados.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.IntegrationTest;
import projetobancodedados.app.domain.Professor;
import projetobancodedados.app.repository.ProfessorRepository;

/**
 * Integration tests for the {@link ProfessorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessorMockMvc;

    private Professor professor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professor createEntity(EntityManager em) {
        Professor professor = new Professor().nome(DEFAULT_NOME).email(DEFAULT_EMAIL);
        return professor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professor createUpdatedEntity(EntityManager em) {
        Professor professor = new Professor().nome(UPDATED_NOME).email(UPDATED_EMAIL);
        return professor;
    }

    @BeforeEach
    public void initTest() {
        professor = createEntity(em);
    }

    @Test
    @Transactional
    void createProfessor() throws Exception {
        int databaseSizeBeforeCreate = professorRepository.findAll().size();
        // Create the Professor
        restProfessorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professor)))
            .andExpect(status().isCreated());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeCreate + 1);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProfessor.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createProfessorWithExistingId() throws Exception {
        // Create the Professor with an existing ID
        professor.setId(1L);

        int databaseSizeBeforeCreate = professorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professor)))
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfessors() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get all the professorList
        restProfessorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        // Get the professor
        restProfessorMockMvc
            .perform(get(ENTITY_API_URL_ID, professor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingProfessor() throws Exception {
        // Get the professor
        restProfessorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeUpdate = professorRepository.findAll().size();

        // Update the professor
        Professor updatedProfessor = professorRepository.findById(professor.getId()).get();
        // Disconnect from session so that the updates on updatedProfessor are not directly saved in db
        em.detach(updatedProfessor);
        updatedProfessor.nome(UPDATED_NOME).email(UPDATED_EMAIL);

        restProfessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfessor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProfessor))
            )
            .andExpect(status().isOk());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProfessor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessorWithPatch() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeUpdate = professorRepository.findAll().size();

        // Update the professor using partial update
        Professor partialUpdatedProfessor = new Professor();
        partialUpdatedProfessor.setId(professor.getId());

        partialUpdatedProfessor.email(UPDATED_EMAIL);

        restProfessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessor))
            )
            .andExpect(status().isOk());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProfessor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateProfessorWithPatch() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeUpdate = professorRepository.findAll().size();

        // Update the professor using partial update
        Professor partialUpdatedProfessor = new Professor();
        partialUpdatedProfessor.setId(professor.getId());

        partialUpdatedProfessor.nome(UPDATED_NOME).email(UPDATED_EMAIL);

        restProfessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfessor))
            )
            .andExpect(status().isOk());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
        Professor testProfessor = professorList.get(professorList.size() - 1);
        assertThat(testProfessor.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProfessor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessor() throws Exception {
        int databaseSizeBeforeUpdate = professorRepository.findAll().size();
        professor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(professor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professor in the database
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessor() throws Exception {
        // Initialize the database
        professorRepository.saveAndFlush(professor);

        int databaseSizeBeforeDelete = professorRepository.findAll().size();

        // Delete the professor
        restProfessorMockMvc
            .perform(delete(ENTITY_API_URL_ID, professor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professor> professorList = professorRepository.findAll();
        assertThat(professorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
