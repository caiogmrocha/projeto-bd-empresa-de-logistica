package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.PageResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.CompanyPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(@Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(companyRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<CompanyResponseDTO>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String tradeName) {
        Page<CompanyResponseDTO> companies = companyService.getAllCompanies(page, size, sortBy, direction, tradeName);
        return ResponseEntity.ok(new PageResponseDTO<>(companies));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable Long id,
            @Valid @RequestBody CompanyPatchDTO companyUpdateDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}