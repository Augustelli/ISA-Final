package com.mancusoaugusto.service;

import com.mancusoaugusto.domain.Contador;
import com.mancusoaugusto.domain.User;
import com.mancusoaugusto.repository.ContadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContadorService {

    private static final Logger LOG = LoggerFactory.getLogger(ContadorService.class);

    private final ContadorRepository contadorRepository;

    public ContadorService(ContadorRepository contadorRepository) {
        this.contadorRepository = contadorRepository;
    }

    public Integer obtenerValorContador(User user) throws Exception {
        LOG.info("Obteniendo valor del contador de " + user.getLogin());
        Optional<Contador> optContador = obtenerContadorUsuario(user);
        if (optContador.isPresent()) {
            return optContador.get().getContadorValor();
        } else {
            this.contadorRepository.save(
                new Contador().contadorValor(0).usuarioId(Integer.parseInt(user.getId().toString()))
            );
            return 0;
        }
    }

    public void guardarValorContador(User user, Integer valor) throws Exception {
        LOG.info("Almacenando valor (" + valor.toString() + ") para usuario " + user.getLogin());
        Optional<Contador> optContador = obtenerContadorUsuario(user);
        if (optContador.isPresent()) {
            Contador nuevoContador = optContador.get();
            if (valor == 0){
                nuevoContador.setContadorValor( valor);
            }
            nuevoContador.setContadorValor( nuevoContador.getContadorValor() + valor);
            this.contadorRepository.save(nuevoContador);
        } else {
            throw new Exception("Contador no encontrado para usuario");
        }
    }

    private Optional<Contador> obtenerContadorUsuario(User user) {
        LOG.info("Obteniendo contador de usuario " + user.getLogin());
        List<Contador> contadores = this.contadorRepository.getContadorByUsuarioId(Integer.parseInt(user.getId().toString()));
        if (contadores.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(contadores.get(0));
    }
}
