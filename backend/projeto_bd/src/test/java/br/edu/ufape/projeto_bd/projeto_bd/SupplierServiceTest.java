package br.edu.ufape.projeto_bd.projeto_bd;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Address;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Supplier;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.AddressMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.SupplierMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.SupplierRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.SupplierService;
import jakarta.persistence.EntityNotFoundException;

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

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
        request.setCpf("12345678901");
        request.setAddress(new AddressDTO());

        Address mockAddress = new Address();
        NaturalPerson mockSupplier = new NaturalPerson();
        mockSupplier.setName("John Doe");
        mockSupplier.setCpf("12345678901");
        SupplierResponseDTO expectedResponse = new SupplierResponseDTO();
        expectedResponse.setName("John Doe");
        expectedResponse.setCpf("12345678901");

        when(addressMapper.toEntity(any())).thenReturn(mockAddress);
        when(supplierRepository.save(any(NaturalPerson.class))).thenReturn(mockSupplier);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(expectedResponse);

        SupplierResponseDTO result = supplierService.createSupplier(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("12345678901", result.getCpf());
        verify(supplierRepository).save(any(NaturalPerson.class));
    }

    @Test
    void findAllSuppliers() {
        List<Supplier> mockSuppliers = Arrays.asList(new NaturalPerson(), new LegalEntity());
        SupplierResponseDTO mockResponse = new SupplierResponseDTO();
        
        when(supplierRepository.findAll()).thenReturn(mockSuppliers);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(mockResponse);

        List<SupplierResponseDTO> result = supplierService.findAllSuppliers();

        assertNotNull(result);
        assertEquals(2, result.size());
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
    void updateSupplier() {
        Long id = 1L;
        SupplierRequestDTO request = new SupplierRequestDTO();
        request.setName("Updated Name");
        request.setAddress(new AddressDTO());

        NaturalPerson existingSupplier = new NaturalPerson();
        existingSupplier.setAddress(new Address());

        SupplierResponseDTO expectedResponse = new SupplierResponseDTO();
        expectedResponse.setName("Updated Name");

        when(supplierRepository.findById(id)).thenReturn(Optional.of(existingSupplier));
        when(addressMapper.toEntity(any())).thenReturn(new Address());
        when(supplierRepository.save(any(Supplier.class))).thenReturn(existingSupplier);
        when(supplierMapper.toDTO(any(Supplier.class))).thenReturn(expectedResponse);

        SupplierResponseDTO result = supplierService.updateSupplier(id, request);

        assertNotNull(result);
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