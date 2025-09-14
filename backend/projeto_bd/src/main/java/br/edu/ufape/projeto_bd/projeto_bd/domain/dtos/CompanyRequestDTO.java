package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import java.util.List;
import org.hibernate.validator.constraints.br.CNPJ;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDTO {

    @NotBlank(message = "Razão social é obrigatória")
    private String legalName;

    @NotBlank(message = "Nome fantasia é obrigatório")
    private String tradeName;

    @NotBlank(message = "CNPJ é obrigatório")
    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    private List<@Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres") String> phones;

    private List<@Email(message = "Email inválido") @Size(max = 100, message = "Email deve ter no máximo 100 caracteres") String> emails;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private AddressDTO address;

}