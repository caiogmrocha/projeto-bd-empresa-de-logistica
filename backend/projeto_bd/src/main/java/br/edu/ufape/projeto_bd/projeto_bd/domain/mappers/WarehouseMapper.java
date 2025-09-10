package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.WarehouseRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.WarehouseResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Warehouse;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface WarehouseMapper {

    WarehouseResponseDTO toResponseDTO(Warehouse warehouse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Warehouse toEntity(WarehouseRequestDTO requestDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateWarehouseFromDto(WarehouseRequestDTO requestDTO, @MappingTarget Warehouse warehouse);
    
}
