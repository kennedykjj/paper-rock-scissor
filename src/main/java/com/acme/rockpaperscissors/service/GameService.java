package com.acme.rockpaperscissors.service;

import com.acme.rockpaperscissors.domain.Game;
import com.acme.rockpaperscissors.domain.Round;
import com.acme.rockpaperscissors.domain.Scoreboard;
import com.acme.rockpaperscissors.domain.enums.GameStatus;
import com.acme.rockpaperscissors.domain.enums.Move;
import com.acme.rockpaperscissors.domain.enums.Result;
import com.acme.rockpaperscissors.repository.GameRepository;
import com.acme.rockpaperscissors.repository.ScoreboardRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final ScoreboardRepository scoreboardRepository;

    public GameService(GameRepository gameRepository, ScoreboardRepository scoreboardRepository) {
        this.gameRepository = gameRepository;
        this.scoreboardRepository = scoreboardRepository;
    }

    public Mono<Game> playGame(Move playerMove, String playerId) {
        return gameRepository.findByPlayerIdAndStatus(playerId, GameStatus.IN_PROGRESS)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not started")))
                .flatMap(game -> {
                    playRound(game, playerMove);
                    return gameRepository.save(game);
                });
    }

    public Mono<Game> startGame(String playerId) {
        return hasGameInProgress(playerId)
                .flatMap(hasGame -> {
                    if (hasGame) {
                        return Mono.error(new RuntimeException("There is already a game in progress for this player."));
                    } else {
                        Game newGame = new Game(null,
                                playerId,
                                GameStatus.IN_PROGRESS,
                                new ArrayList<>(),
                                LocalDateTime.now(),
                                null);
                        return gameRepository.save(newGame);
                    }
                });
    }

    public Mono<Boolean> hasGameInProgress(String playerId) {
        return gameRepository.findByPlayerIdAndStatus(playerId, GameStatus.IN_PROGRESS)
                .hasElement();
    }

    public Mono<Game> endGame(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found")))
                .flatMap(this::finishGame)
                .flatMap(this::saveScoreboard);
    }

    private Mono<Game> finishGame(Game game) {
        game.setStatus(GameStatus.FINISHED);
        game.setEndTime(LocalDateTime.now());
        return gameRepository.save(game);
    }

    private Mono<Game> saveScoreboard(Game game) {
        Scoreboard scoreboard = new Scoreboard(
                null,
                game.getPlayerId(),
                game.getId(),
                calcWinRate(game)
        );
        return scoreboardRepository.save(scoreboard)
                .thenReturn(game);
    }

    private Double calcWinRate(Game game) {
        long wins = game.getRounds().stream()
                .filter(round -> round.getResult().equals(Result.PLAYER_WINS))
                .count();
        long totalRounds = game.getRounds().size();
        return (double) wins / totalRounds;
    }

    private Game playRound(Game game, Move playerMove) {
        Move computerMove = computerMove(game);
        Result result = playerMove.getResultAgainst(computerMove);
        game.newRound(new Round(playerMove, computerMove, result, LocalDateTime.now()));
        return game;
    }

    private Move computerMove(Game game) {
        Move move = Move.randomMove();
        List<Move> previousMoves = game.getRounds().stream()
                .map(Round::getComputerMove)
                .toList();
        if (previousMoves.size() < 3) {
            return Move.randomMove();
        }
        Map<Move, Long> movesCount = previousMoves.stream()
                .collect(Collectors.groupingBy(move1 -> move1, Collectors.counting()));
        Move mostPlayedMove = Collections.max(movesCount.entrySet(), Map.Entry.comparingByValue()).getKey();
        move = getCounterMove(mostPlayedMove, move);
        return move;
    }

    private static Move getCounterMove(Move mostPlayedMove, Move move) {
        if (mostPlayedMove == Move.ROCK) {
            move = Move.PAPER;
        } else if (mostPlayedMove == Move.PAPER) {
            move = Move.SCISSORS;
        } else if (mostPlayedMove == Move.SCISSORS) {
            move = Move.ROCK;
        }
        return move;
    }

    public Flux<Game> showGameHistoryByPlayer(String playerId) {
        return gameRepository.findByPlayerId(playerId)
                .sort((game1, game2) -> game2.getStartTime().compareTo(game1.getStartTime()));
    }
}
