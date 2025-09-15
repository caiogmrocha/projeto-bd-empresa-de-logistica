package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

}