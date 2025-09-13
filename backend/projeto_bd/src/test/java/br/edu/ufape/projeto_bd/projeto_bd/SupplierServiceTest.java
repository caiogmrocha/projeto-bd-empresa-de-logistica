package br.edu.ufape.projeto_bd.projeto_bd;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Address;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Supplier;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.AddressMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.SupplierMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.SupplierRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.NaturalPersonRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.SupplierService;

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private NaturalPersonRepository naturalPersonRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNaturalPersonSupplier() {
        SupplierRequestDTO request = new SupplierRequestDTO();
        request.setSupplierType(SupplierType.NATURAL_PERSON);
        request.setName("John Doe");
        request.setCpf("00025426095");
        request.setAddress(new AddressDTO());

        Address mockAddress = new Address();
        NaturalPerson mockSupplier = new NaturalPerson();
        mockSupplier.setName("John Doe");
        mockSupplier.setCpf("00025426095");

        SupplierResponseDTO expectedResponse = new SupplierResponseDTO();
        expectedResponse.setName("John Doe");
        expectedResponse.setCpf("00025426095");

        when(addressMapper.toEntity(any())).thenReturn(mockAddress);
        when(supplierRepository.save(any(NaturalPerson.class))).thenReturn(mockSupplier);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(expectedResponse);
        when(naturalPersonRepository.existsByCpf(anyString())).thenReturn(false);

        SupplierResponseDTO result = supplierService.createSupplier(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("00025426095", result.getCpf());
        verify(supplierRepository).save(any(NaturalPerson.class));
    }

    @Test
    void findAllSuppliers_WithPagination() {
        List<Supplier> mockSuppliers = Arrays.asList(new NaturalPerson(), new LegalEntity());
        Page<Supplier> mockPage = new PageImpl<>(mockSuppliers);

        when(supplierRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(new SupplierResponseDTO());

        Page<SupplierResponseDTO> result = supplierService.findAllSuppliers(0, 10, "id", "asc", null);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void findSupplierById_WhenExists() {
        Long id = 1L;
        Supplier mockSupplier = new NaturalPerson();
        SupplierResponseDTO mockResponse = new SupplierResponseDTO();

        when(supplierRepository.findById(id)).thenReturn(Optional.of(mockSupplier));
        when(supplierMapper.toDTO(mockSupplier)).thenReturn(mockResponse);

        SupplierResponseDTO result = supplierService.findSupplierById(id);

        assertNotNull(result);
    }

    @Test
    void findSupplierById_WhenNotExists() {
        Long id = 1L;
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> supplierService.findSupplierById(id));
    }

    @Test
    void updateSupplier_WithPatchDTO() {
        Long id = 1L;
        SupplierPatchDTO patch = new SupplierPatchDTO();
        patch.setName("Updated Name");
        patch.setAddress(new AddressDTO());

        NaturalPerson existingSupplier = new NaturalPerson();
        existingSupplier.setAddress(new Address());

        SupplierResponseDTO expectedResponse = new SupplierResponseDTO();
        expectedResponse.setName("Updated Name");

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existingSupplier));
        when(addressMapper.toEntity(any())).thenReturn(new Address());
        when(supplierRepository.save(any(Supplier.class))).thenReturn(existingSupplier);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(expectedResponse);

        SupplierResponseDTO result = supplierService.updateSupplier(id, patch);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(supplierRepository).save(any());
    }

    @Test
    void deleteSupplier_WhenExists() {
        Long id = 1L;
        when(supplierRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> supplierService.deleteSupplier(id));
        verify(supplierRepository).deleteById(id);
    }

    @Test
    void deleteSupplier_WhenNotExists() {
        Long id = 1L;
        when(supplierRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> supplierService.deleteSupplier(id));
    }
}
