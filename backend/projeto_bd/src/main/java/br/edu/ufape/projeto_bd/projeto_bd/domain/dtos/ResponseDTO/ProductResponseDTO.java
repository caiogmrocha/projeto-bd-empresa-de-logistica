package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private LocalDateTime warranty_date;
    private ProductStatus status;
    private BigDecimal minimumSalePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
