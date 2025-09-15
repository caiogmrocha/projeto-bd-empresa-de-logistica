package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
	  private Long id;
	  private String name;
	  private AddressDTO adress;
	  private BigDecimal creditLimit;
	  private LocalDateTime createdAt;
	  private LocalDateTime updatedAt;
}
//devo retornar o updatedAt?
