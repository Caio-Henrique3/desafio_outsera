package br.com.caio.desafio_tecnico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caio.desafio_tecnico.domain.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Optional<Producer> findByNameIgnoreCase(String name);

}
