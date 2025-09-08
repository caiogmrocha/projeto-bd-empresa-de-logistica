package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Validated
public class SupplierController {
    
    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(@Valid @RequestBody SupplierRequestDTO body) {
        SupplierResponseDTO createdSupplier = supplierService.createSupplier(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplier);
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> listSuppliers() {
        List<SupplierResponseDTO> suppliers = supplierService.findAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplier(@PathVariable Long id) {
        SupplierResponseDTO supplier = supplierService.findSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequestDTO body) {
        SupplierResponseDTO updatedSupplier = supplierService.updateSupplier(id, body);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}