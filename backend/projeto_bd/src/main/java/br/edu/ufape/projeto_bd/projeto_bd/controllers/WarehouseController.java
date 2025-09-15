package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.WarehouseRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.WarehouseResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("api/warehouses")
@RequiredArgsConstructor
@Validated
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("/create")
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO request) {
        WarehouseResponseDTO newWarehouse = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWarehouse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(@PathVariable Long id) {
        WarehouseResponseDTO warehouse = warehouseService.findWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

    /**
     * Endpoint para listar todos os armazéns de forma paginada.
     * Ex: /warehouses?page=0&size=10&sort=name,asc
     * @param pageable Objeto que o Spring monta a partir dos parâmetros da URL.
     * @return ResponseEntity com a página de armazéns e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<Page<WarehouseResponseDTO>> getAllWarehouses(@RequestParam(required = false) String name, Pageable pageable) {
        Page<WarehouseResponseDTO> warehouses = warehouseService.findAllWarehouses(name, pageable);
        return ResponseEntity.ok(warehouses);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequestDTO request) {
        WarehouseResponseDTO updatedWarehouse = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(updatedWarehouse);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
    
}
