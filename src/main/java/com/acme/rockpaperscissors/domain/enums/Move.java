package com.acme.rockpaperscissors.domain.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public static Move randomMove() {
        return values()[ThreadLocalRandom.current().nextInt(values().length)];
    }

    public Result getResultAgainst(Move computerMove) {
        if (this == computerMove) {
            return Result.TIE;
        }
        if (this == ROCK && computerMove == SCISSORS) {
            return Result.PLAYER_WINS;
        }
        if (this == PAPER && computerMove == ROCK) {
            return Result.PLAYER_WINS;
        }
        if (this == SCISSORS && computerMove == PAPER) {
            return Result.PLAYER_WINS;
        }
        return Result.COMPUTER_WINS;
    }
}
