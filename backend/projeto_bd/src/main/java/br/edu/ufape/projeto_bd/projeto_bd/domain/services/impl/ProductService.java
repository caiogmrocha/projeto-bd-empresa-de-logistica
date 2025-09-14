package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.ProductMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IProductService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllProducts(Pageable pageable) {
        
        Page<Product> productPage = productRepository.findAll(pageable);
        
        return productPage.map(productMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        
        productMapper.updateProductFromDto(request, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
    
        productRepository.delete(existingProduct);
    }
}
