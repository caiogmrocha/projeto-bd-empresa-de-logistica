package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.time.LocalDateTime;

import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.OrderMethod;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;
    private OrderMethod orderMethod;
    private OrderStatus orderStatus;
    private Long customerId;
    private LocalDateTime orderedAt;
    private LocalDateTime expectedToDeliverAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
