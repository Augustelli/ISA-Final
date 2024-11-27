package com.mancusoaugusto.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ContadorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Contador getContadorSample1() {
        return new Contador().id(1L).usuarioId(1).contadorValor(1);
    }

    public static Contador getContadorSample2() {
        return new Contador().id(2L).usuarioId(2).contadorValor(2);
    }

    public static Contador getContadorRandomSampleGenerator() {
        return new Contador()
            .id(longCount.incrementAndGet())
            .usuarioId(intCount.incrementAndGet())
            .contadorValor(intCount.incrementAndGet());
    }
}
