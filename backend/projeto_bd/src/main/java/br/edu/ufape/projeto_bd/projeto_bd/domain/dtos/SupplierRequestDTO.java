package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

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

    @CPF
    private String cpf;
    @CNPJ
    private String cnpj;

    @NotNull
    private AddressDTO address;
}