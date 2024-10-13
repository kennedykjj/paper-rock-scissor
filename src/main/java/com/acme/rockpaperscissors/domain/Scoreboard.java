package com.acme.rockpaperscissors.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "scoreboard")
public class Scoreboard {
    @Id
    private String id;
    private String playerId;
    private String gameId;
    private Double winRate;
}
