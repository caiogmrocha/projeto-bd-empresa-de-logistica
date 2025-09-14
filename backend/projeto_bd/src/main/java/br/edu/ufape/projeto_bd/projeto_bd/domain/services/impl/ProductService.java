package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Language;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.ProductTranslation;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.ProductTranslationId;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.ProductStatus;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.ProductMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.LanguageRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductTranslationRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IProductService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final LanguageRepository languageRepository;
    private final ProductTranslationRepository productTranslationRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        // Save translations
        upsertTranslations(savedProduct, request.getNames(), request.getDescriptions());

        return buildResponseWithTranslations(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        return buildResponseWithTranslations(product);
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
        return productPage.map(this::buildResponseWithTranslations);
    }

    private Specification<Product> buildSearchSpecification(String term) {
        return (root, query, cb) -> {
            Long idValue = null;
            BigDecimal priceValue = null;
            try { idValue = Long.parseLong(term); } catch (NumberFormatException ignored) {}
            try { priceValue = new BigDecimal(term); } catch (NumberFormatException ignored) {}

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

        // Delete all translations and recreate (ensures clean state and avoids PK conflicts)
        productTranslationRepository.deleteByProductId(updatedProduct.getId());
        // Ensure deletes hit the DB before inserts in the same transaction
        productTranslationRepository.flush();
        upsertTranslations(updatedProduct, request.getNames(), request.getDescriptions());

        return buildResponseWithTranslations(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
        productTranslationRepository.deleteByProductId(id);
        productRepository.delete(existingProduct);
    }

    private void upsertTranslations(Product product, Map<String, String> names, Map<String, String> descriptions) {
        if (names == null && descriptions == null) return;
        Map<String, String> n = names != null ? names : new HashMap<>();
        Map<String, String> d = descriptions != null ? descriptions : new HashMap<>();
        var keys = new java.util.HashSet<>(n.keySet());
        keys.addAll(d.keySet());
        for (String iso : keys) {
            String name = n.getOrDefault(iso, null);
            String desc = d.getOrDefault(iso, null);
            Language lang = languageRepository.findByIsoCode(iso)
                .orElseThrow(() -> new IllegalArgumentException("Idioma não encontrado: " + iso));
            ProductTranslation pt = new ProductTranslation();
            pt.setId(new ProductTranslationId(product.getId(), lang.getId()));
            pt.setProduct(product);
            pt.setLanguage(lang);
            pt.setName(name != null ? name : "");
            pt.setDescription(desc);
            productTranslationRepository.save(pt);
        }
    }

    private ProductResponseDTO buildResponseWithTranslations(Product product) {
        ProductResponseDTO dto = productMapper.toResponseDTO(product);
        List<ProductTranslation> trs = productTranslationRepository.findByProductId(product.getId());
        Map<String, String> names = new HashMap<>();
        Map<String, String> descriptions = new HashMap<>();
        for (ProductTranslation t : trs) {
            if (t.getLanguage() != null && t.getLanguage().getIsoCode() != null) {
                names.put(t.getLanguage().getIsoCode(), t.getName());
                descriptions.put(t.getLanguage().getIsoCode(), t.getDescription());
            }
        }
        dto.setNames(names);
        dto.setDescriptions(descriptions);
        return dto;
    }
}
