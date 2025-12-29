package br.com.caio.desafio_tecnico.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.caio.desafio_tecnico.repository.MovieRepository;
import br.com.caio.desafio_tecnico.service.CsvMovieImporter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CsvMovieLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(CsvMovieLoader.class);

    private static final String CSV_PATH = "csv/movielist.csv";

    private final MovieRepository movieRepository;

    private final CsvMovieImporter importer;

    @Override
    public void run(ApplicationArguments args) {
        if (movieRepository.count() > 0) {
            logger.info("Import CSV ignorado: base já possui dados.");

            return;
        }

        importer.importMoviesFromResource(CSV_PATH);

        logger.info("Import CSV concluído.");
    }

}
