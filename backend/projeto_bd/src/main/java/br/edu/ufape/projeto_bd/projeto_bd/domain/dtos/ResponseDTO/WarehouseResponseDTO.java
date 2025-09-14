package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.time.LocalDateTime;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDTO {
    private Long id;
    private String name;
    private AddressDTO address; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
