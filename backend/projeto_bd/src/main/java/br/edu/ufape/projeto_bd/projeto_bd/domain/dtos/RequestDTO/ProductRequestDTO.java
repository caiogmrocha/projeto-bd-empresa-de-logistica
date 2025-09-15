package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Set;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.ProductStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    @NotNull(message = "A data de garantia é obrigatória.")
    private LocalDateTime warranty_date;

    @NotNull(message = "O status do produto é obrigatório.")
    private ProductStatus status;

    @NotNull(message = "O preço mínimo de venda é obrigatório.")
    @PositiveOrZero(message = "O preço mínimo de venda não pode ser negativo.")
    private BigDecimal minimumSalePrice;

    // Map<ISO_CODE, value>
    private Map<String, String> names;
    private Map<String, String> descriptions;

    private List<Long> categoriesIds;
    private Set<Long> categoryIds;
}
