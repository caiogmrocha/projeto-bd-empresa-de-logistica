package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductStockRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductStockResponseDTO;

public interface IProductStockService {
    public ProductStockResponseDTO createProductStock (ProductStockRequestDTO request);

    public ProductStockResponseDTO findProductStockById(Long id);

    public Page<ProductStockResponseDTO> findAllProductStocks(Pageable pageable);

    public ProductStockResponseDTO updateProductStock(Long id, ProductStockRequestDTO request);

    public void deleteProductStock(Long id);
}
