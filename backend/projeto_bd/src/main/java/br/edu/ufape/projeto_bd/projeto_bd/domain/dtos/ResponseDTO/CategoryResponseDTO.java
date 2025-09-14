package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
}
