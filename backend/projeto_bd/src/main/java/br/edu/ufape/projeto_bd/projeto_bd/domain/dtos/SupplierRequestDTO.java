package br.edu.ufape.projeto_bd.projeto_bd.domain.dtos;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.utils.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Tipo de fornecedor é obrigatório")
    @ValidEnum(enumClass = SupplierType.class, message = "Tipo de fornecedor deve ser NATURAL_PERSON ou LEGAL_ENTITY")
    private SupplierType supplierType; 

    @CPF(message = "CPF inválido")
    private String cpf;
    
    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private AddressDTO address;

    @AssertTrue(message = "Para pessoa física, CPF deve ser informado")
    public boolean isCpfValid() {
        if (supplierType == SupplierType.NATURAL_PERSON) {
            return cpf != null && !cpf.isBlank();
        }
        return true; 
    }

    @AssertTrue(message = "Para pessoa jurídica, CNPJ deve ser informado") 
    public boolean isCnpjValid() {
        if (supplierType == SupplierType.LEGAL_ENTITY) {
            return cnpj != null && !cnpj.isBlank();
        }
        return true; 
    }

    @AssertTrue(message = "Pessoa física não deve ter CNPJ e pessoa jurídica não deve ter CPF")
    public boolean isCpfCnpjMutuallyExclusive() {
        if (supplierType == SupplierType.NATURAL_PERSON) {
            return cnpj == null || cnpj.isBlank();
        } else if (supplierType == SupplierType.LEGAL_ENTITY) {
            return cpf == null || cpf.isBlank();
        }
        return true;
    }
}