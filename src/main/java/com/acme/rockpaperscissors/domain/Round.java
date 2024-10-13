package com.acme.rockpaperscissors.domain;

import com.acme.rockpaperscissors.domain.enums.Move;
import com.acme.rockpaperscissors.domain.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Round {
    private Move playerMove;
    private Move computerMove;
    private Result result;
    private LocalDateTime date;
}