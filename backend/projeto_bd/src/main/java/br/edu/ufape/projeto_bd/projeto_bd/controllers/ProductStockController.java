package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductStockRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductStockResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.ProductStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("api/product-stocks")
@RequiredArgsConstructor
@Validated
public class ProductStockController {
    private final ProductStockService productStockService;


    @PostMapping("/create")
    public ResponseEntity<ProductStockResponseDTO> createProductStock(@Valid @RequestBody ProductStockRequestDTO request) {
        ProductStockResponseDTO newStock = productStockService.createProductStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStock);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductStockResponseDTO> getProductStockById(@PathVariable Long id) {
        ProductStockResponseDTO stock = productStockService.findProductStockById(id);
        return ResponseEntity.ok(stock);
    }


    @GetMapping
    public ResponseEntity<Page<ProductStockResponseDTO>> getAllProductStocks(Pageable pageable) {
        Page<ProductStockResponseDTO> stocks = productStockService.findAllProductStocks(pageable);
        return ResponseEntity.ok(stocks);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ProductStockResponseDTO> updateProductStock(@PathVariable Long id, @Valid @RequestBody ProductStockRequestDTO request) {
        ProductStockResponseDTO updatedStock = productStockService.updateProductStock(id, request);
        return ResponseEntity.ok(updatedStock);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProductStock(@PathVariable Long id) {
        productStockService.deleteProductStock(id);
        return ResponseEntity.noContent().build();
    }
}
