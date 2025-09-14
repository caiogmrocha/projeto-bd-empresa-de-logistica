package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
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
    @Transactional (readOnly = true)
    public List<SupplierResponseDTO> findAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .map(supplierMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO findSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Supplier.class, id));

        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO supplierRequest) {
        Supplier supplierToUpdate = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Supplier.class, id));

        if (supplierToUpdate instanceof NaturalPerson && supplierRequest.getCpf() != null) {
            NaturalPerson naturalPerson = (NaturalPerson) supplierToUpdate;
            if (!naturalPerson.getCpf().equals(supplierRequest.getCpf()) &&
                    naturalPersonRepository.existsByCpfAndIdNot(supplierRequest.getCpf(), id)) {
                throw new AttributeAlreadyInUseException("CPF", supplierRequest.getCpf(), NaturalPerson.class);
            }
        } else if (supplierToUpdate instanceof LegalEntity && supplierRequest.getCnpj() != null) {
            LegalEntity legalEntity = (LegalEntity) supplierToUpdate;
            if (!legalEntity.getCnpj().equals(supplierRequest.getCnpj()) &&
                    legalEntityRepository.existsByCnpjAndIdNot(supplierRequest.getCnpj(), id)) {
                throw new AttributeAlreadyInUseException("CNPJ", supplierRequest.getCnpj(), LegalEntity.class);
            }
        }

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