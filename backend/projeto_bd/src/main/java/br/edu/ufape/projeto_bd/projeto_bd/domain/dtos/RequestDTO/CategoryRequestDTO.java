package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO;

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
public class CategoryRequestDTO {
    @NotBlank(message = "O nome da categoria não pode estar em branco")
    @Size(max = 100)
    private String name;

    private String description;
}
