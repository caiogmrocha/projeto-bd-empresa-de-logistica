package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Order;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.OrderProduct;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.OrderProductId;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.OrderProductMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.OrderProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.OrderRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IOrderProduct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProductService implements IOrderProduct {

    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductMapper orderProductMapper;

    @Override
    @Transactional
    public OrderProductResponseDTO createOrderProduct(OrderProductRequestDTO request) {
        Order order = findOrderByIdOrThrow(request.getOrderId());
        Product product = findProductByIdOrThrow(request.getProductId());

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setAmount(Long.valueOf(request.getAmount()));
        orderProduct.setSalePrice(BigDecimal.valueOf(request.getSalePrice()));

        OrderProduct saved = orderProductRepository.save(orderProduct);
        return orderProductMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderProductResponseDTO findOrderProductById(Long orderId, Long productId) {
        OrderProductId id = new OrderProductId(orderId, productId);
        OrderProduct orderProduct = orderProductRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(OrderProduct.class, id));
        return orderProductMapper.toResponseDTO(orderProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderProductResponseDTO> findAllOrderProducts(Pageable pageable) {
        Page<OrderProduct> page = orderProductRepository.findAll(pageable);
        return page.map(orderProductMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public OrderProductResponseDTO updateOrderProduct(Long orderId, Long productId, OrderProductRequestDTO request) {
        OrderProductId id = new OrderProductId(orderId, productId);
        OrderProduct existing = orderProductRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(OrderProduct.class, id));

        existing.setAmount(Long.valueOf(request.getAmount()));
        existing.setSalePrice(BigDecimal.valueOf(request.getSalePrice()));

        OrderProduct updated = orderProductRepository.save(existing);
        return orderProductMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteOrderProduct(Long orderId, Long productId) {
        OrderProductId id = new OrderProductId(orderId, productId);
        OrderProduct existing = orderProductRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(OrderProduct.class, id));

        orderProductRepository.delete(existing);
    }

    // --------- métodos privados ---------

    private Order findOrderByIdOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Order.class, id));
    }

    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));
    }
}
