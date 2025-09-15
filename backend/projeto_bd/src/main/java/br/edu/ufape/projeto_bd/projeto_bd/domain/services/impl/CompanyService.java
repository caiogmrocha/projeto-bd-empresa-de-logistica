package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Company;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.AttributeAlreadyInUseException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.CompanyMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.CompanyRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ICompanyService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO) {
        if (companyRepository.existsByCnpj(companyRequestDTO.getCnpj())) {
            throw new AttributeAlreadyInUseException("CNPJ", Company.class);
        }
        Company company = companyMapper.toEntity(companyRequestDTO);
        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id) {
        return companyMapper.toDTO(companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Company.class, id)));
    }

    @Override
    public Page<CompanyResponseDTO> getAllCompanies(int page, int size, String sortBy, String direction,
            String tradeName) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Company> result;
        if (tradeName != null && !tradeName.isEmpty()) {
            result = companyRepository.findByTradeNameContainingIgnoreCase(tradeName, pageable);
        } else {
            result = companyRepository.findAll(pageable);
        }

        return result.map(companyMapper::toDTO);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyPatchDTO companyPatchDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Company.class, id));

        companyMapper.updateEntityFromDTO(companyPatchDTO, company);

        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException(Company.class, id);
        }
        companyRepository.deleteById(id);
    }

}