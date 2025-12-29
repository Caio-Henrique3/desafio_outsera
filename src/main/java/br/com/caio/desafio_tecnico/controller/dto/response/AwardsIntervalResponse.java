package br.com.caio.desafio_tecnico.controller.dto.response;

import java.util.List;

import br.com.caio.desafio_tecnico.domain.dto.AwardInterval;

public record AwardsIntervalResponse(
        List<AwardInterval> min,
        List<AwardInterval> max
) {
}