package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private SupplierType supplierType;

    private String cpf;

    private String cnpj;

    @NotNull
    private AddressDTO address;
}