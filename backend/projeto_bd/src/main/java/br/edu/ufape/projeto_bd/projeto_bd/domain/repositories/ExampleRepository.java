package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Example;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Integer> {
}

