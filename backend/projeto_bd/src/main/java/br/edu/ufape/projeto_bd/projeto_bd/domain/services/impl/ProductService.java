package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Category;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.ProductMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.CategoryRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IProductService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = productMapper.toEntity(request);

        Set<Category> categories = findCategoriesByIds(request.getCategoryIds());

        product.setCategories(categories);

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

        Set<Category> categories = findCategoriesByIds(request.getCategoryIds());
        existingProduct.setCategories(categories);

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


    private Set<Category> findCategoriesByIds(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Collections.emptySet();
        }
        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new EntityNotFoundException(Category.class, categoryId)))
                .collect(Collectors.toSet());
    }
}
