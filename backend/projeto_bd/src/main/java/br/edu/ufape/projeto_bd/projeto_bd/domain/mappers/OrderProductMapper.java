package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.OrderProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.OrderProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Order;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.OrderProduct;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderProductMapper {

    @Mapping(target = "order", source = "order")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "amount", source = "dto.amount")
    @Mapping(target = "salePrice", source = "dto.salePrice")
    OrderProduct toEntity(OrderProductRequestDTO dto, Order order, Product product);

    OrderProductResponseDTO toResponseDTO(OrderProduct entity);

    @Mapping(target = "amount", source = "dto.amount")
    @Mapping(target = "salePrice", source = "dto.salePrice")
    void updateEntityFromDTO(OrderProductRequestDTO dto, @MappingTarget OrderProduct entity);
}
