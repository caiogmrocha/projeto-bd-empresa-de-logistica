package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
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

    // Map<ISO_CODE, value>
    private Map<String, String> names;
    private Map<String, String> descriptions;
}
