package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyPatchDTO {
    private String legalName;
    private String tradeName;
    
    private List<@Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres") String> phones;

    private List<@Email(message = "Email inválido") @Size(max = 100, message = "Email deve ter no máximo 100 caracteres") String> emails;

    @Valid
    private AddressDTO address;
}