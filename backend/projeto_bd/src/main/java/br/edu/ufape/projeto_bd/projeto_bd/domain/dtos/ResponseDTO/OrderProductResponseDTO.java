package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import lombok.Data;

@Data
public class OrderProductResponseDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer amount;
    private Double salePrice;
}
