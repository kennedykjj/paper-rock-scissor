package com.acme.rockpaperscissors.repository;

import com.acme.rockpaperscissors.domain.Scoreboard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface ScoreboardRepository extends ReactiveMongoRepository<Scoreboard, String> {
}
