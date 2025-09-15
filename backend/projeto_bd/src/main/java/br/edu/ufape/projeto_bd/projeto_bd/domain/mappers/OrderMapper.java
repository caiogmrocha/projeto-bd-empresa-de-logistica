package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper{

    // Mapear de DTO de requisição para entidade
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    // Mapear de entidade para DTO de resposta
    OrderResponseDTO toDTO(Order entity);

    // Atualizar uma entidade existente a partir do DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateOrderFromDto(OrderRequestDTO dto, @MappingTarget Order entity);
}
