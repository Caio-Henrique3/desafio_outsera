package br.com.caio.desafio_tecnico.domain.dto;

public record AwardInterval(
        String producer,
        int interval,
        int previousWin,
        int followingWin
) {
}
