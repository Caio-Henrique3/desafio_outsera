package br.com.caio.desafio_tecnico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caio.desafio_tecnico.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = "producers")
    List<Movie> findAllByWinnerTrueOrderByYearAsc();

}
