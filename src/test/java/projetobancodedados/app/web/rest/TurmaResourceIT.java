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
import projetobancodedados.app.domain.Turma;
import projetobancodedados.app.repository.TurmaRepository;

/**
 * Integration tests for the {@link TurmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurmaResourceIT {

    private static final String DEFAULT_TURMA = "AAAAAAAAAA";
    private static final String UPDATED_TURMA = "BBBBBBBBBB";

    private static final String DEFAULT_PERIODO = "AAAAAAAAAA";
    private static final String UPDATED_PERIODO = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSOR = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSOR = "BBBBBBBBBB";

    private static final String DEFAULT_HORARIO = "AAAAAAAAAA";
    private static final String UPDATED_HORARIO = "BBBBBBBBBB";

    private static final String DEFAULT_VAGAS_OCUPADAS = "AAAAAAAAAA";
    private static final String UPDATED_VAGAS_OCUPADAS = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL_VAGAS = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_VAGAS = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/turmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurmaMockMvc;

    private Turma turma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createEntity(EntityManager em) {
        Turma turma = new Turma()
            .turma(DEFAULT_TURMA)
            .periodo(DEFAULT_PERIODO)
            .professor(DEFAULT_PROFESSOR)
            .horario(DEFAULT_HORARIO)
            .vagasOcupadas(DEFAULT_VAGAS_OCUPADAS)
            .totalVagas(DEFAULT_TOTAL_VAGAS)
            .local(DEFAULT_LOCAL);
        return turma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createUpdatedEntity(EntityManager em) {
        Turma turma = new Turma()
            .turma(UPDATED_TURMA)
            .periodo(UPDATED_PERIODO)
            .professor(UPDATED_PROFESSOR)
            .horario(UPDATED_HORARIO)
            .vagasOcupadas(UPDATED_VAGAS_OCUPADAS)
            .totalVagas(UPDATED_TOTAL_VAGAS)
            .local(UPDATED_LOCAL);
        return turma;
    }

    @BeforeEach
    public void initTest() {
        turma = createEntity(em);
    }

    @Test
    @Transactional
    void createTurma() throws Exception {
        int databaseSizeBeforeCreate = turmaRepository.findAll().size();
        // Create the Turma
        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isCreated());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate + 1);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getTurma()).isEqualTo(DEFAULT_TURMA);
        assertThat(testTurma.getPeriodo()).isEqualTo(DEFAULT_PERIODO);
        assertThat(testTurma.getProfessor()).isEqualTo(DEFAULT_PROFESSOR);
        assertThat(testTurma.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testTurma.getVagasOcupadas()).isEqualTo(DEFAULT_VAGAS_OCUPADAS);
        assertThat(testTurma.getTotalVagas()).isEqualTo(DEFAULT_TOTAL_VAGAS);
        assertThat(testTurma.getLocal()).isEqualTo(DEFAULT_LOCAL);
    }

    @Test
    @Transactional
    void createTurmaWithExistingId() throws Exception {
        // Create the Turma with an existing ID
        turma.setId(1L);

        int databaseSizeBeforeCreate = turmaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTurmas() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].turma").value(hasItem(DEFAULT_TURMA)))
            .andExpect(jsonPath("$.[*].periodo").value(hasItem(DEFAULT_PERIODO)))
            .andExpect(jsonPath("$.[*].professor").value(hasItem(DEFAULT_PROFESSOR)))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO)))
            .andExpect(jsonPath("$.[*].vagasOcupadas").value(hasItem(DEFAULT_VAGAS_OCUPADAS)))
            .andExpect(jsonPath("$.[*].totalVagas").value(hasItem(DEFAULT_TOTAL_VAGAS)))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL)));
    }

    @Test
    @Transactional
    void getTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get the turma
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL_ID, turma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turma.getId().intValue()))
            .andExpect(jsonPath("$.turma").value(DEFAULT_TURMA))
            .andExpect(jsonPath("$.periodo").value(DEFAULT_PERIODO))
            .andExpect(jsonPath("$.professor").value(DEFAULT_PROFESSOR))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO))
            .andExpect(jsonPath("$.vagasOcupadas").value(DEFAULT_VAGAS_OCUPADAS))
            .andExpect(jsonPath("$.totalVagas").value(DEFAULT_TOTAL_VAGAS))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL));
    }

    @Test
    @Transactional
    void getNonExistingTurma() throws Exception {
        // Get the turma
        restTurmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma
        Turma updatedTurma = turmaRepository.findById(turma.getId()).get();
        // Disconnect from session so that the updates on updatedTurma are not directly saved in db
        em.detach(updatedTurma);
        updatedTurma
            .turma(UPDATED_TURMA)
            .periodo(UPDATED_PERIODO)
            .professor(UPDATED_PROFESSOR)
            .horario(UPDATED_HORARIO)
            .vagasOcupadas(UPDATED_VAGAS_OCUPADAS)
            .totalVagas(UPDATED_TOTAL_VAGAS)
            .local(UPDATED_LOCAL);

        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTurma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getTurma()).isEqualTo(UPDATED_TURMA);
        assertThat(testTurma.getPeriodo()).isEqualTo(UPDATED_PERIODO);
        assertThat(testTurma.getProfessor()).isEqualTo(UPDATED_PROFESSOR);
        assertThat(testTurma.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testTurma.getVagasOcupadas()).isEqualTo(UPDATED_VAGAS_OCUPADAS);
        assertThat(testTurma.getTotalVagas()).isEqualTo(UPDATED_TOTAL_VAGAS);
        assertThat(testTurma.getLocal()).isEqualTo(UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void putNonExistingTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma.professor(UPDATED_PROFESSOR).vagasOcupadas(UPDATED_VAGAS_OCUPADAS).local(UPDATED_LOCAL);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getTurma()).isEqualTo(DEFAULT_TURMA);
        assertThat(testTurma.getPeriodo()).isEqualTo(DEFAULT_PERIODO);
        assertThat(testTurma.getProfessor()).isEqualTo(UPDATED_PROFESSOR);
        assertThat(testTurma.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testTurma.getVagasOcupadas()).isEqualTo(UPDATED_VAGAS_OCUPADAS);
        assertThat(testTurma.getTotalVagas()).isEqualTo(DEFAULT_TOTAL_VAGAS);
        assertThat(testTurma.getLocal()).isEqualTo(UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void fullUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma
            .turma(UPDATED_TURMA)
            .periodo(UPDATED_PERIODO)
            .professor(UPDATED_PROFESSOR)
            .horario(UPDATED_HORARIO)
            .vagasOcupadas(UPDATED_VAGAS_OCUPADAS)
            .totalVagas(UPDATED_TOTAL_VAGAS)
            .local(UPDATED_LOCAL);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getTurma()).isEqualTo(UPDATED_TURMA);
        assertThat(testTurma.getPeriodo()).isEqualTo(UPDATED_PERIODO);
        assertThat(testTurma.getProfessor()).isEqualTo(UPDATED_PROFESSOR);
        assertThat(testTurma.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testTurma.getVagasOcupadas()).isEqualTo(UPDATED_VAGAS_OCUPADAS);
        assertThat(testTurma.getTotalVagas()).isEqualTo(UPDATED_TOTAL_VAGAS);
        assertThat(testTurma.getLocal()).isEqualTo(UPDATED_LOCAL);
    }

    @Test
    @Transactional
    void patchNonExistingTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeDelete = turmaRepository.findAll().size();

        // Delete the turma
        restTurmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, turma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
