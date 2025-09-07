package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Address;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Supplier;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.AddressMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.SupplierMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.SupplierRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ISupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {
    
    private final SupplierRepository supplierRepository;
    private final AddressMapper addressMapper;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponseDTO createSupplier(SupplierRequestDTO supplierRequest) {
        Supplier newSupplier;
        
        if (supplierRequest.getSupplierType() == SupplierType.NATURAL_PERSON) {
            NaturalPerson person = new NaturalPerson();
            person.setCpf(supplierRequest.getCpf());
            newSupplier = person;
        } else if (supplierRequest.getSupplierType() == SupplierType.LEGAL_ENTITY) {
            LegalEntity entity = new LegalEntity();
            entity.setCnpj(supplierRequest.getCnpj());
            newSupplier = entity;
        } else {
            throw new IllegalArgumentException("Tipo de fornecedor inválido.");
        }
        
        newSupplier.setName(supplierRequest.getName());
        newSupplier.setSupplierType(supplierRequest.getSupplierType());
        
        Address address = addressMapper.toEntity(supplierRequest.getAddress());
        newSupplier.setAddress(address);
        
        Supplier savedSupplier = supplierRepository.save(newSupplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    public List<SupplierResponseDTO> findAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierResponseDTO findSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));
        
        return supplierMapper.toDTO(supplier);
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO supplierRequest) {
        Supplier supplierToUpdate = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));
        
        supplierToUpdate.setName(supplierRequest.getName());
        
        Address updatedAddress = addressMapper.toEntity(supplierRequest.getAddress());
        updatedAddress.setId(supplierToUpdate.getAddress().getId());
        supplierToUpdate.setAddress(updatedAddress);
        
        if (supplierToUpdate instanceof NaturalPerson && supplierRequest.getCpf() != null) {
            ((NaturalPerson) supplierToUpdate).setCpf(supplierRequest.getCpf());
        } else if (supplierToUpdate instanceof LegalEntity && supplierRequest.getCnpj() != null) {
            ((LegalEntity) supplierToUpdate).setCnpj(supplierRequest.getCnpj());
        }
        
        Supplier savedSupplier = supplierRepository.save(supplierToUpdate);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Fornecedor não encontrado");
        }
        supplierRepository.deleteById(id);
    }
}