package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDTO toResponseDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Product toEntity(ProductRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateProductFromDto(ProductRequestDTO requestDTO, @MappingTarget Product product);
}
