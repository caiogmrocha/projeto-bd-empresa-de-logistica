package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyPatchDTO;

public interface ICompanyService {
    CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO);

    CompanyResponseDTO getCompanyById(Long id);

    Page<CompanyResponseDTO> getAllCompanies(int page, int size, String sortBy, String direction, String tradeName);

    CompanyResponseDTO updateCompany(Long id, CompanyPatchDTO companyPatchDTO);

    void deleteCompany(Long id);
}