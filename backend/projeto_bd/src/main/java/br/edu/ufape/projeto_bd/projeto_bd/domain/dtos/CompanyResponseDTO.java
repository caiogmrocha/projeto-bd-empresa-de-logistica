package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponseDTO {
    private Long id;
    private String legalName;
    private String tradeName;
    private String cnpj;
    private List<String> phones;
    private List<String> emails;
    private AddressDTO address;
    private LocalDateTime createdAt;
}