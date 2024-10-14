package com.acme.rockpaperscissors.controller;

import com.acme.rockpaperscissors.domain.Game;
import com.acme.rockpaperscissors.domain.enums.Move;
import com.acme.rockpaperscissors.service.GameService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public Mono<Game> startGame(@RequestParam String playerId) {
        return gameService.startGame(playerId);
    }

    @PostMapping("/end")
    public Mono<Game> endGame(@RequestParam String gameId) {
        return gameService.endGame(gameId);
    }

    @PostMapping("/play")
    public Mono<Game> playGame(@RequestParam Move playerMove, @RequestParam String playerId) {
        return gameService.playGame(playerMove, playerId);
    }

    @GetMapping("/history")
    public Flux<Game> showGamesByPlayer(@RequestParam String playerId) {
        return gameService.showGameHistoryByPlayer(playerId);
    }
}
