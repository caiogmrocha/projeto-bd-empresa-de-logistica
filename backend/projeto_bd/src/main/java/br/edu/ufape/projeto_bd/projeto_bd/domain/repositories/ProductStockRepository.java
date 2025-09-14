package br.edu.ufape.projeto_bd.projeto_bd.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);

    boolean existsByCode(String code);
    
}
