package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Address;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Supplier;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.AttributeAlreadyInUseException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.AddressMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.SupplierMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.LegalEntityRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.NaturalPersonRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.SupplierRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {

    private final SupplierRepository supplierRepository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final AddressMapper addressMapper;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierRequestDTO supplierRequest) {
        if (supplierRequest.getSupplierType() == SupplierType.NATURAL_PERSON) {
            if (supplierRequest.getCpf() != null &&
                    naturalPersonRepository.existsByCpf(supplierRequest.getCpf())) {
                throw new AttributeAlreadyInUseException("CPF", supplierRequest.getCpf(), NaturalPerson.class);
            }
        } else if (supplierRequest.getSupplierType() == SupplierType.LEGAL_ENTITY) {
            if (supplierRequest.getCnpj() != null &&
                    legalEntityRepository.existsByCnpj(supplierRequest.getCnpj())) {
                throw new AttributeAlreadyInUseException("CNPJ", supplierRequest.getCnpj(), LegalEntity.class);
            }
        }

        Supplier newSupplier;

        if (supplierRequest.getSupplierType() == SupplierType.NATURAL_PERSON) {
            NaturalPerson person = new NaturalPerson();
            person.setCpf(supplierRequest.getCpf());
            newSupplier = person;
        } else {
            LegalEntity entity = new LegalEntity();
            entity.setCnpj(supplierRequest.getCnpj());
            newSupplier = entity;
        }

        newSupplier.setName(supplierRequest.getName());
        newSupplier.setSupplierType(supplierRequest.getSupplierType());

        Address address = addressMapper.toEntity(supplierRequest.getAddress());
        newSupplier.setAddress(address);

        Supplier savedSupplier = supplierRepository.save(newSupplier);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    public Page<SupplierResponseDTO> findAllSuppliers(int page, int size, String sortBy, String direction,
            String name) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Supplier> result;

        if (name != null && !name.isEmpty()) {
            result = supplierRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            result = supplierRepository.findAll(pageable);
        }

        return result.map(supplier -> supplierMapper.toDTO(supplier));
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO findSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Supplier.class, id));

        return supplierMapper.toDTO(supplier);
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierPatchDTO supplierPatch) {
        Supplier supplierToUpdate = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Supplier.class, id));

        if (supplierPatch.getName() != null) {
            supplierToUpdate.setName(supplierPatch.getName());
        }

        if (supplierPatch.getAddress() != null) {
            Address updatedAddress = addressMapper.toEntity(supplierPatch.getAddress());
            updatedAddress.setId(supplierToUpdate.getAddress().getId());
            supplierToUpdate.setAddress(updatedAddress);
        }

        Supplier savedSupplier = supplierRepository.save(supplierToUpdate);
        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException(Supplier.class, id);
        }
        supplierRepository.deleteById(id);
    }

    public SupplierResponseDTO findNaturalPersonByCpf(String cpf) {
        return naturalPersonRepository.findByCpf(cpf)
                .map(supplierMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(NaturalPerson.class, "CPF", cpf));
    }

    public SupplierResponseDTO findLegalEntityByCnpj(String cnpj) {
        return legalEntityRepository.findByCnpj(cnpj)
                .map(supplierMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(LegalEntity.class, "CNPJ", cnpj));
    }

    public boolean existsNaturalPersonByCpf(String cpf) {
        return naturalPersonRepository.existsByCpf(cpf);
    }

    public boolean existsLegalEntityByCnpj(String cnpj) {
        return legalEntityRepository.existsByCnpj(cnpj);
    }
}