package com.acme.rockpaperscissors.service;

import com.acme.rockpaperscissors.domain.Player;
import com.acme.rockpaperscissors.repository.PlayerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {
    private final PlayerRepository repo;
    private final PasswordEncoder passwordEncoder;


    public PlayerService(PlayerRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Player> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Mono<Object> registerPlayer(String username, String password, String email){
        return repo.findByUsername(username)
                .flatMap(player -> Mono.error(new IllegalArgumentException("UserName already taken")))
                .switchIfEmpty(repo.save(new Player(null, username, email, passwordEncoder.encode(password))));

    }


}
