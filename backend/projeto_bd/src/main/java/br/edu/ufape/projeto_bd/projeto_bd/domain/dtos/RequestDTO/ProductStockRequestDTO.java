package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequestDTO {
    @NotBlank(message = "O código do estoque não pode estar em branco")
    @Size(max = 50, message = "O código do estoque não pode ultrapassar 50 caracteres")
    private String code;

    @NotNull(message = "A quantidade não pode ser nula")
    @PositiveOrZero(message = "A quantidade do estoque não pode ser negativa")
    private Long amount;

    @NotNull(message = "O ID do produto é obrigatório")
    private Long productId;

    @NotNull(message = "O ID do armazém é obrigatório")
    private Long warehouseId;
}
