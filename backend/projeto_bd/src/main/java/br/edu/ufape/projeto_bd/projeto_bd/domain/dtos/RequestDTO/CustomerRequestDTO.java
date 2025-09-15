package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import jakarta.validation.Valid;
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
public class CustomerRequestDTO {
    @NotBlank(message = "O nome do cliente não pode estar em branco")
    @Size(max = 100, message = "O nome do cliente não pode ultrapassar 100 caracteres")  
	private String name;
	  
    @NotNull(message = "O preço limite de crédito é obrigatório.")
    @PositiveOrZero(message = "O limite não pode ser negativo.")
    private BigDecimal creditLimit;
    
    @NotNull(message = "O endereço é obrigatório.")
    @Valid
    private AddressDTO addresses;
}
