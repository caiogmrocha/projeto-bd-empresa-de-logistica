package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
