package com.mancusoaugusto.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ContadorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContadorAllPropertiesEquals(Contador expected, Contador actual) {
        assertContadorAutoGeneratedPropertiesEquals(expected, actual);
        assertContadorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContadorAllUpdatablePropertiesEquals(Contador expected, Contador actual) {
        assertContadorUpdatableFieldsEquals(expected, actual);
        assertContadorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContadorAutoGeneratedPropertiesEquals(Contador expected, Contador actual) {
        assertThat(expected)
            .as("Verify Contador auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContadorUpdatableFieldsEquals(Contador expected, Contador actual) {
        assertThat(expected)
            .as("Verify Contador relevant properties")
            .satisfies(e -> assertThat(e.getUsuarioId()).as("check usuarioId").isEqualTo(actual.getUsuarioId()))
            .satisfies(e -> assertThat(e.getContadorValor()).as("check contadorValor").isEqualTo(actual.getContadorValor()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertContadorUpdatableRelationshipsEquals(Contador expected, Contador actual) {
        // empty method
    }
}