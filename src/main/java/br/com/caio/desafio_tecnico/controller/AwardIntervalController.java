package br.com.caio.desafio_tecnico.controller;

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
public class AwardIntervalController {

    private final AwardIntervalService awardIntervalService;

    @GetMapping("/awards-intervals")
    public ResponseEntity<AwardsIntervalResponse> getAwardsIntervals() {
        return ResponseEntity.ok(awardIntervalService.calculateAwardIntervals());
    }

}

