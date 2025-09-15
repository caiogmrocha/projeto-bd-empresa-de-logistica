package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.PageResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierPatchDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.SupplierRequestDTO;
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
    public ResponseEntity<PageResponseDTO<SupplierResponseDTO>> listSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name) {

        Page<SupplierResponseDTO> suppliers = supplierService.findAllSuppliers(page, size, sortBy, direction, name);
        return ResponseEntity.ok(new PageResponseDTO<>(suppliers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplier(@PathVariable Long id) {
        SupplierResponseDTO supplier = supplierService.findSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(@PathVariable Long id,
            @Valid @RequestBody SupplierPatchDTO body) {
        SupplierResponseDTO updatedSupplier = supplierService.updateSupplier(id, body);
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}