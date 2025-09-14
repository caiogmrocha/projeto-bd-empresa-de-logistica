package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO newProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {

        ProductResponseDTO product = productService.findProductById(id);
        return ResponseEntity.ok(product);

    }

    /**
     * Endpoint para listar todos os produtos de forma paginada.
     * Os parâmetros de paginação (page, size, sort) são passados na URL.
     * Ex: /products?page=0&size=10&sort=minimumSalePrice,desc
     * @param pageable Objeto que o Spring monta a partir dos parâmetros da URL.
     * @param search Palavra-chave opcional para filtrar (por id, status ou preço mínimo).
     * @return ResponseEntity com a página de produtos e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(Pageable pageable,
            @RequestParam(name = "search", required = false) String search) {

        Page<ProductResponseDTO> products = productService.findAllProducts(pageable, search);
        return ResponseEntity.ok(products);

    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
