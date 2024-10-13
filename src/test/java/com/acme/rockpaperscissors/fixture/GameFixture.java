package com.acme.rockpaperscissors.fixture;


import com.acme.rockpaperscissors.domain.Game;
import com.acme.rockpaperscissors.domain.Round;
import com.acme.rockpaperscissors.domain.enums.GameStatus;
import com.acme.rockpaperscissors.domain.enums.Move;
import com.acme.rockpaperscissors.domain.enums.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameFixture {

    public static Game.GameBuilder aGame() {
        return Game.builder()
                .id("1")
                .playerId("player1")
                .status(GameStatus.IN_PROGRESS)
                .rounds(new ArrayList<>())
                .startTime(LocalDateTime.now())
                .endTime(null);
    }

    public static Round.RoundBuilder aRound() {
        return Round.builder()
                .playerMove(Move.ROCK)
                .computerMove(Move.SCISSORS)
                .result(Result.PLAYER_WINS);
    }

    public static Game aGameWithOneRound() {
        Game game = aGame().build();
        game.newRound(aRound().build());
        return game;
    }

    public static Game aCompletedGameWithMultipleRounds() {
        Game game = aGame()
                .status(GameStatus.FINISHED)
                .endTime(LocalDateTime.now())
                .build();
        game.newRound(aRound().build());
        game.newRound(aRound().playerMove(Move.PAPER).computerMove(Move.ROCK).result(Result.PLAYER_WINS).build());
        game.newRound(aRound().playerMove(Move.SCISSORS).computerMove(Move.SCISSORS).result(Result.TIE).build());
        return game;
    }
}