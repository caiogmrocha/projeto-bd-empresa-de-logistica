package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CustomerRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CustomerResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Customer;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CustomerMapper{

    CustomerResponseDTO toResponseDTO(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Customer toEntity(CustomerRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateCustomerFromDto(CustomerRequestDTO requestDTO, @MappingTarget Customer customer);
    
}