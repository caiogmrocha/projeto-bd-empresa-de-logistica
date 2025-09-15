package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IOrderProduct;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/orders-products")
@RequiredArgsConstructor
@Validated
public class OrderProductController {

    private final IOrderProduct orderProductService;

    @PostMapping("/create")
    public ResponseEntity<OrderProductResponseDTO> createOrderProduct(
            @Valid @RequestBody OrderProductRequestDTO request) {

        OrderProductResponseDTO response = orderProductService.createOrderProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderProductResponseDTO> getOrderProductById(
            @PathVariable Long orderId,
            @PathVariable Long productId) {

        OrderProductResponseDTO response = orderProductService.findOrderProductById(orderId, productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderProductResponseDTO>> getAllOrderProducts(Pageable pageable) {
        Page<OrderProductResponseDTO> page = orderProductService.findAllOrderProducts(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/update/{orderId}/{productId}")
    public ResponseEntity<OrderProductResponseDTO> updateOrderProduct(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @Valid @RequestBody OrderProductRequestDTO request) {

        OrderProductResponseDTO response = orderProductService.updateOrderProduct(orderId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{orderId}/{productId}")
    public ResponseEntity<Void> deleteOrderProduct(
            @PathVariable Long orderId,
            @PathVariable Long productId) {

        orderProductService.deleteOrderProduct(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}
