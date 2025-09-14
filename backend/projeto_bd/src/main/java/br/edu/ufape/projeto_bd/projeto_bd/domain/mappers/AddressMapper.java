package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Address;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true) 
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Address toEntity(AddressDTO dto);
    
    AddressDTO toDTO(Address entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateAddressFromDto(AddressDTO addressDTO, @MappingTarget Address address);
}