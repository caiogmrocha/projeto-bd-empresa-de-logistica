package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequestDTO {
    @NotBlank(message = "O nome do armazém não pode estar em branco")
    @Size(max = 100, message = "O nome do armazém não pode ultrapassar 100 caracteres")
    private String name;

    @NotNull(message = "O endereço é obrigatório.")
    @Valid
    private AddressDTO address;
}
