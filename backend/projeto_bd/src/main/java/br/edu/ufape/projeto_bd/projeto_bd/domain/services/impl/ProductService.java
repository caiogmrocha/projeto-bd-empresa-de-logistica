package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.ProductStatus;
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
    public Page<ProductResponseDTO> findAllProducts(Pageable pageable, String search) {
        Page<Product> productPage;
        if (search == null || search.trim().isEmpty()) {
            productPage = productRepository.findAll(pageable);
        } else {
            String term = search.trim();
            Specification<Product> spec = buildSearchSpecification(term);
            productPage = productRepository.findAll(spec, pageable);
        }
        return productPage.map(productMapper::toResponseDTO);
    }

    private Specification<Product> buildSearchSpecification(String term) {
        return (root, query, cb) -> {
            // Try to parse numeric values
            Long idValue = null;
            BigDecimal priceValue = null;
            try { idValue = Long.parseLong(term); } catch (NumberFormatException ignored) {}
            try { priceValue = new BigDecimal(term); } catch (NumberFormatException ignored) {}

            // Try to match status by name (case-insensitive)
            ProductStatus statusValue = null;
            try { statusValue = ProductStatus.valueOf(term.toUpperCase()); } catch (IllegalArgumentException ignored) {}

            var predicates = cb.disjunction();
            if (idValue != null) {
                predicates.getExpressions().add(cb.equal(root.get("id"), idValue));
            }
            if (priceValue != null) {
                predicates.getExpressions().add(cb.equal(root.get("minimumSalePrice"), priceValue));
            }
            if (statusValue != null) {
                predicates.getExpressions().add(cb.equal(root.get("status"), statusValue));
            }
            // If nothing parsable, try a very loose match converting to string for status only
            // (other fields are numeric/date and not suited for LIKE here)
            if (predicates.getExpressions().isEmpty()) {
                predicates.getExpressions().add(cb.like(cb.lower(cb.function("CAST", String.class, root.get("status"))), "%" + term.toLowerCase() + "%"));
            }
            return predicates;
        };
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
