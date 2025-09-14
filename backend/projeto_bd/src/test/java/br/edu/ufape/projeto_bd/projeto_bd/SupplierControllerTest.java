package br.edu.ufape.projeto_bd.projeto_bd;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ufape.projeto_bd.projeto_bd.controllers.SupplierController;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.AddressDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.SupplierService;

@ExtendWith(MockitoExtension.class)
public class SupplierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private ObjectMapper objectMapper;
    private SupplierRequestDTO supplierRequest;
    private SupplierResponseDTO supplierResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();
        objectMapper = new ObjectMapper();

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("São Paulo");
        addressDTO.setState("SP");
        addressDTO.setZipCode("01000-000");

        supplierRequest = new SupplierRequestDTO();
        supplierRequest.setSupplierType(SupplierType.NATURAL_PERSON);
        supplierRequest.setName("John Doe");
        supplierRequest.setCpf("12345678901");
        supplierRequest.setAddress(addressDTO);

        supplierResponse = new SupplierResponseDTO();
        supplierResponse.setId(1L);
        supplierResponse.setName("John Doe");
        supplierResponse.setSupplierType(SupplierType.NATURAL_PERSON);
        supplierResponse.setCpf("12345678901");
        supplierResponse.setAddress(addressDTO);
    }

    @Test
    void createSupplier_ShouldReturnCreated() throws Exception {
        when(supplierService.createSupplier(any(SupplierRequestDTO.class))).thenReturn(supplierResponse);

        mockMvc.perform(post("/api/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.supplierType").value("NATURAL_PERSON"));
    }

    @Test
    void listSuppliers_ShouldReturnList() throws Exception {
        List<SupplierResponseDTO> suppliers = Arrays.asList(supplierResponse);
        when(supplierService.findAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void getSupplier_ShouldReturnSupplier() throws Exception {
        when(supplierService.findSupplierById(1L)).thenReturn(supplierResponse);

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.supplierType").value("NATURAL_PERSON"));
    }

    @Test
    void updateSupplier_ShouldReturnUpdatedSupplier() throws Exception {
        SupplierResponseDTO updatedResponse = new SupplierResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setName("John Doe Updated");
        updatedResponse.setSupplierType(SupplierType.NATURAL_PERSON);
        
        when(supplierService.updateSupplier(any(Long.class), any(SupplierRequestDTO.class)))
                .thenReturn(updatedResponse);

        supplierRequest.setName("John Doe Updated");

        mockMvc.perform(put("/api/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe Updated"));
    }

    @Test
    void deleteSupplier_ShouldReturnNoContent() throws Exception {
        doNothing().when(supplierService).deleteSupplier(1L);

        mockMvc.perform(delete("/api/suppliers/1"))
                .andExpect(status().isNoContent());
    }
}