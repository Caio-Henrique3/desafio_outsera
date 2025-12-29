package br.com.caio.desafio_tecnico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caio.desafio_tecnico.domain.Studio;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    Optional<Studio> findByNameIgnoreCase(String name);

}
