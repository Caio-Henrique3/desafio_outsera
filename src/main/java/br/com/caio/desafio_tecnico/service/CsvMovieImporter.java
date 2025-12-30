package br.com.caio.desafio_tecnico.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import br.com.caio.desafio_tecnico.domain.Movie;
import br.com.caio.desafio_tecnico.domain.Producer;
import br.com.caio.desafio_tecnico.domain.Studio;
import br.com.caio.desafio_tecnico.exception.BusinessException;
import br.com.caio.desafio_tecnico.exception.CsvFileNotFoundException;
import br.com.caio.desafio_tecnico.exception.CsvParsingException;
import br.com.caio.desafio_tecnico.repository.MovieRepository;
import br.com.caio.desafio_tecnico.repository.ProducerRepository;
import br.com.caio.desafio_tecnico.repository.StudioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvMovieImporter {

    private final MovieRepository movieRepository;
    private final StudioRepository studioRepository;
    private final ProducerRepository producerRepository;

    public void importMoviesFromResource(String resourcePath) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new CsvFileNotFoundException("CSV nao encontrado: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                throw new CsvParsingException("CSV vazio: " + resourcePath);
            }

            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(";", -1);
                if (columns.length < 5) {
                    throw new CsvParsingException(
                            "Linha " + lineNumber + " invalida: esperado 5 colunas, recebido " + columns.length
                    );
                }

                Integer year = parseYear(columns[0]);
                if (year == null) {
                    throw new CsvParsingException("Ano invalido na linha " + lineNumber + ": " + columns[0]);
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
            throw new BusinessException("Falha ao ler CSV: " + resourcePath, ex);
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
