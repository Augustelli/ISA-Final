package com.mancusoaugusto.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Contador.
 */
@Entity
@Table(name = "contador")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "contador_valor")
    private Integer contadorValor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return this.usuarioId;
    }

    public Contador usuarioId(Integer usuarioId) {
        this.setUsuarioId(usuarioId);
        return this;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getContadorValor() {
        return this.contadorValor;
    }

    public Contador contadorValor(Integer contadorValor) {
        this.setContadorValor(contadorValor);
        return this;
    }

    public void setContadorValor(Integer contadorValor) {
        this.contadorValor = contadorValor;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contador)) {
            return false;
        }
        return getId() != null && getId().equals(((Contador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contador{" +
            "id=" + getId() +
            ", usuarioId=" + getUsuarioId() +
            ", contadorValor=" + getContadorValor() +
            "}";
    }
}
