package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.WarehouseRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.WarehouseResponseDTO;

public interface IWarehouseService {
    public WarehouseResponseDTO createWarehouse (WarehouseRequestDTO request);

    public WarehouseResponseDTO findWarehouseById(Long id);

    public Page<WarehouseResponseDTO> findAllWarehouses(String name, Pageable pageable);

    public WarehouseResponseDTO updateWarehouse(Long id, WarehouseRequestDTO request);

    public void deleteWarehouse(Long id);

}
