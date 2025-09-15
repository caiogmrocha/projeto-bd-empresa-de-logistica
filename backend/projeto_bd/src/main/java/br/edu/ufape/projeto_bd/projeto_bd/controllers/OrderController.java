package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO newOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.findOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint para listar todos os pedidos de forma paginada.
     * Os parâmetros de paginação (page, size, sort) são passados na URL.
     * Ex: /orders?page=0&size=10&sort=orderedAt,desc
     * @param pageable Objeto que o Spring monta a partir dos parâmetros da URL.
     * @return ResponseEntity com a página de pedidos e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.findAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
