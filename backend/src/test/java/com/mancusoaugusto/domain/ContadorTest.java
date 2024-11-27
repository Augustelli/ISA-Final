package com.mancusoaugusto.domain;

import static com.mancusoaugusto.domain.ContadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mancusoaugusto.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contador.class);
        Contador contador1 = getContadorSample1();
        Contador contador2 = new Contador();
        assertThat(contador1).isNotEqualTo(contador2);

        contador2.setId(contador1.getId());
        assertThat(contador1).isEqualTo(contador2);

        contador2 = getContadorSample2();
        assertThat(contador1).isNotEqualTo(contador2);
    }
}
