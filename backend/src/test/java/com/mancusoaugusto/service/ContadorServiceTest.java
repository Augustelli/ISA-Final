package com.mancusoaugusto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mancusoaugusto.domain.Contador;
import com.mancusoaugusto.domain.User;
import com.mancusoaugusto.repository.ContadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;


class ContadorServiceTest {

    @Mock
    private ContadorRepository contadorRepository;

    @InjectMocks
    private ContadorService contadorService;

    private User user;
    private Contador contador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setLogin("testUser");

        contador = new Contador();
        contador.setUsuarioId(1);
        contador.setContadorValor(10);
    }

    @Test
    void obtenerValorContador_ReturnsExistingContadorValue() throws Exception {
        when(contadorRepository.getContadorByUsuarioId(1)).thenReturn(List.of(contador));

        Integer valor = contadorService.obtenerValorContador(user);

        assertEquals(10, valor);
        verify(contadorRepository, times(1)).getContadorByUsuarioId(1);
    }

    @Test
    void obtenerValorContador_CreatesNewContadorIfNotFound() throws Exception {
        when(contadorRepository.getContadorByUsuarioId(1)).thenReturn(List.of());

        Integer valor = contadorService.obtenerValorContador(user);

        assertEquals(0, valor);
        verify(contadorRepository, times(1)).save(any(Contador.class));
    }

    @Test
    void guardarValorContador_UpdatesExistingContador() throws Exception {
        when(contadorRepository.getContadorByUsuarioId(1)).thenReturn(List.of(contador));

        contadorService.guardarValorContador(user, 5);

        assertEquals(15, contador.getContadorValor());
        verify(contadorRepository, times(1)).save(contador);
    }

    @Test
    void guardarValorContador_SetsContadorToZeroIfValueIsZero() throws Exception {
        when(contadorRepository.getContadorByUsuarioId(1)).thenReturn(List.of(contador));

        contadorService.guardarValorContador(user, 0);

        assertEquals(0, contador.getContadorValor());
        verify(contadorRepository, times(1)).save(contador);
    }

    @Test
    void guardarValorContador_ThrowsExceptionIfContadorNotFound() {
        when(contadorRepository.getContadorByUsuarioId(1)).thenReturn(List.of());

        Exception exception = assertThrows(Exception.class, () -> {
            contadorService.guardarValorContador(user, 5);
        });

        assertEquals("Contador no encontrado para usuario", exception.getMessage());
    }
}
