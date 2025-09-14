package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotBlank(message = "O país é obrigatório")
    @Size(max = 50, message = "O país não pode exceder 50 caracteres")
    private String country;

    @NotBlank(message = "O estado é obrigatório")
    @Size(max = 50, message = "O estado não pode exceder 50 caracteres")
    private String state;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 50, message = "A cidade não pode exceder 50 caracteres")
    private String city;

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 100, message = "A rua não pode exceder 100 caracteres")
    private String street;

    @NotBlank(message = "O número é obrigatório")
    @Size(max = 10, message = "O número não pode exceder 10 caracteres")
    private String number;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(max = 15, message = "O CEP não pode exceder 15 caracteres")
    private String zipCode;
}
