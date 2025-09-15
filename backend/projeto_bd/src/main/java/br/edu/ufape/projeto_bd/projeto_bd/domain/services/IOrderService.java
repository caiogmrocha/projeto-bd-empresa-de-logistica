package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderResponseDTO;


public interface IOrderService {
    public OrderResponseDTO createOrder (OrderRequestDTO request);

    public OrderResponseDTO findOrderById(Long id);

    Page<OrderResponseDTO> findAllOrders(Pageable pageable);

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO request);

    public void deleteOrder(Long id);
}
