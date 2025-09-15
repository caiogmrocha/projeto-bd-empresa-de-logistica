package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findByTradeNameContainingIgnoreCase(String tradeName, Pageable pageable);
    boolean existsByCnpj(String cnpj);
}