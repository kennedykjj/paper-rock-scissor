package com.acme.rockpaperscissors.service;

import com.acme.rockpaperscissors.domain.Game;
import com.acme.rockpaperscissors.domain.Scoreboard;
import com.acme.rockpaperscissors.domain.enums.GameStatus;
import com.acme.rockpaperscissors.domain.enums.Move;
import com.acme.rockpaperscissors.fixture.GameFixture;
import com.acme.rockpaperscissors.repository.GameRepository;
import com.acme.rockpaperscissors.repository.ScoreboardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GameServiceTest {

    GameRepository gameRepositoryMock = mock(GameRepository.class);
    ScoreboardRepository scoreboardRepositoryMock = mock(ScoreboardRepository.class);

    GameService service = new GameService(gameRepositoryMock, scoreboardRepositoryMock);

    @Test
    void shouldStartAGame() {
        Game gameMock = GameFixture.aGame().status(GameStatus.IN_PROGRESS).build();
        Scoreboard scoreboardMock = new Scoreboard(null, "1", "1", 100.0);
        when(gameRepositoryMock.save(any())).thenReturn(Mono.just(gameMock));
        when(scoreboardRepositoryMock.save(any())).thenReturn(Mono.just(scoreboardMock));
        when(gameRepositoryMock.findByPlayerIdAndStatus(any(), any())).thenReturn(Mono.empty());


        Game game = service.startGame("1").block();

        assertNotNull(game);
        Assertions.assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
    }

    @Test
    void ShouldGetAStartedAGameAndPlayOneTime() {
        Game gameMock = GameFixture.aGame().status(GameStatus.IN_PROGRESS).build();
        when(gameRepositoryMock.findByPlayerIdAndStatus("1", GameStatus.IN_PROGRESS)).thenReturn(Mono.just(gameMock));
        when(gameRepositoryMock.save(any())).thenReturn(Mono.just(gameMock));

        Game game = service.playGame(Move.PAPER, "1").block();

        assertNotNull(game);
        Assertions.assertEquals(Move.PAPER, game.getRounds().getFirst().getPlayerMove());
    }

    @Test
    void shouldNotFindAGameAndReturnError(){
        when(gameRepositoryMock.findByPlayerIdAndStatus("1", GameStatus.IN_PROGRESS)).thenReturn(Mono.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            service.playGame(Move.PAPER, "1").block();
            });

        assertNotNull(exception);
        Assertions.assertEquals("Game not started", exception.getMessage());
    }

    @Test
    void shouldShowGameHistoryByPlayer() {
        Game gameMock = GameFixture.aGame().status(GameStatus.IN_PROGRESS).build();
        when(gameRepositoryMock.findByPlayerId("1")).thenReturn(Flux.just(gameMock,
                GameFixture.aGame().status(GameStatus.FINISHED).build()));

        List<Game> game = service.showGameHistoryByPlayer("1").collectList().block();

        assertNotNull(game);
        Assertions.assertEquals(GameStatus.FINISHED, game.getFirst().getStatus());
        Assertions.assertEquals(GameStatus.IN_PROGRESS, game.getLast().getStatus());
    }

    @Test
    void shouldEndGame() {
        Game gameMock = GameFixture.aGame().status(GameStatus.IN_PROGRESS).build();
        Scoreboard scoreboardMock = new Scoreboard(null, "1", "1", 100.0);
        when(gameRepositoryMock.findById("1")).thenReturn(Mono.just(gameMock));
        when(gameRepositoryMock.save(any())).thenReturn(Mono.just(gameMock));
        when(scoreboardRepositoryMock.save(any())).thenReturn(Mono.just(scoreboardMock));

        Game game = service.endGame("1").block();

        assertNotNull(game);
        Assertions.assertEquals(GameStatus.FINISHED, game.getStatus());
    }
}