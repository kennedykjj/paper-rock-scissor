package com.acme.rockpaperscissors.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Scoreboard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Player player;
    private Game game;

}
