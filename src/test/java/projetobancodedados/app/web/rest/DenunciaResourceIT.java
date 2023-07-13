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
import projetobancodedados.app.domain.Denuncia;
import projetobancodedados.app.repository.DenunciaRepository;

/**
 * Integration tests for the {@link DenunciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DenunciaResourceIT {

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/denuncias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDenunciaMockMvc;

    private Denuncia denuncia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Denuncia createEntity(EntityManager em) {
        Denuncia denuncia = new Denuncia().motivo(DEFAULT_MOTIVO);
        return denuncia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Denuncia createUpdatedEntity(EntityManager em) {
        Denuncia denuncia = new Denuncia().motivo(UPDATED_MOTIVO);
        return denuncia;
    }

    @BeforeEach
    public void initTest() {
        denuncia = createEntity(em);
    }

    @Test
    @Transactional
    void createDenuncia() throws Exception {
        int databaseSizeBeforeCreate = denunciaRepository.findAll().size();
        // Create the Denuncia
        restDenunciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(denuncia)))
            .andExpect(status().isCreated());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeCreate + 1);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    void createDenunciaWithExistingId() throws Exception {
        // Create the Denuncia with an existing ID
        denuncia.setId(1L);

        int databaseSizeBeforeCreate = denunciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDenunciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(denuncia)))
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDenuncias() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        // Get all the denunciaList
        restDenunciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(denuncia.getId().intValue())))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)));
    }

    @Test
    @Transactional
    void getDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        // Get the denuncia
        restDenunciaMockMvc
            .perform(get(ENTITY_API_URL_ID, denuncia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(denuncia.getId().intValue()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO));
    }

    @Test
    @Transactional
    void getNonExistingDenuncia() throws Exception {
        // Get the denuncia
        restDenunciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Update the denuncia
        Denuncia updatedDenuncia = denunciaRepository.findById(denuncia.getId()).get();
        // Disconnect from session so that the updates on updatedDenuncia are not directly saved in db
        em.detach(updatedDenuncia);
        updatedDenuncia.motivo(UPDATED_MOTIVO);

        restDenunciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDenuncia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDenuncia))
            )
            .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void putNonExistingDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, denuncia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(denuncia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(denuncia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(denuncia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDenunciaWithPatch() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Update the denuncia using partial update
        Denuncia partialUpdatedDenuncia = new Denuncia();
        partialUpdatedDenuncia.setId(denuncia.getId());

        restDenunciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDenuncia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDenuncia))
            )
            .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    void fullUpdateDenunciaWithPatch() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();

        // Update the denuncia using partial update
        Denuncia partialUpdatedDenuncia = new Denuncia();
        partialUpdatedDenuncia.setId(denuncia.getId());

        partialUpdatedDenuncia.motivo(UPDATED_MOTIVO);

        restDenunciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDenuncia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDenuncia))
            )
            .andExpect(status().isOk());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
        Denuncia testDenuncia = denunciaList.get(denunciaList.size() - 1);
        assertThat(testDenuncia.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void patchNonExistingDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, denuncia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(denuncia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(denuncia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDenuncia() throws Exception {
        int databaseSizeBeforeUpdate = denunciaRepository.findAll().size();
        denuncia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDenunciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(denuncia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Denuncia in the database
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDenuncia() throws Exception {
        // Initialize the database
        denunciaRepository.saveAndFlush(denuncia);

        int databaseSizeBeforeDelete = denunciaRepository.findAll().size();

        // Delete the denuncia
        restDenunciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, denuncia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Denuncia> denunciaList = denunciaRepository.findAll();
        assertThat(denunciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
