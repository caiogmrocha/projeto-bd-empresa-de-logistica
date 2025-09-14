package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;

public interface IProductService {
    public ProductResponseDTO createProduct (ProductRequestDTO request);

    public ProductResponseDTO findProductById(Long id);

    Page<ProductResponseDTO> findAllProducts(Pageable pageable);

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request);

    public void deleteProduct(Long id);

}
