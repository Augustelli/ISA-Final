package com.mancusoaugusto.repository;

import com.mancusoaugusto.domain.Contador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContadorRepository extends JpaRepository<Contador, Long> {}
