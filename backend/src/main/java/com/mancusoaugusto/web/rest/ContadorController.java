package com.mancusoaugusto.web.rest;


import com.auth0.jwt.JWT;
import com.mancusoaugusto.domain.User;
import com.mancusoaugusto.service.ContadorService;
import com.mancusoaugusto.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador de contador.
 */
@RestController
@RequestMapping("/api/contador")
public class ContadorController {

    private static final Logger LOG = LoggerFactory.getLogger(ContadorController.class);
    private ContadorService contadorService;

    private UserService userService;

    public ContadorController(
        ContadorService contadorService,
        UserService userService
    ) {
        this.contadorService = contadorService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Integer> getContador(
        @RequestHeader("Authorization") String token
    ) throws Exception {
        LOG.info("Obteniendo valor de contador");
        Optional<User> optUser = userService.getUserWithAuthoritiesByLogin(obtenerSubDeToken(token.substring(7)));
        if (optUser.isPresent()) {
            LOG.info("Usuario encontrado: " + optUser.get());
            return ResponseEntity.ok(
                this.contadorService.obtenerValorContador(optUser.get())
            );
        } else {
            return null;
        }
    }

    @PostMapping
    public ResponseEntity<Integer> incrementarContador(
        @RequestHeader("Authorization") String token,
        @RequestParam(value = "incremento", defaultValue = "1") Integer incremento
    ) throws Exception {
        Optional<User> optUser = userService.getUserWithAuthoritiesByLogin(obtenerSubDeToken(token.substring(7)));
        if (optUser.isPresent()) {
            this.contadorService.guardarValorContador(optUser.get(), incremento);
            return ResponseEntity.ok(200);
        } else {
            return null;
        }
    }


    private String obtenerSubDeToken(String jwt) {
        LOG.info("Obteniendo SUB de token :" + jwt);
        String sub =  JWT.decode(jwt).getSubject();
        LOG.info("SUB de token: " + sub);
        return sub;
    }
}
