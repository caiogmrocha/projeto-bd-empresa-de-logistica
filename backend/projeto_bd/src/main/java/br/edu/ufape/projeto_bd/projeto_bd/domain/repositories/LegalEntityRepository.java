package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {
    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    Optional<LegalEntity> findByCnpj(String cnpj);
}
