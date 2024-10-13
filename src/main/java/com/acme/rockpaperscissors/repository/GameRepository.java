package com.acme.rockpaperscissors.repository;

import com.acme.rockpaperscissors.domain.Game;
import com.acme.rockpaperscissors.domain.enums.GameStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Mono<Game> findByPlayerIdAndStatus(String playerId, GameStatus status);

    Flux<Game> findByPlayerId(String playerId);
}
