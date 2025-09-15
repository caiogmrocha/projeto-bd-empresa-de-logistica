package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CustomerRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CustomerResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("api/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO newCustomer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAllCustomers(@RequestParam(required = false) String name, Pageable pageable) {
        Page<CustomerResponseDTO> customers = customerService.findAllCustomers(name, pageable);
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}