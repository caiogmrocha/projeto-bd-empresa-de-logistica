package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

import java.time.LocalDateTime;

import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.OrderMethod;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.OrderStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "O método do pedido é obrigatório.")
    private OrderMethod orderMethod;

    @NotNull(message = "O status do pedido é obrigatório.")
    private OrderStatus orderStatus;

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long customerId;

    @NotNull(message = "A data do pedido é obrigatória.")
    private LocalDateTime orderedAt;

    @NotNull(message = "A data de entrega esperada é obrigatória.")
    @Future(message = "A data de entrega esperada deve ser no futuro.")
    private LocalDateTime expectedToDeliverAt;

}
