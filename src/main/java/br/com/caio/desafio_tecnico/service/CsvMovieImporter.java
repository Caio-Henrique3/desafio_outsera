package br.com.caio.desafio_tecnico.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import br.com.caio.desafio_tecnico.domain.Movie;
import br.com.caio.desafio_tecnico.domain.Producer;
import br.com.caio.desafio_tecnico.domain.Studio;
import br.com.caio.desafio_tecnico.repository.MovieRepository;
import br.com.caio.desafio_tecnico.repository.ProducerRepository;
import br.com.caio.desafio_tecnico.repository.StudioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvMovieImporter {

    private static final Logger logger = LoggerFactory.getLogger(CsvMovieImporter.class);

    private final MovieRepository movieRepository;
    private final StudioRepository studioRepository;
    private final ProducerRepository producerRepository;

    public void importMoviesFromResource(String resourcePath) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                logger.warn("CSV vazio: {}", resourcePath);

                return;
            }

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(";", -1);
                if (columns.length < 5) {
                    logger.warn("Linha ignorada por formato inválido: {}", line);

                    continue;
                }

                Integer year = parseYear(columns[0]);
                if (year == null) {
                    logger.warn("Ano inválido na linha: {}", line);

                    continue;
                }

                movieRepository.save(
                        Movie.builder()
                                .year(year)
                                .title(columns[1].trim())
                                .studios(resolveStudios(columns[2]))
                                .producers(resolveProducers(columns[3]))
                                .winner("yes".equalsIgnoreCase(columns[4].trim()))
                                .build()
                );
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao ler CSV: " + resourcePath, ex);
        }
    }

    private Integer parseYear(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Set<Producer> resolveProducers(String raw) {
        Set<Producer> producers = new LinkedHashSet<>();
        for (String name : splitNames(raw)) {
            Producer producer = producerRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> producerRepository.save(new Producer(name)));
            producers.add(producer);
        }

        return producers;
    }

    private Set<Studio> resolveStudios(String raw) {
        Set<Studio> studios = new LinkedHashSet<>();
        for (String name : splitNames(raw)) {
            Studio studio = studioRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> studioRepository.save(new Studio(name)));
            studios.add(studio);
        }

        return studios;
    }

    private Set<String> splitNames(String raw) {
        Set<String> names = new LinkedHashSet<>();
        if (raw == null || raw.isBlank()) {
            return names;
        }

        String normalized = raw.replace(", and ", ",").replace(" and ", ",");
        for (String part : normalized.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                names.add(trimmed);
            }
        }

        return names;
    }

}
