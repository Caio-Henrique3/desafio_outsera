package br.com.caio.desafio_tecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caio.desafio_tecnico.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
