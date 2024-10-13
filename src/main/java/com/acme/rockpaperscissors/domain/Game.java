package com.acme.rockpaperscissors.domain;

import com.acme.rockpaperscissors.domain.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "game")
public class Game {
    @Id
    private String id;
    private String playerId;
    private GameStatus status;
    private ArrayList<Round> rounds = new ArrayList<>();
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void newRound(Round round) {
        rounds.add(round);
    }

}
