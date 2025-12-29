package br.com.caio.desafio_tecnico.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.caio.desafio_tecnico.domain.Movie;
import br.com.caio.desafio_tecnico.domain.Producer;
import br.com.caio.desafio_tecnico.domain.dto.AwardInterval;
import br.com.caio.desafio_tecnico.controller.dto.response.AwardsIntervalResponse;
import br.com.caio.desafio_tecnico.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwardIntervalService {

    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public AwardsIntervalResponse calculateAwardIntervals() {
        List<Movie> winners = movieRepository.findAllByWinnerTrueOrderByYearAsc();
        if (winners.isEmpty()) {
            return new AwardsIntervalResponse(List.of(), List.of());
        }

        Map<String, List<Integer>> winsByProducer = new HashMap<>();
        for (Movie movie : winners) {
            Integer year = movie.getYear();
            if (year == null) {
                continue;
            }

            for (Producer producer : movie.getProducers()) {
                winsByProducer
                        .computeIfAbsent(producer.getName(), key -> new ArrayList<>())
                        .add(year);
            }
        }

        List<AwardInterval> intervals = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : winsByProducer.entrySet()) {
            List<Integer> yearsSorted = entry.getValue().stream()
                    .distinct()
                    .sorted()
                    .toList();
            if (yearsSorted.size() < 2) {
                continue;
            }

            for (int i = 1; i < yearsSorted.size(); i++) {
                int previous = yearsSorted.get(i - 1);
                int following = yearsSorted.get(i);
                intervals.add(new AwardInterval(
                        entry.getKey(),
                        following - previous,
                        previous,
                        following
                ));
            }
        }

        if (intervals.isEmpty()) {
            return new AwardsIntervalResponse(List.of(), List.of());
        }

        int minInterval = intervals.stream()
                .mapToInt(AwardInterval::interval)
                .min()
                .orElse(0);
        int maxInterval = intervals.stream()
                .mapToInt(AwardInterval::interval)
                .max()
                .orElse(0);

        List<AwardInterval> min = intervals.stream()
                .filter(interval -> interval.interval() == minInterval)
                .toList();
        List<AwardInterval> max = intervals.stream()
                .filter(interval -> interval.interval() == maxInterval)
                .toList();

        return new AwardsIntervalResponse(min, max);
    }

}
