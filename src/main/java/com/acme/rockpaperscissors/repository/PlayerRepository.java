package com.acme.rockpaperscissors.repository;

import com.acme.rockpaperscissors.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {
    Mono<Player> findByUsername(String username);
}
