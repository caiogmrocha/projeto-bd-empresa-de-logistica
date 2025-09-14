package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductStockRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductStockResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.ProductStock;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Warehouse;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, WarehouseMapper.class})
public interface ProductStockMapper {

    ProductStockResponseDTO toResponseDTO(ProductStock productStock);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(source = "request.code", target = "code")
    @Mapping(source = "request.amount", target = "amount")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "warehouse", target = "warehouse")
    ProductStock toEntity(ProductStockRequestDTO request, Product product, Warehouse warehouse);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(source = "request.code", target = "code")
    @Mapping(source = "request.amount", target = "amount")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "warehouse", target = "warehouse")
    void updateProductStockFromDto(ProductStockRequestDTO request, Product product, Warehouse warehouse, @MappingTarget ProductStock productStock);
    
}
