package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;

@Repository
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    Optional<NaturalPerson> findByCpf(String cpf);
}
