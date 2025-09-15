package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Company;

@Mapper(componentModel = "spring", uses = { AddressMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    CompanyResponseDTO toDTO(Company company);

    Company toEntity(CompanyRequestDTO companyRequestDTO);

    void updateEntityFromDTO(CompanyPatchDTO companyPatchDTO, @MappingTarget Company company);

}