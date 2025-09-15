package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Customer;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
}
