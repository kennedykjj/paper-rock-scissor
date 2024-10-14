package com.acme.rockpaperscissors.controller;

import com.acme.rockpaperscissors.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService service;

    public PlayerController(PlayerService playerService ) {
        this.service = playerService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        return service.findByUsername(username)
                .flatMap(player -> Mono.just(ResponseEntity.badRequest().body("Username already exists.")))
                .switchIfEmpty(service.registerPlayer(username, password, email)
                        .map(registeredPlayer -> ResponseEntity.ok("Player registered successfully.")));
    }

}
