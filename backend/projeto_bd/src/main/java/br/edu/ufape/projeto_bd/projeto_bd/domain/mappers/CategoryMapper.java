package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CategoryRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CategoryResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toResponseDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Category toEntity(CategoryRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateFromDto(CategoryRequestDTO requestDTO, @MappingTarget Category category);
}
