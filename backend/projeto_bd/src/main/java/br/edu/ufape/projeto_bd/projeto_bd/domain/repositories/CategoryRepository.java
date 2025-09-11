package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
