package com.mancusoaugusto.repository;

import com.mancusoaugusto.domain.Contador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Contador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContadorRepository extends JpaRepository<Contador, Long> {
    List<Contador> getContadorByUsuarioId(Integer usuarioId);
}
