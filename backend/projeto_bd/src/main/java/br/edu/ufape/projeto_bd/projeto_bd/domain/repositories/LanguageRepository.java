package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByIsoCode(String isoCode);
}
