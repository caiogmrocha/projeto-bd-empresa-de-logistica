package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import java.util.List;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;

public interface ISupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO request);

    SupplierResponseDTO findSupplierById(Long id);

    List<SupplierResponseDTO> findAllSuppliers();

    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO request);

    void deleteSupplier(Long id);
}