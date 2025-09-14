package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import java.time.LocalDateTime;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponseDTO {
    private Long id;
    private String name;
    private SupplierType supplierType;
    private String cpf;
    private String cnpj;
    private AddressDTO address;
    private LocalDateTime createdAt;
}