package br.com.caio.desafio_tecnico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caio.desafio_tecnico.controller.dto.response.AwardsIntervalResponse;
import br.com.caio.desafio_tecnico.service.AwardIntervalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producers")
@Tag(name = "Producers", description = "Endpoints de produtores e intervalos de premios.")
public class AwardIntervalController {

    private final AwardIntervalService awardIntervalService;

    @GetMapping("/awards-intervals")
    @Operation(
            summary = "Obter intervalos de premios por produtor",
            description = "Retorna produtores com menor e maior intervalo entre premios consecutivos."
    )
    @ApiResponse(responseCode = "200", description = "Intervalos calculados com sucesso.")
    public ResponseEntity<AwardsIntervalResponse> getAwardsIntervals() {
        return ResponseEntity.ok(awardIntervalService.calculateAwardIntervals());
    }

}
