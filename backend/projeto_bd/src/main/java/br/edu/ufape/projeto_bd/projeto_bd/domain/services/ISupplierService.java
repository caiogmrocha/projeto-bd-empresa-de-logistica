package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierPatchDTO;

public interface ISupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO request);

    SupplierResponseDTO findSupplierById(Long id);

    public Page<SupplierResponseDTO> findAllSuppliers(int page, int size, String sortBy, String direction, String name);

    SupplierResponseDTO updateSupplier(Long id, SupplierPatchDTO request);

    void deleteSupplier(Long id);
}