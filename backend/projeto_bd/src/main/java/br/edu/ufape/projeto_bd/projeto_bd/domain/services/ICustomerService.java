package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CustomerRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CustomerResponseDTO;

public interface ICustomerService {
    public CustomerResponseDTO createCustomer (CustomerRequestDTO request);

    public CustomerResponseDTO findCustomerById(Long id);

    Page<CustomerResponseDTO> findAllCustomers(Pageable pageable);

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request);

    public void deleteCustomer(Long id);
}
