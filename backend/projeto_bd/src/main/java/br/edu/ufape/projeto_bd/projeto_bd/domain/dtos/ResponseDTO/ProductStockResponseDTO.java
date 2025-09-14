package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockResponseDTO {
    private Long id;
    private String code;
    private Long amount;
    private ProductResponseDTO product;
    private WarehouseResponseDTO warehouse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
