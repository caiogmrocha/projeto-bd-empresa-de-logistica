package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.WarehouseRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.WarehouseResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Warehouse;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.WarehouseMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.WarehouseRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IWarehouseService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService implements IWarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    @Transactional
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO request) {

        Warehouse newWarehouse = warehouseMapper.toEntity(request);
        
        Warehouse savedWarehouse = warehouseRepository.save(newWarehouse);
        
        return warehouseMapper.toResponseDTO(savedWarehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponseDTO findWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Warehouse.class, id));
        
        return warehouseMapper.toResponseDTO(warehouse);
    }

    @Override
    @Transactional (readOnly = true)
    public Page<WarehouseResponseDTO> findAllWarehouses(Pageable pageable) {
        Page<Warehouse> warehousePage = warehouseRepository.findAll(pageable);

        return warehousePage.map(warehouseMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public WarehouseResponseDTO updateWarehouse(Long id, WarehouseRequestDTO request) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Warehouse.class, id));

        warehouseMapper.updateWarehouseFromDto(request, existingWarehouse);

        Warehouse updatedWarehouse = warehouseRepository.save(existingWarehouse);

        return warehouseMapper.toResponseDTO(updatedWarehouse);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Warehouse.class, id));

        warehouseRepository.delete(existingWarehouse);
    }
}