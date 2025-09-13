package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierPatchDTO {
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;

    @Valid
    private AddressDTO address;

}
