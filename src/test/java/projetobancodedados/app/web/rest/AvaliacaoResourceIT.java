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
import projetobancodedados.app.domain.Avaliacao;
import projetobancodedados.app.repository.AvaliacaoRepository;

/**
 * Integration tests for the {@link AvaliacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AvaliacaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOTA = 1;
    private static final Integer UPDATED_NOTA = 2;

    private static final String ENTITY_API_URL = "/api/avaliacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAvaliacaoMockMvc;

    private Avaliacao avaliacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avaliacao createEntity(EntityManager em) {
        Avaliacao avaliacao = new Avaliacao().descricao(DEFAULT_DESCRICAO).nota(DEFAULT_NOTA);
        return avaliacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Avaliacao createUpdatedEntity(EntityManager em) {
        Avaliacao avaliacao = new Avaliacao().descricao(UPDATED_DESCRICAO).nota(UPDATED_NOTA);
        return avaliacao;
    }

    @BeforeEach
    public void initTest() {
        avaliacao = createEntity(em);
    }

    @Test
    @Transactional
    void createAvaliacao() throws Exception {
        int databaseSizeBeforeCreate = avaliacaoRepository.findAll().size();
        // Create the Avaliacao
        restAvaliacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avaliacao)))
            .andExpect(status().isCreated());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Avaliacao testAvaliacao = avaliacaoList.get(avaliacaoList.size() - 1);
        assertThat(testAvaliacao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testAvaliacao.getNota()).isEqualTo(DEFAULT_NOTA);
    }

    @Test
    @Transactional
    void createAvaliacaoWithExistingId() throws Exception {
        // Create the Avaliacao with an existing ID
        avaliacao.setId(1L);

        int databaseSizeBeforeCreate = avaliacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvaliacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avaliacao)))
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAvaliacaos() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        // Get all the avaliacaoList
        restAvaliacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avaliacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)));
    }

    @Test
    @Transactional
    void getAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        // Get the avaliacao
        restAvaliacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, avaliacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(avaliacao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA));
    }

    @Test
    @Transactional
    void getNonExistingAvaliacao() throws Exception {
        // Get the avaliacao
        restAvaliacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();

        // Update the avaliacao
        Avaliacao updatedAvaliacao = avaliacaoRepository.findById(avaliacao.getId()).get();
        // Disconnect from session so that the updates on updatedAvaliacao are not directly saved in db
        em.detach(updatedAvaliacao);
        updatedAvaliacao.descricao(UPDATED_DESCRICAO).nota(UPDATED_NOTA);

        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAvaliacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
        Avaliacao testAvaliacao = avaliacaoList.get(avaliacaoList.size() - 1);
        assertThat(testAvaliacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAvaliacao.getNota()).isEqualTo(UPDATED_NOTA);
    }

    @Test
    @Transactional
    void putNonExistingAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, avaliacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(avaliacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAvaliacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();

        // Update the avaliacao using partial update
        Avaliacao partialUpdatedAvaliacao = new Avaliacao();
        partialUpdatedAvaliacao.setId(avaliacao.getId());

        partialUpdatedAvaliacao.descricao(UPDATED_DESCRICAO);

        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
        Avaliacao testAvaliacao = avaliacaoList.get(avaliacaoList.size() - 1);
        assertThat(testAvaliacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAvaliacao.getNota()).isEqualTo(DEFAULT_NOTA);
    }

    @Test
    @Transactional
    void fullUpdateAvaliacaoWithPatch() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();

        // Update the avaliacao using partial update
        Avaliacao partialUpdatedAvaliacao = new Avaliacao();
        partialUpdatedAvaliacao.setId(avaliacao.getId());

        partialUpdatedAvaliacao.descricao(UPDATED_DESCRICAO).nota(UPDATED_NOTA);

        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAvaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAvaliacao))
            )
            .andExpect(status().isOk());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
        Avaliacao testAvaliacao = avaliacaoList.get(avaliacaoList.size() - 1);
        assertThat(testAvaliacao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testAvaliacao.getNota()).isEqualTo(UPDATED_NOTA);
    }

    @Test
    @Transactional
    void patchNonExistingAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, avaliacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(avaliacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAvaliacao() throws Exception {
        int databaseSizeBeforeUpdate = avaliacaoRepository.findAll().size();
        avaliacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAvaliacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(avaliacao))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Avaliacao in the database
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAvaliacao() throws Exception {
        // Initialize the database
        avaliacaoRepository.saveAndFlush(avaliacao);

        int databaseSizeBeforeDelete = avaliacaoRepository.findAll().size();

        // Delete the avaliacao
        restAvaliacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, avaliacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Avaliacao> avaliacaoList = avaliacaoRepository.findAll();
        assertThat(avaliacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
