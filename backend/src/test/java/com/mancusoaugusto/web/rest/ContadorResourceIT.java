package com.mancusoaugusto.web.rest;

import static com.mancusoaugusto.domain.ContadorAsserts.*;
import static com.mancusoaugusto.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mancusoaugusto.IntegrationTest;
import com.mancusoaugusto.domain.Contador;
import com.mancusoaugusto.repository.ContadorRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContadorResourceIT {

    private static final Integer DEFAULT_USUARIO_ID = 1;
    private static final Integer UPDATED_USUARIO_ID = 2;

    private static final Integer DEFAULT_CONTADOR_VALOR = 1;
    private static final Integer UPDATED_CONTADOR_VALOR = 2;

    private static final String ENTITY_API_URL = "/api/contadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContadorRepository contadorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContadorMockMvc;

    private Contador contador;

    private Contador insertedContador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contador createEntity() {
        return new Contador().usuarioId(DEFAULT_USUARIO_ID).contadorValor(DEFAULT_CONTADOR_VALOR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contador createUpdatedEntity() {
        return new Contador().usuarioId(UPDATED_USUARIO_ID).contadorValor(UPDATED_CONTADOR_VALOR);
    }

    @BeforeEach
    public void initTest() {
        contador = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedContador != null) {
            contadorRepository.delete(insertedContador);
            insertedContador = null;
        }
    }

    @Test
    @Transactional
    void createContador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contador
        var returnedContador = om.readValue(
            restContadorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contador)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Contador.class
        );

        // Validate the Contador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertContadorUpdatableFieldsEquals(returnedContador, getPersistedContador(returnedContador));

        insertedContador = returnedContador;
    }

    @Test
    @Transactional
    void createContadorWithExistingId() throws Exception {
        // Create the Contador with an existing ID
        contador.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contador)))
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContadors() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        // Get all the contadorList
        restContadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contador.getId().intValue())))
            .andExpect(jsonPath("$.[*].usuarioId").value(hasItem(DEFAULT_USUARIO_ID)))
            .andExpect(jsonPath("$.[*].contadorValor").value(hasItem(DEFAULT_CONTADOR_VALOR)));
    }

    @Test
    @Transactional
    void getContador() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        // Get the contador
        restContadorMockMvc
            .perform(get(ENTITY_API_URL_ID, contador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contador.getId().intValue()))
            .andExpect(jsonPath("$.usuarioId").value(DEFAULT_USUARIO_ID))
            .andExpect(jsonPath("$.contadorValor").value(DEFAULT_CONTADOR_VALOR));
    }

    @Test
    @Transactional
    void getNonExistingContador() throws Exception {
        // Get the contador
        restContadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContador() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contador
        Contador updatedContador = contadorRepository.findById(contador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContador are not directly saved in db
        em.detach(updatedContador);
        updatedContador.usuarioId(UPDATED_USUARIO_ID).contadorValor(UPDATED_CONTADOR_VALOR);

        restContadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContador.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedContador))
            )
            .andExpect(status().isOk());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContadorToMatchAllProperties(updatedContador);
    }

    @Test
    @Transactional
    void putNonExistingContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contador.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContadorWithPatch() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contador using partial update
        Contador partialUpdatedContador = new Contador();
        partialUpdatedContador.setId(contador.getId());

        partialUpdatedContador.contadorValor(UPDATED_CONTADOR_VALOR);

        restContadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContador))
            )
            .andExpect(status().isOk());

        // Validate the Contador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContadorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContador, contador), getPersistedContador(contador));
    }

    @Test
    @Transactional
    void fullUpdateContadorWithPatch() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contador using partial update
        Contador partialUpdatedContador = new Contador();
        partialUpdatedContador.setId(contador.getId());

        partialUpdatedContador.usuarioId(UPDATED_USUARIO_ID).contadorValor(UPDATED_CONTADOR_VALOR);

        restContadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContador))
            )
            .andExpect(status().isOk());

        // Validate the Contador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContadorUpdatableFieldsEquals(partialUpdatedContador, getPersistedContador(partialUpdatedContador));
    }

    @Test
    @Transactional
    void patchNonExistingContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contador))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contador.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContadorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contador)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContador() throws Exception {
        // Initialize the database
        insertedContador = contadorRepository.saveAndFlush(contador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contador
        restContadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, contador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contadorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Contador getPersistedContador(Contador contador) {
        return contadorRepository.findById(contador.getId()).orElseThrow();
    }

    protected void assertPersistedContadorToMatchAllProperties(Contador expectedContador) {
        assertContadorAllPropertiesEquals(expectedContador, getPersistedContador(expectedContador));
    }

    protected void assertPersistedContadorToMatchUpdatableProperties(Contador expectedContador) {
        assertContadorAllUpdatablePropertiesEquals(expectedContador, getPersistedContador(expectedContador));
    }
}
