package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderProductResponseDTO;

public interface IOrderProduct{

    OrderProductResponseDTO createOrderProduct(OrderProductRequestDTO request);

    OrderProductResponseDTO findOrderProductById(Long orderId, Long productId);

    Page<OrderProductResponseDTO> findAllOrderProducts(Pageable pageable);

    OrderProductResponseDTO updateOrderProduct(Long orderId, Long productId, OrderProductRequestDTO request);

    void deleteOrderProduct(Long orderId, Long productId);
}
