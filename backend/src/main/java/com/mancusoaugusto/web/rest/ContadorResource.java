package com.mancusoaugusto.web.rest;

import com.mancusoaugusto.domain.Contador;
import com.mancusoaugusto.repository.ContadorRepository;
import com.mancusoaugusto.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mancusoaugusto.domain.Contador}.
 */
@RestController
@RequestMapping("/api/contadors")
@Transactional
public class ContadorResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContadorResource.class);

    private static final String ENTITY_NAME = "contador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContadorRepository contadorRepository;

    public ContadorResource(ContadorRepository contadorRepository) {
        this.contadorRepository = contadorRepository;
    }

    /**
     * {@code POST  /contadors} : Create a new contador.
     *
     * @param contador the contador to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contador, or with status {@code 400 (Bad Request)} if the contador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Contador> createContador(@RequestBody Contador contador) throws URISyntaxException {
        LOG.debug("REST request to save Contador : {}", contador);
        if (contador.getId() != null) {
            throw new BadRequestAlertException("A new contador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contador = contadorRepository.save(contador);
        return ResponseEntity.created(new URI("/api/contadors/" + contador.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, contador.getId().toString()))
            .body(contador);
    }

    /**
     * {@code PUT  /contadors/:id} : Updates an existing contador.
     *
     * @param id the id of the contador to save.
     * @param contador the contador to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contador,
     * or with status {@code 400 (Bad Request)} if the contador is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contador couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contador> updateContador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contador contador
    ) throws URISyntaxException {
        LOG.debug("REST request to update Contador : {}, {}", id, contador);
        if (contador.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contador.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contador = contadorRepository.save(contador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contador.getId().toString()))
            .body(contador);
    }

    /**
     * {@code PATCH  /contadors/:id} : Partial updates given fields of an existing contador, field will ignore if it is null
     *
     * @param id the id of the contador to save.
     * @param contador the contador to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contador,
     * or with status {@code 400 (Bad Request)} if the contador is not valid,
     * or with status {@code 404 (Not Found)} if the contador is not found,
     * or with status {@code 500 (Internal Server Error)} if the contador couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contador> partialUpdateContador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contador contador
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Contador partially : {}, {}", id, contador);
        if (contador.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contador.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contador> result = contadorRepository
            .findById(contador.getId())
            .map(existingContador -> {
                if (contador.getUsuarioId() != null) {
                    existingContador.setUsuarioId(contador.getUsuarioId());
                }
                if (contador.getContadorValor() != null) {
                    existingContador.setContadorValor(contador.getContadorValor());
                }

                return existingContador;
            })
            .map(contadorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contador.getId().toString())
        );
    }

    /**
     * {@code GET  /contadors} : get all the contadors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contadors in body.
     */
    @GetMapping("")
    public List<Contador> getAllContadors() {
        LOG.debug("REST request to get all Contadors");
        return contadorRepository.findAll();
    }

    /**
     * {@code GET  /contadors/:id} : get the "id" contador.
     *
     * @param id the id of the contador to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contador, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contador> getContador(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Contador : {}", id);
        Optional<Contador> contador = contadorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contador);
    }

    /**
     * {@code DELETE  /contadors/:id} : delete the "id" contador.
     *
     * @param id the id of the contador to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContador(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Contador : {}", id);
        contadorRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
